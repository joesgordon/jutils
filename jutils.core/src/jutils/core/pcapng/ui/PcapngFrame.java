package jutils.core.pcapng.ui;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.Iterables.Iteratorable;
import jutils.core.SwingUtils;
import jutils.core.data.DataItemPair;
import jutils.core.io.FilePrintStream;
import jutils.core.io.IOUtils;
import jutils.core.io.LogUtils;
import jutils.core.pcapng.IBlock;
import jutils.core.ui.RecentFilesViews;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.FileChooserListener;
import jutils.core.ui.event.FileChooserListener.IFileSelected;
import jutils.core.ui.event.FileChooserListener.ILastFile;
import jutils.core.ui.event.FileDropTarget;
import jutils.core.ui.event.FileDropTarget.IFileDropEvent;
import jutils.core.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class PcapngFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final RecentFilesViews recentViews;
    /**  */
    private final PcapngView content;

    /***************************************************************************
     * 
     **************************************************************************/
    public PcapngFrame()
    {
        this.view = new StandardFrameView();
        this.recentViews = new RecentFilesViews();
        this.content = new PcapngView();

        view.setTitle( "PCAP Viewer" );
        view.setSize( 800, 800 );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        createMenus( view.getFileMenu() );
        view.setToolbar( createToolbar() );
        view.setContent( content.getView() );

        content.getView().setDropTarget( new FileDropTarget(
            ( fde ) -> handleFileDropEvent( fde.getItem() ) ) );

        PcapngOptions options = PcapngOptions.read();
        recentViews.setData( options.recentDirs.toList() );
        recentViews.setListeners( ( f, c ) -> openFile( f ) );
    }

    /***************************************************************************
     * @param fileMenu
     **************************************************************************/
    private void createMenus( JMenu fileMenu )
    {
        int i = 0;

        fileMenu.add( new JMenuItem( createOpenAction() ), i++ );

        fileMenu.add( recentViews.getMenu(), i++ );

        fileMenu.add( new JSeparator(), i++ );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        recentViews.install( toolbar, createOpenListener() );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, createExportAction() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createExportAction()
    {
        IFileSelected ifs = ( f ) -> exportFile( f );
        ILastFile irf = () -> IOUtils.replaceExtension( getLastFileOpened(),
            "csv" );
        FileChooserListener listener = new FileChooserListener( getView(),
            "Save CSV", true, ifs, irf );
        Icon icon = IconConstants.getIcon( IconConstants.EXPORT_16 );

        return new ActionAdapter( listener, "Export", icon );
    }

    /***************************************************************************
     * @param f
     **************************************************************************/
    private void exportFile( File f )
    {
        BlockTableConfig config = new BlockTableConfig();
        Iteratorable<DataItemPair<IBlock>> pairs;

        pairs = new Iteratorable<>( content.getItems() );

        try( FilePrintStream printer = new FilePrintStream( f ) )
        {
            String [] headers = config.getColumnNames();

            for( int i = 0; i < headers.length; i++ )
            {
                if( i > 0 )
                {
                    printer.print( ", " );
                }

                printer.print( headers[i] );
            }

            printer.println();

            for( DataItemPair<IBlock> pair : pairs )
            {
                for( int i = 0; i < headers.length; i++ )
                {
                    if( i > 0 )
                    {
                        printer.print( ", " );
                    }

                    printer.print( "%s", config.getItemData( pair, i ) );
                }
                printer.println();
            }
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createOpenAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FILE_16 );

        return new ActionAdapter( createOpenListener(), "Open File", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private ActionListener createOpenListener()
    {
        IFileSelected ifs = ( f ) -> openFile( f );
        ILastFile ilf = () -> getLastFileOpened();
        FileChooserListener fcl = new FileChooserListener( getView(),
            "Open File", false, ifs, ilf );

        fcl.addExtension( "pcap", "pcapng" );

        return fcl;
    }

    /***************************************************************************
     * @param file
     * @return
     **************************************************************************/
    private void setLastFileOpened( File file )
    {
        PcapngOptions options = PcapngOptions.read();
        options.recentDirs.push( file );
        recentViews.setData( options.recentDirs.toList() );
        PcapngOptions.write( options );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static File getLastFileOpened()
    {
        PcapngOptions options = PcapngOptions.read();

        return options.recentDirs.first();
    }

    /***************************************************************************
     * @param event
     **************************************************************************/
    private void handleFileDropEvent( IFileDropEvent event )
    {
        List<File> files = event.getFiles();

        if( !files.isEmpty() )
        {
            File file = files.get( 0 );
            openFile( file );
        }
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void openFile( File file )
    {
        LogUtils.printDebug( "Opening file %s", file );

        setLastFileOpened( file );

        try
        {
            content.openFile( file );
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return view.getView();
    }
}
