package jutils.iris.ui;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.io.ByteArrayStream;
import jutils.core.io.DataStream;
import jutils.core.ui.RecentFilesViews;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.FileChooserListener;
import jutils.core.ui.event.FileChooserListener.IFileSelected;
import jutils.core.ui.event.FileChooserListener.ILastFile;
import jutils.core.ui.event.FileDropTarget;
import jutils.core.ui.event.FileDropTarget.IFileDropEvent;
import jutils.core.ui.event.ItemActionEvent;
import jutils.core.ui.model.IView;
import jutils.core.utils.ByteOrdering;
import jutils.iris.IrisIcons;
import jutils.iris.IrisOptions;
import jutils.iris.IrisUtils;
import jutils.iris.albums.RasterListAlbum;
import jutils.iris.colors.BayerColorizer;
import jutils.iris.colors.IColorizer;
import jutils.iris.colors.MonoColorizer;
import jutils.iris.data.BayerOrder;
import jutils.iris.io.IRasterAlbumReader;
import jutils.iris.rasters.BayerRaster;
import jutils.iris.rasters.IRaster;
import jutils.iris.rasters.Mono8Raster;

/*******************************************************************************
 * Defines the frame that displays the components of the Iris application.
 ******************************************************************************/
public class IrisFrame implements IView<JFrame>
{
    /** The frame view created in {@link #IrisFrame()}. */
    private final StandardFrameView frameView;
    /**  */
    private final RecentFilesViews recentOpenedFiles;
    /**  */
    private final Action saveAction;
    /**  */
    private final IrisView viewer;
    /**  */
    private final SaveOptionsView saveView;

    /***************************************************************************
     * 
     **************************************************************************/
    public IrisFrame()
    {
        this.frameView = new StandardFrameView();
        this.recentOpenedFiles = new RecentFilesViews( 20 );
        this.saveView = new SaveOptionsView();
        this.saveAction = createSaveAction();
        this.viewer = new IrisView();

        frameView.setTitle( "Iris" );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 800, 800 );
        frameView.setToolbar( createToolbar() );
        frameView.setContent( viewer.getView() );
        frameView.getView().setIconImages( IrisIcons.getAppImages() );

        createMenus( frameView.getMenuBar(), frameView.getFileMenu() );

        viewer.getView().setDropTarget(
            new FileDropTarget( ( ie ) -> handleFileDropped( ie ) ) );

