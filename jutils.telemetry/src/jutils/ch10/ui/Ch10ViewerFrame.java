package jutils.ch10.ui;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import jutils.core.SwingUtils;
import jutils.core.ValidationException;
import jutils.core.io.DataStream;
import jutils.core.io.FieldPrinter;
import jutils.core.io.FileStream;
import jutils.core.io.IDataStream;
import jutils.core.io.LogUtils;
import jutils.core.ui.RecentFilesViews;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.event.FileChooserListener;
import jutils.core.ui.event.FileChooserListener.IFileSelected;
import jutils.core.ui.event.FileChooserListener.ILastFile;
import jutils.core.ui.event.FileDropTarget;
import jutils.core.ui.event.FileDropTarget.IFileDropEvent;
import jutils.core.ui.event.ItemActionEvent;
import jutils.core.ui.model.IView;
import jutils.core.utils.ByteOrdering;
import jutils.telemetry.ch10.PacketHeader;
import jutils.telemetry.ch10.PacketHeader.PacketHeaderSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ch10ViewerFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final RecentFilesViews recentFiles;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ch10ViewerFrame()
    {
        this.view = new StandardFrameView();
        this.recentFiles = new RecentFilesViews();

        view.setTitle( "Chapter 10 Viewer" );
        view.setSize( 800, 800 );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        createFileMenu( view.getFileMenu() );
        view.setToolbar( createToolbar() );
        view.setContent( createContent() );

        recentFiles.setListeners( ( f, c ) -> openFile( f ) );
        recentFiles.setData(
            Ch10ViewerOptions.getOptions().recentFiles.toList() );
    }

    /***************************************************************************
     * @param fileMenu
     **************************************************************************/
    private void createFileMenu( JMenu fileMenu )
    {
        int i = 0;

        fileMenu.add( recentFiles.getMenu(), i++ );
        fileMenu.add( new JSeparator(), i++ );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        recentFiles.install( toolbar, createOpenListener() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private ActionListener createOpenListener()
    {
        IFileSelected ifs = ( f ) -> openFile( f );
        ILastFile ilf = () -> Ch10ViewerOptions.getOptions().recentFiles.first();
        FileChooserListener fcl = new FileChooserListener( getView(),
            "Open File", false, ifs, ilf );

        fcl.addExtension( "IRIG-106 Chapter 10 File", "ch10", "c10" );

        return fcl;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        JPanel panel = new JPanel();

        panel.setDropTarget(
            new FileDropTarget( ( e ) -> handleFileDropped( e ) ) );

        return panel;
    }

    /***************************************************************************
     * @param event
     **************************************************************************/
    private void handleFileDropped( ItemActionEvent<IFileDropEvent> event )
    {
        IFileDropEvent fde = event.getItem();
        List<File> files = fde.getFiles();

        openFile( files.get( 0 ) );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void openFile( File file )
    {
        Ch10ViewerOptions options = Ch10ViewerOptions.getOptions();
        options.recentFiles.push( file );
        Ch10ViewerOptions.setOptions( options );
        recentFiles.setData(
            Ch10ViewerOptions.getOptions().recentFiles.toList() );

        try( FileStream fs = new FileStream( file, true );
             IDataStream stream = new DataStream( fs,
                 ByteOrdering.INTEL_ORDER ) )
        {
            PacketHeader header = new PacketHeader();
            PacketHeaderSerializer serializer = new PacketHeaderSerializer();

            FieldPrinter fp = new FieldPrinter();
            int i = 0;
            while( PacketHeader.SIZE < stream.getAvailable() )
            {
                serializer.read( header, stream );

                // LogUtils.print( "%d, %d, %d, %d, %s, %d", i,
                // header.channelId,
                // header.sequenceNumber & 0xFF, header.packetLength,
                // header.dataType.name, header.relativeTimeCounter );
                fp.printTier( "Packet " + i, header );

                long skipSize = header.packetLength - PacketHeader.SIZE;

                stream.skip( skipSize );
                i++;
            }

            LogUtils.print( fp.toString() );
        }
        catch( FileNotFoundException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        catch( ValidationException ex )
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
