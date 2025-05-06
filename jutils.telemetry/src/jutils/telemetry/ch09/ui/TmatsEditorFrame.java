package jutils.telemetry.ch09.ui;

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
import jutils.telemetry.TelemetryIcons;
import jutils.telemetry.TmatsEditorOptions;
import jutils.telemetry.ch09.Tmats;
import jutils.telemetry.ch09.TmatsFile;
import jutils.telemetry.ch09.io.ascii.TmatsParser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsEditorFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final StringView setupView;
    /**  */
    private final StringView printView;
    /**  */
    private final TmatsTreeView tmatsView;
    /**  */
    private final RecentFilesViews recentFiles;

    /***************************************************************************
     * 
     **************************************************************************/
    public TmatsEditorFrame()
    {
        this.view = new StandardFrameView();
        this.setupView = new StringView();
        this.printView = new StringView();
        this.tmatsView = new TmatsTreeView();
        this.recentFiles = new RecentFilesViews();

        view.setTitle( "TMATS Editor" );
        view.setSize( 800, 800 );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        createFileMenu( view.getFileMenu() );
        view.setToolbar( createToolbar() );
        view.setContent( createContent() );
        view.getView().setIconImages( TelemetryIcons.getAppImages() );

        view.getContent().setDropTarget(
            new FileDropTarget( ( e ) -> handleFileDropped( e.getItem() ) ) );

        recentFiles.setListeners( ( f, c ) -> openFile( f ) );
        recentFiles.setData(
            TmatsEditorOptions.getOptions().recentFiles.toList() );

        setupView.setEditable( false );
        printView.setEditable( false );

        setData( tmatsView.getData(), "" );
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

            TmatsFile file = new TmatsFile( f, tmats );
            setData( file, setup );
        }
        catch( ValidationException ex )
        {
            OptionUtils.showErrorMessage( getView(),
                "Unable to open " + f.getName(), "Parsing Error" );
            return;
        }

    }

    /***************************************************************************
     * @param file
     * @param setup
     **************************************************************************/
    private void setData( TmatsFile file, String setup )
    {
        tmatsView.setData( file );
        setupView.setData( setup );
        printView.setData( FieldPrinter.toString( file.tmats ) );
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
        ILastFile ilf = () -> TmatsEditorOptions.getOptions().recentFiles.first();
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
        tabs.addTab( "Print", printView.getView() );
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