        recentOpenedFiles.setListeners( ( f, c ) -> handleOpenFile( f ) );
        recentOpenedFiles.setData(
            IrisOptions.getOptions().lastOpenedFiles.toList() );
    }

    /***************************************************************************
     * @param menuBar
     * @param fileMenu
     **************************************************************************/
    private void createMenus( JMenuBar menuBar, JMenu fileMenu )
    {
        createFileMenu( fileMenu );

        menuBar.add( createPatternMenu() );
    }

    /***************************************************************************
     * @param fileMenu
     **************************************************************************/
    private void createFileMenu( JMenu fileMenu )
    {
        int i = 0;

        fileMenu.add( new JMenuItem( createOpenAction() ), i++ );
        fileMenu.add( recentOpenedFiles.getMenu(), i++ );
        fileMenu.add( new JMenuItem( saveAction ), i++ );
        fileMenu.add( new JMenuItem( createCloseAction() ), i++ );
        fileMenu.add( new JSeparator(), i++ );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createPatternMenu()
    {
        JMenu menu = new JMenu( "Patterns" );
        JMenuItem item;

        menu.setMnemonic( 'P' );

        item = menu.add( "Diagonal Gradients" );
        item.setMnemonic( 'D' );
        item.addActionListener( ( e ) -> handleDiagonalGradient() );

        item = menu.add( "Julia Fractals" );
        item.setMnemonic( 'J' );
        item.addActionListener( ( e ) -> handleJuliaFractal() );

        item = menu.add( "Bayer Gray" );
        item.setMnemonic( 'B' );
        item.addActionListener( ( e ) -> handleBayerGray() );

        return menu;
    }

    /***************************************************************************
     * @return the new action that opens a file of the user's choice.
     * @see #openFile(File)
     **************************************************************************/
    private Action createOpenAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );
        return new ActionAdapter( createOpenActionListener(), "Open", icon );
    }

    /***************************************************************************
     * @return the action that opens images and converts to grayscale.
     * @see #openFile(File)
     **************************************************************************/
    private ActionListener createOpenActionListener()
    {
        IFileSelected fileSelected = ( f ) -> handleOpenFile( f );
        ILastFile lastFile = () -> IrisOptions.getOptions().getLastOpenedFile();
        FileChooserListener listener = new FileChooserListener(
            frameView.getView(), "Open Image", false, fileSelected, lastFile );

        for( IRasterAlbumReader reader : viewer.getReaders() )
        {
            String name = reader.getName() + " Image Files";

            listener.addExtension( name, reader.getExtensions() );
        }

        return listener;
    }

    /***************************************************************************
     * @return the new action that save's images
     * @see #saveFile(File)
     **************************************************************************/
    private Action createSaveAction()
    {
        IFileSelected ifs = ( f ) -> handleSaveFile( f );
        ILastFile ilf = () -> IrisOptions.getOptions().getLastSavedFile();
        FileChooserListener chooserListener = new FileChooserListener(
            frameView.getView(), "Save Images", true, ifs, ilf );

        JComponent comp = saveView.getView();

        chooserListener.setAdditional( comp );

        chooserListener.addExtension( "PNG Files", "png" );
        chooserListener.addExtension( "JPG Files", "jpg" );
        chooserListener.addExtension( "Bitmap Files", "bmp" );

        ActionListener listener = ( e ) -> {
            // SaveOptions options = saveView.getData();
            // options.firstIndex = viewer.getIndex() + 1;
            // optionsView.setData( options );
            // chooserListener.actionPerformed( e );
        };

        return new ActionAdapter( listener, "Save",
            IconConstants.getIcon( IconConstants.SAVE_16 ) );
    }

    /***************************************************************************
     * @return the new action that closes the currently open file.
     * @see #closeFile()
     **************************************************************************/
    private Action createCloseAction()
    {
        ActionListener listener = ( e ) -> handleCloseFile();

        return new ActionAdapter( listener, "Close File",
            IconConstants.getIcon( IconConstants.FILE_CLOSE_016 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        recentOpenedFiles.install( toolbar, createOpenAction() );

        SwingUtils.addActionToToolbar( toolbar, saveAction );

        toolbar.addSeparator();

        JToggleButton infoButton = new JToggleButton();
        infoButton.setIcon( IconConstants.getIcon( IconConstants.CHECK_16 ) );
        infoButton.setText( "Info" );
        infoButton.addActionListener(
            ( e ) -> handleImageInfo( infoButton.isSelected() ) );
        toolbar.add( infoButton );

        return toolbar;
    }

    /***************************************************************************
     * @param ie
     **************************************************************************/
    private void handleFileDropped( ItemActionEvent<IFileDropEvent> ie )
    {
        IFileDropEvent fde = ie.getItem();
        List<File> files = fde.getFiles();

        if( files.size() == 1 )
        {
            File file = files.get( 0 );

            handleOpenFile( file );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleDiagonalGradient()
    {
        int w = 512;
        int h = 512;
        IRaster r = new Mono8Raster( w, h );

        IrisUtils.setDiagonalGradient( r );

        IColorizer colorizer = new MonoColorizer();
        RasterListAlbum rasters = new RasterListAlbum();

        rasters.addRaster( r );
        rasters.setColorizer( colorizer );

        viewer.setAlbum( rasters );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleBayerGray()
    {
        int w = 512;
        int h = 512;
        BayerRaster r = new BayerRaster( w, h, 12, BayerOrder.GRBG );

        try( ByteArrayStream bas = new ByteArrayStream( r.buffer,
            r.buffer.length, 0, false );
             DataStream stream = new DataStream( bas,
                 ByteOrdering.LITTLE_ENDIAN ) )
        {
            for( int i = 0; i < r.pixels.length; i++ )
            {
                int y = i / w;
                int x = i - ( y * w );

                boolean isEvenRow = ( y % 2 ) == 0;
                boolean isEvenCol = ( x % 2 ) == 0;
                boolean isG1 = isEvenRow && isEvenCol;
                boolean isR = isEvenRow && !isEvenCol;
                boolean isB = !isEvenRow && isEvenCol;
                // boolean isG2 = !isEvenRow && !isEvenCol;

                short s;

                if( isG1 )
                {
                    s = 190;
                }
                else if( isR )
                {
                    s = 187;
                }
                else if( isB )
                {
                    s = 186;
                }
                else
                {
                    s = 185;
                }
                r.pixels[i] = s;
            }
        }
        catch( IOException ex )
        {
            String err = String.format( "Unable to write %d pixels to %d bytes",
                r.pixels.length, r.buffer.length );

            throw new RuntimeException( err, ex );
        }

        IColorizer colorizer = new BayerColorizer( BayerOrder.GRBG );
        RasterListAlbum rasters = new RasterListAlbum();

        rasters.addRaster( r );
        rasters.setColorizer( colorizer );

        viewer.setAlbum( rasters );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleJuliaFractal()
    {
        int w = 512;
        int h = 512;
        IRaster r = new Mono8Raster( w, h );

        IrisUtils.setJuliaFractal( r );

        IColorizer colorizer = new MonoColorizer();
        RasterListAlbum rasters = new RasterListAlbum();

        rasters.addRaster( r );
        rasters.setColorizer( colorizer );

        viewer.setAlbum( rasters );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void handleOpenFile( File file )
    {
        IrisOptions options = IrisOptions.getOptions();

        options.lastOpenedFiles.push( file );
        IrisOptions.setOptions( options );
        recentOpenedFiles.setData( options.lastOpenedFiles.toList() );

        viewer.openFile( file );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void handleSaveFile( File file )
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleCloseFile()
    {
        viewer.resetImages();
    }

    /***************************************************************************
     * @param showInfo
     **************************************************************************/
    private void handleImageInfo( boolean showInfo )
    {
        viewer.setInfoVisible( showInfo );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }
}
