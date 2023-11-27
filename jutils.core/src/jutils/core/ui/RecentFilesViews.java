package jutils.core.ui;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileSystemView;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.ui.SplitButtonView.IListItemModel;
import jutils.core.ui.event.FileDropTarget;
import jutils.core.ui.event.IRecentListener;

/*******************************************************************************
 * Manages a {@link JMenu} and a {@link SplitButtonView} that displays a list of
 * files.
 ******************************************************************************/
public class RecentFilesViews
{
    /** The default maximum number of files to display, 20. */
    public static final int DEFAULT_MAX_FILE_COUNT = 20;

    /** The menu to display the list of files. */
    private final RecentFilesMenuView menuView;
    /** The split button to display the list of files. */
    private final SplitButtonView<File> buttonView;

    /**
     * The client listener invoked when a file is chosen from either the menu or
     * the split button.
     */
    private IRecentListener<File> recentListener;

    /***************************************************************************
     * Creates new views with the {@link #DEFAULT_MAX_FILE_COUNT}.
     **************************************************************************/
    public RecentFilesViews()
    {
        this( 20 );
    }

    /***************************************************************************
     * Creates new views with the provided maximum number of files to display.
     * @param maxFileCount the maximum number of files to display.
     **************************************************************************/
    public RecentFilesViews( int maxFileCount )
    {
        this.menuView = new RecentFilesMenuView( maxFileCount );
        this.buttonView = new SplitButtonView<>( null,
            IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ),
            new ArrayList<>(), new FileListItemModel() );
        this.recentListener = null;

        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );

        menuView.getView().setIcon( icon );
        buttonView.setIcon( icon );

        menuView.addSelectedListener( ( f, c ) -> callSelected( f, c ) );
        buttonView.addItemSelected( ( f, c ) -> callSelected( f, c ) );

        buttonView.setDropTarget( new FileDropTarget(
            ( e ) -> callSelected( e.getItem().getFiles().get( 0 ), false ) ) );

        FileContextMenu menu = new FileContextMenu( buttonView.getView() );

        buttonView.addRightClickListener( ( f, c, x, y ) -> {
            c = SwingUtils.getComponentsWindow( c );
            buttonView.hidePopup();
            menu.show( f, c, x, y );
        } );
    }

    /***************************************************************************
     * @param f
     * @param c
     **************************************************************************/
    private void callSelected( File f, boolean c )
    {
        if( recentListener != null )
        {
            recentListener.selected( f, c );
        }
    }

    /***************************************************************************
     * @param irs
     **************************************************************************/
    public void setListeners( IRecentListener<File> irs )
    {
        this.recentListener = irs;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JMenu getMenu()
    {
        return menuView.getView();
    }

    /***************************************************************************
     * @param files
     **************************************************************************/
    public void setData( List<File> files )
    {
        List<File> recentFiles = new ArrayList<>( files.size() );

        for( File f : files )
        {
            if( f.exists() )
            {
                recentFiles.add( f );
            }
        }

        menuView.setData( recentFiles );
        buttonView.setData( recentFiles );
    }

    /***************************************************************************
     * @param toolbar
     * @param listener
     **************************************************************************/
    public void install( JToolBar toolbar, ActionListener listener )
    {
        buttonView.install( toolbar );

        buttonView.addButtonListener( listener );
    }

    /***************************************************************************
     * @param icon
     **************************************************************************/
    public void setIcon( Icon icon )
    {
        menuView.setIcon( icon );
        buttonView.setIcon( icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FileListItemModel implements IListItemModel<File>
    {
        /**  */
        private final FileSystemView fsv = FileSystemView.getFileSystemView();

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName( File file )
        {
            return file.getName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTooltip( File file )
        {
            return file.getAbsolutePath();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Icon getIcon( File file )
        {
            return fsv.getSystemIcon( file );
        }
    }
}
