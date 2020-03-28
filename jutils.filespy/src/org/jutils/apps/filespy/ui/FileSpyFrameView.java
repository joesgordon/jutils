package org.jutils.apps.filespy.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.jutils.apps.filespy.FileSpyMain;
import org.jutils.apps.filespy.data.FileSpyData;
import org.jutils.apps.filespy.data.SearchParams;
import org.jutils.apps.filespy.search.Searcher;
import org.jutils.core.IconConstants;
import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.Utils;
import org.jutils.core.ValidationException;
import org.jutils.core.io.XStreamUtils;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.licensing.LicenseDialog;
import org.jutils.core.time.TimeUtils;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.StatusBarPanel;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.explorer.AppManagerView;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileSpyFrameView implements IView<JFrame>
{
    /**  */
    private final OptionsSerializer<FileSpyData> options;

    /**  */
    private final JButton startButton;
    /**  */
    private final Icon startIcon;
    /**  */
    private final Icon stopIcon;

    /**  */
    private final StandardFrameView view;
    /**  */
    private final StatusBarPanel statusBar;

    /**  */
    private final SearchParamsView spyPanel;
    /**  */
    private final ResultsView resultsView;

    /**  */
    private final AtomicReference<Searcher> searcher;

    /***************************************************************************
     *
     **************************************************************************/
    public FileSpyFrameView()
    {
        this.options = FileSpyMain.getOptions();

        this.startButton = new JButton();
        this.startIcon = IconConstants.getIcon( IconConstants.FIND_16 );
        this.stopIcon = IconConstants.getIcon( IconConstants.STOP_16 );

        this.spyPanel = new SearchParamsView();
        this.view = new StandardFrameView();
        this.statusBar = view.getStatusBar();
        this.resultsView = new ResultsView();

        this.searcher = new AtomicReference<>();

        spyPanel.setData( options.getOptions().lastParams );
        statusBar.setText( "" );

        createMenuBar( view.getMenuBar(), view.getFileMenu() );
        view.setSize( 850, 800 );
        view.setTitle( "FileSpy" );

        view.getView().setIconImages( IconConstants.getImages(
            IconConstants.PAGEMAG_16, IconConstants.PAGEMAG_32,
            IconConstants.PAGEMAG_64, IconConstants.PAGEMAG_128 ) );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        view.setToolbar( createToolBar() );
        view.setContent( createContent() );

        setSearchButtonState( true );

        spyPanel.addStartListener( ( e ) -> stopStartSearch() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        JPanel contentPane = new JPanel( new BorderLayout() );

        contentPane.add( spyPanel.getView(), BorderLayout.NORTH );
        contentPane.add( resultsView.getView(), java.awt.BorderLayout.CENTER );

        return contentPane;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolBar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, createNewAction() );
        SwingUtils.addActionToToolbar( toolbar, createOpenAction() );
        SwingUtils.addActionToToolbar( toolbar, createSaveSearchAction() );
        SwingUtils.addActionToToolbar( toolbar, createSaveResultsAction() );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, createStartStopAction(),
            startButton );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createNewAction()
    {
        Action action;
        Icon icon = IconConstants.getIcon( IconConstants.NEW_FILE_16 );
        ActionListener l = ( e ) -> spyPanel.setData( new SearchParams() );

        action = new ActionAdapter( l, "New", icon );

        SwingUtils.setActionToolTip( action, "Creates a new search" );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createOpenAction()
    {
        Action action;
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FILE_16 );
        ActionListener l = ( e ) -> openSearchParams();

        action = new ActionAdapter( l, "Open", icon );

        SwingUtils.setActionToolTip( action,
            "Opens a previously saved search" );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveSearchAction()
    {
        Action action;
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );
        ActionListener l = ( e ) -> saveSearchParams();

        action = new ActionAdapter( l, "Save Search", icon );

        SwingUtils.setActionToolTip( action, "Saves the current search terms" );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveResultsAction()
    {
        Action action;
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_AS_16 );
        ActionListener l = ( e ) -> saveSearchResults();

        action = new ActionAdapter( l, "Save Results", icon );

        SwingUtils.setActionToolTip( action, "Saves search results" );

        return action;
    }

    private Action createStartStopAction()
    {
        Action action;
        Icon icon = startIcon;
        ActionListener l = new StartButtonListener();

        action = new ActionAdapter( l, "Start", icon );

        return action;
    }

    /***************************************************************************
     * @param jMenu
     * @param jMenuBar
     * @return
     **************************************************************************/
    private JMenuBar createMenuBar( JMenuBar menuBar, JMenu fileMenu )
    {
        createFileMenu( fileMenu );

        menuBar.add( createToolsMenu() );
        menuBar.add( createHelpMenu() );

        return menuBar;
    }

    /***************************************************************************
     * @param fileMenu
     * @return
     **************************************************************************/
    private JMenu createFileMenu( JMenu fileMenu )
    {
        int index = 0;

        fileMenu.add( new JMenuItem( createNewAction() ), index++ );
        fileMenu.add( new JMenuItem( createOpenAction() ), index++ );
        fileMenu.add( new JMenuItem( createSaveSearchAction() ), index++ );
        fileMenu.add( new JMenuItem( createSaveResultsAction() ), index++ );

        fileMenu.add( new JSeparator(), index++ );

        return fileMenu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createToolsMenu()
    {
        JMenu toolsMenu = new JMenu( "Tools" );

        JMenuItem regexMenuItem = new JMenuItem( "Test RegEx" );
        regexMenuItem.setToolTipText(
            "Allows you to test a regular " + "expression." );
        regexMenuItem.addActionListener( ( e ) -> {
            showRegexHelper();
        } );

        JMenuItem fileOptionsMenuItem = new JMenuItem( "File Options" );
        fileOptionsMenuItem.setIcon(
            IconConstants.getIcon( IconConstants.EDIT_16 ) );
        fileOptionsMenuItem.addActionListener( ( e ) -> {
            showFileConfig();
        } );

        toolsMenu.add( regexMenuItem );
        toolsMenu.add( fileOptionsMenuItem );

        return toolsMenu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createHelpMenu()
    {
        JMenu helpMenu = new JMenu( "Help" );

        JMenuItem aboutMenuItem = new JMenuItem( "About" );
        aboutMenuItem.setToolTipText(
            "Presents infomation about this program." );
        aboutMenuItem.addActionListener( new AboutListener( getView() ) );

        helpMenu.add( aboutMenuItem );

        return helpMenu;
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    private void openSearchParams()
    {
        SearchParams params = null;
        JFileChooser chooser = new JFileChooser();
        int result = JFileChooser.ERROR_OPTION;
        File fileChosen = null;
        FileSpyData configData = options.getOptions();

        chooser.setCurrentDirectory( configData.lastSavedLocation );
        chooser.setAcceptAllFileFilterUsed( false );
        chooser.setFileFilter( new FileSpySearchFilter() );
        chooser.setDialogTitle( "Open Search File" );

        result = chooser.showOpenDialog( getView() );
        if( result == JFileChooser.APPROVE_OPTION )
        {
            options.write();

            fileChosen = chooser.getSelectedFile();
            if( fileChosen != null && fileChosen.isFile() )
            {
                try
                {
                    params = XStreamUtils.readObjectXStream( fileChosen );
                    spyPanel.setData( params );
                }
                catch( IOException | ValidationException ex )
                {
                    OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                        "I/O ERROR" );
                }
            }
        }
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    private void saveSearchParams()
    {
        SearchParams params = spyPanel.getData();
        JFileChooser chooser = new JFileChooser();
        int result = JFileChooser.ERROR_OPTION;
        File fileChosen = null;
        FileSpyData configData = options.getOptions();

        chooser.setCurrentDirectory( configData.lastSavedLocation );
        chooser.setAcceptAllFileFilterUsed( false );
        chooser.setFileFilter( new FileSpySearchFilter() );
        chooser.setDialogTitle( "Save As" );

        result = chooser.showSaveDialog( getView() );
        if( result == JFileChooser.APPROVE_OPTION )
        {
            options.write();

            fileChosen = chooser.getSelectedFile();
            if( !fileChosen.getName().endsWith(
                FileSpySearchFilter.FILESPY_SEARCH_EXT ) )
            {
                fileChosen = new File( fileChosen.getParentFile(),
                    fileChosen.getName() +
                        FileSpySearchFilter.FILESPY_SEARCH_EXT );
            }

            try
            {
                XStreamUtils.writeObjectXStream( params, fileChosen );
            }
            catch( IOException ex )
            {
                OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                    "I/O ERROR" );
            }
            catch( ValidationException ex )
            {
                OptionUtils.showErrorMessage( getView(),
                    "The file could not be saved: " +
                        fileChosen.getAbsolutePath() + Utils.NEW_LINE +
                        ex.getMessage(),
                    "Serialization Error" );
            }
        }
    }

    /**
     * 
     */
    private void saveSearchResults()
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    private void showRegexHelper()
    {
        JDialog dialog = new JDialog( getView() );
        dialog.setTitle( "Regex Friend" );
        dialog.setContentPane( new RegexPanel().getView() );
        dialog.setSize( new Dimension( 500, 500 ) );
        dialog.validate();
        dialog.setLocationRelativeTo( null );
        dialog.setVisible( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showFileConfig()
    {
        AppManagerView.showDialog( getView() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void startSearch()
    {
        SearchParams params = spyPanel.getData();

        try
        {
            params.validate();
        }
        catch( ValidationException ex )
        {
            OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                "Input Validation Error" );
            return;
        }

        options.getOptions().lastParams = params;
        options.write();

        Searcher s = new Searcher( resultsView, statusBar );

        searcher.set( s );

        s.search( params, ( e ) -> SwingUtilities.invokeLater(
            () -> setSearchFinished( e.getItem() ) ) );

        setSearchButtonState( false );

        options.write();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void stopSearch()
    {
        this.searcher.getAndSet( null ).cancel();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void stopStartSearch()
    {
        Searcher s = searcher.get();

        if( s == null )
        {
            startSearch();
        }
        else
        {
            stopSearch();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setSearchButtonState( boolean start )
    {
        Icon icon = stopIcon;
        String text = "Stop";

        if( start )
        {
            icon = startIcon;
            text = "Start";
        }

        startButton.setIcon( icon );
        startButton.setText( text );
        startButton.setActionCommand( text );
    }

    /***************************************************************************
     * @param millis
     **************************************************************************/
    private void setSearchFinished( long millis )
    {
        startButton.setIcon( startIcon );
        startButton.setText( "Start" );
        startButton.setActionCommand( "Start" );

        int rowCount = resultsView.getRecordCount();
        String elapsed = TimeUtils.durationToString( millis );

        statusBar.setText( rowCount + " file(s) found in " + elapsed + "." );

        searcher.set( null );

        // LogUtils.printDebug( "Search Finished" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class StartButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            String str = e.getActionCommand();

            if( str.compareTo( "Start" ) == 0 )
            {
                startSearch();
            }
            else if( str.compareTo( "Stop" ) == 0 )
            {
                stopSearch();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AboutListener implements ActionListener
    {
        private final JFrame frame;

        public AboutListener( JFrame frame )
        {
            this.frame = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            LicenseDialog ld = new LicenseDialog( frame );
            JDialog d = ld.getView();

            d.setLocationRelativeTo( frame );
            d.setVisible( true );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileSpySearchFilter
        extends javax.swing.filechooser.FileFilter
    {
        public static final String FILESPY_SEARCH_EXT = ".fss";

        public static final String FILESPY_SEARCH_DESC = "File Spy Search (*" +
            FILESPY_SEARCH_EXT + ")";

        public FileSpySearchFilter()
        {
            ;
        }

        @Override
        public boolean accept( File file )
        {
            String name = file.getName();
            if( file.isDirectory() || name.endsWith( FILESPY_SEARCH_EXT ) )
            {
                return true;
            }
            return false;
        }

        @Override
        public String getDescription()
        {
            return FILESPY_SEARCH_DESC;
        }
    }
}
