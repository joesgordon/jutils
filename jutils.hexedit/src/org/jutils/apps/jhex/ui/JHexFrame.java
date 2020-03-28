package org.jutils.apps.jhex.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import org.jutils.apps.jhex.JHexIcons;
import org.jutils.apps.jhex.JHexMain;
import org.jutils.apps.jhex.data.JHexOptions;
import org.jutils.core.IconConstants;
import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.RecentFilesViews;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.FileChooserListener;
import org.jutils.core.ui.event.FileDropTarget;
import org.jutils.core.ui.event.ItemActionEvent;
import org.jutils.core.ui.event.ItemActionListener;
import org.jutils.core.ui.event.FileChooserListener.IFileSelected;
import org.jutils.core.ui.event.FileChooserListener.ILastFile;
import org.jutils.core.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.core.ui.fields.ComboFormField;
import org.jutils.core.ui.hex.HexBufferSize;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 * Represents the view that builds and contains the main frame for the
 * application.
 ******************************************************************************/
public class JHexFrame implements IView<JFrame>
{
    // -------------------------------------------------------------------------
    // Main panel widgets
    // -------------------------------------------------------------------------
    /**  */
    private final StandardFrameView frameView;
    /** The file tree displaying the directories in the given file system. */
    private final HexFileView editor;
    /** The serializer to access user options. */
    private final OptionsSerializer<JHexOptions> options;
    /** The recent files menu. */
    private final RecentFilesViews recentFiles;

    /** Index of the currently selected buffer size. */
    private HexBufferSize bufferSize;

    /***************************************************************************
     * Creates a new frame that closes the file when the frame closes.
     **************************************************************************/
    public JHexFrame()
    {
        this( true );
    }

    /***************************************************************************
     * Creates a new frame that closes the file when the frame closes according
     * to the provided parameter.
     * @param closeFileWithFrame closes the file when the frame is closed if
     * {@code true}.
     **************************************************************************/
    public JHexFrame( boolean closeFileWithFrame )
    {
        this.options = JHexMain.getOptions();

        this.frameView = new StandardFrameView();
        this.editor = new HexFileView();
        this.recentFiles = new RecentFilesViews();

        this.bufferSize = HexBufferSize.LARGE;

        // ---------------------------------------------------------------------
        // Setup frame
        // ---------------------------------------------------------------------
        createMenuBar( frameView.getMenuBar(), frameView.getFileMenu() );

        frameView.setToolbar( createToolbar() );
        frameView.setContent( editor.getView() );
        frameView.setSize( 1000, 600 );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        JFrame frame = frameView.getView();
        if( closeFileWithFrame )
        {
            frame.addWindowListener( new WindowCloseListener( this ) );
        }
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setTitle( "JHex" );

        frame.setIconImages( JHexIcons.getAppImages() );

        recentFiles.setListeners( ( f, c ) -> openFile( f ) );
        editor.getView().setDropTarget(
            new FileDropTarget( new FileDroppedListener( this ) ) );
    }

    /***************************************************************************
     * Creates the toolbar.
     * @return the toolbar created.
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        recentFiles.install( toolbar, createOpenListener() );

        // SwingUtils.addActionToToolbar( toolbar,
        // new ActionAdapter( ( e ) -> saveFile(), "Save File",
        // IconConstants.getIcon( IconConstants.SAVE_16 ) ) );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar,
            new ActionAdapter( ( e ) -> showBufferSizeDialog(),
                "Configure Options",
                IconConstants.getIcon( IconConstants.CONFIG_16 ) ) );

        return toolbar;
    }

    /***************************************************************************
     * Creates a listener that opens a file.
     * @return the listener that opens a file.
     **************************************************************************/
    private FileChooserListener createOpenListener()
    {
        IFileSelected ifs = ( f ) -> openFile( f );
        ILastFile ilf = () -> options.getOptions().lastAccessedFiles.first();
        FileChooserListener fcl = new FileChooserListener( getView(),
            "Open File", false, ifs, ilf );

        return fcl;
    }

    /***************************************************************************
     * Adds the menu components to the provided bar and file menu.
     * @param menubar the menu bar to add menus to.
     * @param fileMenu the file menu of the menu bar.
     **************************************************************************/
    private void createMenuBar( JMenuBar menubar, JMenu fileMenu )
    {
        createFileMenu( fileMenu );
        menubar.add( fileMenu );
        menubar.add( createNavMenu() );
        menubar.add( createToolsMenu() );

        updateFileMenu();
    }

