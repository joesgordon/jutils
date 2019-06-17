package org.jutils.ui;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import org.jutils.IconConstants;
import org.jutils.ui.SplitButtonView.IListItemModel;
import org.jutils.ui.event.FileDropTarget;
import org.jutils.ui.event.IRecentListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RecentFilesViews
{
    /**  */
    private final RecentFilesMenuView menuView;
    /**  */
    private final SplitButtonView<File> buttonView;

    /**  */
    private IRecentListener<File> recentListener;

    /***************************************************************************
     * 
     **************************************************************************/
    public RecentFilesViews()
    {
        this( 20 );
    }

    /***************************************************************************
     * @param maxFileCount
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
        private final FileSystemView fsv = FileSystemView.getFileSystemView();

        @Override
        public String getName( File file )
        {
            return file.getName();
        }

        @Override
        public String getTooltip( File file )
        {
            return file.getAbsolutePath();
        }

        @Override
        public Icon getIcon( File file )
        {
            return fsv.getSystemIcon( file );
        }
    }
}
