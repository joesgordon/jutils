package jutils.telemetry.ui.ch09;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import jutils.core.OptionUtils;
import jutils.core.SwingUtils;
import jutils.core.ValidationException;
import jutils.core.io.FieldPrinter;
import jutils.core.io.IOUtils;
import jutils.core.ui.RecentFilesViews;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.StringView;
import jutils.core.ui.event.FileChooserListener;
import jutils.core.ui.event.FileChooserListener.IFileSelected;
import jutils.core.ui.event.FileChooserListener.ILastFile;
import jutils.core.ui.event.FileDropTarget;
import jutils.core.ui.event.FileDropTarget.IFileDropEvent;
import jutils.core.ui.model.IView;
import jutils.telemetry.TedeOptions;
import jutils.telemetry.TelemetryIcons;
import jutils.telemetry.data.ch09.Tmats;
import jutils.telemetry.io.ch09.ascii.TmatsParser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TedeFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final StringView setupView;
    /**  */
    private final StringView tmatsView;
    /**  */
    private final RecentFilesViews recentFiles;

    /***************************************************************************
     * 
     **************************************************************************/
    public TedeFrame()
    {
        this.view = new StandardFrameView();
        this.setupView = new StringView();
        this.tmatsView = new StringView();
        this.recentFiles = new RecentFilesViews();

        view.setTitle( "Telemetry Viewer" );
        view.setSize( 800, 800 );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        createFileMenu( view.getFileMenu() );
        view.setToolbar( createToolbar() );
        view.setContent( createContent() );
        view.getView().setIconImages( TelemetryIcons.getAppImages() );

        view.getContent().setDropTarget(
            new FileDropTarget( ( e ) -> handleFileDropped( e.getItem() ) ) );

        recentFiles.setListeners( ( f, c ) -> openFile( f ) );
        recentFiles.setData( TedeOptions.getOptions().recentFiles.toList() );

        setupView.setEditable( false );
        tmatsView.setEditable( false );
    }

    /***************************************************************************
     * @param f
     **************************************************************************/
    public void openFile( File f )
    {
        TmatsParser parser = new TmatsParser();
        String setup;
        Tmats tmats;

        try
        {
            setup = IOUtils.readAll( f );
        }
        catch( FileNotFoundException ex )
        {
            OptionUtils.showErrorMessage( getView(),
                "Unable to open " + f.getName(), "File Not Found" );
            return;
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( getView(),
                "Unable to open " + f.getName(), "I/O Error" );
            return;
        }

        try
        {
            tmats = parser.parse( setup );
        }
        catch( ValidationException ex )
        {
            OptionUtils.showErrorMessage( getView(),
                "Unable to open " + f.getName(), "Parsing Error" );
            return;
        }

        setupView.setData( setup );
        tmatsView.setData( FieldPrinter.toString( tmats ) );
    }

    /***************************************************************************
     * @param evt
     **************************************************************************/
    private void handleFileDropped( IFileDropEvent evt )
    {
        List<File> files = evt.getFiles();
        File file = files.get( 0 );

        openFile( file );
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
        ILastFile ilf = () -> TedeOptions.getOptions().recentFiles.first();
        FileChooserListener fcl = new FileChooserListener( getView(),
            "Open File", false, ifs, ilf );

        fcl.addExtension( "IRIG-106 Chapter 09 TMATS File", Tmats.EXTENSIONS );

        return fcl;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "TMATS", tmatsView.getView() );
        tabs.addTab( "Setup", setupView.getView() );

        return tabs;
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