    /***************************************************************************
     * Adds the application menu items to the provided file menu.
     * @param fileMenu the file menu to add menu items to.
     **************************************************************************/
    private void createFileMenu( JMenu fileMenu )
    {
        JMenuItem item;
        int idx = 0;

        item = new JMenuItem( "Open" );
        item.addActionListener( createOpenListener() );
        item.setIcon( IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        fileMenu.add( item, idx++ );

        item = new JMenuItem( "Save" );
        item.addActionListener( ( e ) -> saveFile() );
        item.setIcon( IconConstants.getIcon( IconConstants.SAVE_16 ) );
        // fileMenu.add( item, idx++ );

        fileMenu.add( recentFiles.getMenu(), idx++ );

        fileMenu.add( new JSeparator(), idx++ );
    }

    /***************************************************************************
     * Replaces the recently opened files in the file menu with those in the
     * user data.
     **************************************************************************/
    private void updateFileMenu()
    {
        JHexOptions options = this.options.getOptions();

        recentFiles.setData( options.lastAccessedFiles.toList() );
    }

    /***************************************************************************
     * Creates the search menu.
     * @return the application's search menu.
     **************************************************************************/
    private JMenu createNavMenu()
    {
        JMenu menu = new JMenu( "Navigate" );

        menu.add( editor.gotoAction );
        menu.add( editor.searchAction );

        menu.addSeparator();

        menu.add( editor.firstAction );
        menu.add( editor.prevAction );
        menu.add( editor.nextAction );
        menu.add( editor.lastAction );

        return menu;
    }

    /***************************************************************************
     * Creates the tools menu.
     * @return the applications tools menu.
     **************************************************************************/
    private JMenu createToolsMenu()
    {
        JMenu menu = new JMenu( "Tools" );
        JMenuItem item;

        item = new JMenuItem( "Set Buffer Size" );
        item.setIcon( IconConstants.getIcon( IconConstants.CONFIG_16 ) );
        item.addActionListener( ( e ) -> showBufferSizeDialog() );
        menu.add( item );

        return menu;
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
     * Displays the dialog that allows the user to change the size of the
     * buffer.
     **************************************************************************/
    private void showBufferSizeDialog()
    {
        ComboFormField<HexBufferSize> sizeField = new ComboFormField<HexBufferSize>(
            "Buffer Size", HexBufferSize.values() );

        sizeField.setValue( bufferSize );

        HexBufferSize size = OptionUtils.showQuestionField( getView(),
            "Choose buffer size:", "Buffer Size", sizeField );

        if( size != null )
        {
            bufferSize = size;
            editor.setBufferSize( bufferSize.size );
        }
    }

    /***************************************************************************
     * Opens the provided file and displays its content.
     * @param f the file to be opened.
     **************************************************************************/
    public void openFile( File f )
    {
        if( !f.isFile() )
        {
            return;
        }

        JHexOptions options = this.options.getOptions();

        options.lastAccessedFiles.push( f );
        this.options.write();

        editor.setData( f );

        updateFileMenu();
    }

    /***************************************************************************
     * Saves the file.
     **************************************************************************/
    private void saveFile()
    {
        OptionUtils.showErrorMessage( getView(),
            "Sorry, this functionality is not yet implemented.",
            "Not Yet Implemented" );

        if( "".length() < 1 )
        {
            return;
        }

        JFileChooser chooser = new JFileChooser();
        int choice = JFileChooser.CANCEL_OPTION;
        JHexOptions options = this.options.getOptions();

        chooser.setSelectedFile( options.getLastFile() );
        choice = chooser.showSaveDialog( getView() );

        if( choice == JFileChooser.APPROVE_OPTION )
        {
            File f = chooser.getSelectedFile();

            options.lastAccessedFiles.push( f );
            this.options.write();

            try
            {
                editor.saveFile( f );
            }
            catch( IOException ex )
            {
                ex.printStackTrace();
            }

            updateFileMenu();
        }
    }

    /***************************************************************************
     * Closes the currently opened file.
     **************************************************************************/
    public void closeFile()
    {
        try
        {
            editor.closeFile();
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                "I/O Error" );
        }
    }

    /***************************************************************************
     * Listener to open a file that is drag and dropped onto the table.
     **************************************************************************/
    private static class FileDroppedListener
        implements ItemActionListener<IFileDropEvent>
    {
        /** The view to open the dropped file. */
        private final JHexFrame view;

        /**
         * Creates a new listener for a dropped file.
         * @param view the view to open the dropped file.
         */
        public FileDroppedListener( JHexFrame view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            IFileDropEvent dropEvent = event.getItem();
            List<File> files = dropEvent.getFiles();

            if( !files.isEmpty() )
            {
                view.openFile( files.get( 0 ) );
            }
        }
    }

    /***************************************************************************
     * Window listener to close the file.
     **************************************************************************/
    private static class WindowCloseListener extends WindowAdapter
    {
        /**
         * The frame that contains the resource to be closed when the frame
         * closes.
         */
        private final JHexFrame frame;

        /**
         * Creates a new window listener that closes the resources of the
         * provided frame when closed.
         * @param frame the frame that contains the resource to be closed when
         * the frame closes.
         */
        public WindowCloseListener( JHexFrame frame )
        {
            this.frame = frame;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void windowClosing( WindowEvent e )
        {
            // LogUtils.printDebug( "Window Closing" );
            frame.closeFile();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void windowClosed( WindowEvent e )
        {
            // LogUtils.printDebug( "Window Closed" );
            frame.closeFile();
        }
    }
}
