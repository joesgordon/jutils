package jutils.iris.ui;

import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import jutils.core.IconConstants;
import jutils.core.ui.RecentFilesViews;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.FileChooserListener;
import jutils.core.ui.event.FileChooserListener.IFileSelected;
import jutils.core.ui.event.FileChooserListener.ILastFile;
import jutils.core.ui.model.IView;
import jutils.iris.IrisIcons;
import jutils.iris.IrisMain;
import jutils.iris.IrisUtils;
import jutils.iris.colors.IColorizer;
import jutils.iris.colors.MonoColorizer;
import jutils.iris.data.IRaster;
import jutils.iris.data.RasterAlbumList;
import jutils.iris.io.IRasterAlbumReader;
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
    private final IrisView contentView;

    /***************************************************************************
     * 
     **************************************************************************/
    public IrisFrame()
    {
        this.frameView = new StandardFrameView();
        this.recentOpenedFiles = new RecentFilesViews( 20 );
        this.saveAction = createSaveAction();
        this.contentView = new IrisView();

        frameView.setTitle( "Iris" );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 800, 800 );
        frameView.setContent( contentView.getView() );
        frameView.getView().setIconImages( IrisIcons.getAppImages() );

        createMenus( frameView.getMenuBar(), frameView.getFileMenu() );
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

        item = menu.add( "Diagonal Gradients" );
        item.setMnemonic( 'D' );
        item.addActionListener( ( e ) -> handleDiagonalGradient() );

        item = menu.add( "Julia Fractals" );
        item.setMnemonic( 'J' );
        item.addActionListener( ( e ) -> handleJuliaFractal() );

        menu.setMnemonic( 'P' );

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
        ILastFile lastFile = () -> IrisMain.getOptions().getOptions().getLastOpenedFile();
        FileChooserListener listener = new FileChooserListener(
            frameView.getView(), "Open Image", false, fileSelected, lastFile );

        for( IRasterAlbumReader reader : contentView.getReaders() )
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
        // IFileSelected ifs = ( f ) -> handleSaveFile( f );
        // ILastFile ilf = () ->
        // IrisMain.getOptions().getOptions().getLastSavedFile();
        // FileChooserListener chooserListener = new FileChooserListener(
        // frameView.getView(), "Save Images", true, ifs, ilf );

        // JComponent comp = optionsView.view;
        //
        // chooserListener.setAdditional( comp );
        //
        // chooserListener.addExtension( "PNG Files", "png" );
        // chooserListener.addExtension( "JPG Files", "jpg" );
        // chooserListener.addExtension( "Bitmap Files", "bmp" );

        ActionListener listener = ( e ) -> {
            // SaveOptions options = optionsView.getData();
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
     * 
     **************************************************************************/
    private void handleDiagonalGradient()
    {
        int w = 512;
        int h = 512;
        IRaster r = new Mono8Raster( w, h );

        IrisUtils.setDiagonalGradient( r );

        IColorizer colorizer = new MonoColorizer();
        RasterAlbumList rasters = new RasterAlbumList();

        rasters.addRaster( r );
        rasters.setColorizer( colorizer );

        contentView.setImages( rasters );
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
        RasterAlbumList rasters = new RasterAlbumList();

        rasters.addRaster( r );
        rasters.setColorizer( colorizer );

        contentView.setImages( rasters );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void handleOpenFile( File file )
    {
        contentView.openFile( file );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void handleSaveFile( File file )
    {
        // TODO Auto-generated method stub
    }

    private void handleCloseFile()
    {
        ;
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
