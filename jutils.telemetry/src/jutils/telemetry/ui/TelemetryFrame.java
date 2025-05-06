package jutils.telemetry.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import jutils.core.SwingUtils;
import jutils.core.ValidationException;
import jutils.core.ui.RecentFilesViews;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.event.FileChooserListener;
import jutils.core.ui.event.FileChooserListener.IFileSelected;
import jutils.core.ui.event.FileChooserListener.ILastFile;
import jutils.core.ui.event.FileDropTarget;
import jutils.core.ui.event.FileDropTarget.IFileDropEvent;
import jutils.core.ui.model.IView;
import jutils.telemetry.TelemetryIcons;
import jutils.telemetry.TelemetryOptions;
import jutils.telemetry.ch10.Ch10File;
import jutils.telemetry.ch10.io.Ch10FileReader;
import jutils.telemetry.ch10.ui.Ch10View;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TelemetryFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final Ch10View c10View;
    /**  */
    private final RecentFilesViews recentFiles;

    /***************************************************************************
     * 
     **************************************************************************/
    public TelemetryFrame()
    {
        this.view = new StandardFrameView();
        this.c10View = new Ch10View();
        this.recentFiles = new RecentFilesViews();

        view.setTitle( "Telemetry Viewer" );
        view.setSize( 800, 800 );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        createFileMenu( view.getFileMenu() );
        view.setToolbar( createToolbar() );
        view.setContent( createContent() );
        view.getView().setIconImages( TelemetryIcons.getAppImages() );

        recentFiles.setListeners( ( f, c ) -> openFile( f ) );
        recentFiles.setData(
            TelemetryOptions.getOptions().recentFiles.toList() );
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
        ILastFile ilf = () -> TelemetryOptions.getOptions().recentFiles.first();
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
        JPanel panel = new JPanel( new BorderLayout() );

        panel.setDropTarget(
            new FileDropTarget( ( e ) -> handleFileDropped( e.getItem() ) ) );
        panel.add( c10View.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @param event
     **************************************************************************/
    private void handleFileDropped( IFileDropEvent event )
    {
        List<File> files = event.getFiles();

        openFile( files.get( 0 ) );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void openFile( File file )
    {
        TelemetryOptions options = TelemetryOptions.getOptions();
        options.recentFiles.push( file );
        TelemetryOptions.setOptions( options );
        recentFiles.setData(
            TelemetryOptions.getOptions().recentFiles.toList() );

        Ch10FileReader reader = new Ch10FileReader();

        try
        {
            Ch10File c10 = reader.read( file );

            // c10.printInfo();

            Ch10File oldFile = c10View.getData();

            oldFile.stream.close();

            c10View.setData( c10 );
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
