package org.jutils.duak.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.jutils.core.IconConstants;
import org.jutils.core.JUtilsInfo;
import org.jutils.core.data.BuildInfo;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.task.TaskView;
import org.jutils.core.ui.BuildInfoView;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.RecentFilesViews;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.event.DirectoryChooserListener;
import org.jutils.core.ui.event.FileChooserListener.IFileSelected;
import org.jutils.core.ui.event.FileChooserListener.ILastFile;
import org.jutils.core.ui.event.FileDropTarget;
import org.jutils.core.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.core.ui.event.ItemActionEvent;
import org.jutils.core.ui.event.ItemActionListener;
import org.jutils.core.ui.model.IView;
import org.jutils.duak.DuakConstants;
import org.jutils.duak.DuakIcons;
import org.jutils.duak.DuakOptions;
import org.jutils.duak.data.FileInfo;
import org.jutils.duak.task.DuakTask;
import org.jutils.duak.utils.HistoryList;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DuakFrame implements IView<JFrame>
{
    /**  */
    private StandardFrameView frameView;
    /**  */
    private RecentFilesViews recentFiles;
    /**  */
    private JButton previousButton;
    /**  */
    private JButton nextButton;

    /**  */
    private final DuakPanel duakPanel;
    /**  */
    private final HistoryList<FileInfo> history;
    /**  */
    private final OptionsSerializer<DuakOptions> options;

    /***************************************************************************
     * 
     **************************************************************************/
    public DuakFrame()
    {
        this.frameView = new StandardFrameView();
        this.recentFiles = new RecentFilesViews();
        this.history = new HistoryList<FileInfo>();
        this.duakPanel = new DuakPanel();
        this.options = DuakConstants.getOptions();

        duakPanel.addFolderOpenedListener( new FolderOpenedListener() );

        JFrame frame = frameView.getView();

        frame.setSize( 600, 500 );
        frame.setIconImages( DuakIcons.getAppImages() );
        frame.setTitle( "Disk Usage Analysis Kit" );
        frame.setDropTarget( new FileDropTarget( new FileDropped( this ) ) );

        createMenubar( frameView.getMenuBar(), frameView.getFileMenu() );
        frameView.setToolbar( createToolbar() );
        frameView.setContent( duakPanel.getView() );

        recentFiles.setData( options.getOptions().recentDirs.toList() );
        recentFiles.setListeners( ( f, c ) -> open( f ) );
    }

    /***************************************************************************
     * @param menuBar
     * @param fileMenu
     **************************************************************************/
    private void createMenubar( JMenuBar menuBar, JMenu fileMenu )
    {
        createFileMenu( fileMenu );

        // menuBar.add( Box.createHorizontalGlue() );

        menuBar.add( createHelpMenu() );
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
    private JMenu createHelpMenu()
    {
        JMenu menu = new JMenu( "Help" );
        JMenuItem item;

        item = new JMenuItem( "About" );
        item.addActionListener( ( e ) -> showAbout() );
        menu.add( item );

        return menu;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showAbout()
    {
        BuildInfo info = JUtilsInfo.load();
        BuildInfoView.show( getView(), "About Duak", info );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void open( File file )
    {
        options.getOptions().recentDirs.push( file );
        options.write();

        recentFiles.setData( options.getOptions().recentDirs.toList() );

        DuakTask task = new DuakTask( file );

        TaskView.startAndShow( getView(), task, "Analyzing Files" );

        FileInfo results = task.getResults();

        if( results != null )
        {
            SwingUtilities.invokeLater( new PanelUpdater( this, results ) );
        }
    }

    /***************************************************************************
     * @param results
     **************************************************************************/
    private void setResults( FileInfo results )
    {
        duakPanel.setData( results );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        JButton button;
        DirectoryChooserListener openListener;

        openListener = new DirectoryChooserListener( getView(),
            "Choose Directory", new OpenListener( this ) );
        recentFiles.install( toolbar, openListener );

        button = new JButton(
            IconConstants.getIcon( IconConstants.NAV_PREVIOUS_16 ) );
        button.setFocusable( false );
        button.addActionListener( new BackListener() );
        button.setEnabled( false );
        toolbar.add( button );
        previousButton = button;

        button = new JButton(
            IconConstants.getIcon( IconConstants.NAV_NEXT_16 ) );
        button.setFocusable( false );
        button.addActionListener( new ForwardListener() );
        button.setEnabled( false );
        toolbar.add( button );
        nextButton = button;

        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class FolderOpenedListener implements ItemActionListener<FileInfo>
    {
        @Override
        public void actionPerformed( ItemActionEvent<FileInfo> event )
        {
            FileInfo result = event.getItem();
            history.add( result );
            setResults( result );
            refreshButtons();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void refreshButtons()
    {
        previousButton.setEnabled( history.hasPrevious() );
        nextButton.setEnabled( history.hasNext() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class BackListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( history.hasPrevious() )
            {
                setResults( history.previous() );
                refreshButtons();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ForwardListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( history.hasNext() )
            {
                setResults( history.next() );
                refreshButtons();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenListener implements ILastFile, IFileSelected
    {
        private final DuakFrame view;

        public OpenListener( DuakFrame view )
        {
            this.view = view;
        }

        @Override
        public void fileChosen( File file )
        {
            view.open( file );
        }

        @Override
        public File getLastFile()
        {
            return view.options.getOptions().recentDirs.first();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PanelUpdater implements Runnable
    {
        private final FileInfo results;
        private final DuakFrame view;

        public PanelUpdater( DuakFrame view, FileInfo results )
        {
            this.view = view;
            this.results = results;
        }

        @Override
        public void run()
        {
            view.history.clear();
            view.history.add( results );
            view.setResults( results );
            view.refreshButtons();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileDropped
        implements ItemActionListener<IFileDropEvent>
    {
        private final DuakFrame view;

        public FileDropped( DuakFrame view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            view.open( event.getItem().getFiles().get( 0 ) );
        }
    }
}
