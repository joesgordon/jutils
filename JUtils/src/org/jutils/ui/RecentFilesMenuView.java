package org.jutils.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import org.jutils.ui.event.IRecentListener;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RecentFilesMenuView implements IDataView<List<File>>
{
    /**  */
    private final JMenu menu;
    /**  */
    private final List<IRecentListener<File>> selectedListeners;
    /**  */
    private final int maxFileCount;

    /**  */
    private List<File> files;

    /***************************************************************************
     * 
     **************************************************************************/
    public RecentFilesMenuView()
    {
        this( 10 );
    }

    /***************************************************************************
     * @param maxFileCount
     **************************************************************************/
    public RecentFilesMenuView( int maxFileCount )
    {
        this.maxFileCount = maxFileCount;
        this.menu = new JMenu( "Recent Files" );
        this.selectedListeners = new ArrayList<>();
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addSelectedListener( IRecentListener<File> l )
    {
        selectedListeners.add( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JMenu getView()
    {
        return menu;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<File> getData()
    {
        return files;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( List<File> files )
    {
        JMenuItem item;

        this.files = new ArrayList<>();

        menu.removeAll();

        FileSystemView view = FileSystemView.getFileSystemView();

        for( int i = 0; i < files.size(); i++ )
        {
            File file = files.get( i );

            if( file.exists() )
            {
                item = new JMenuItem(
                    ( this.files.size() + 1 ) + " " + file.getName() );
                item.setIcon( view.getSystemIcon( file ) );
                item.addActionListener( new ItemSelected( this, file ) );
                item.setToolTipText( file.getAbsolutePath() );
                item.setMnemonic( item.getText().charAt( 0 ) );
                menu.add( item );
                this.files.add( file );

                if( this.files.size() > maxFileCount )
                {
                    break;
                }
            }
        }
    }

    /***************************************************************************
     * @param file
     * @param ctrlPressed
     **************************************************************************/
    private void fireListeners( File file, boolean ctrlPressed )
    {
        for( IRecentListener<File> irs : selectedListeners )
        {
            irs.selected( file, ctrlPressed );
        }
    }

    /***************************************************************************
     * @param icon
     **************************************************************************/
    public void setIcon( Icon icon )
    {
        menu.setIcon( icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ItemSelected implements ActionListener
    {
        private final RecentFilesMenuView view;
        private final File file;

        public ItemSelected( RecentFilesMenuView view, File file )
        {
            this.view = view;
            this.file = file;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            int modifiers = e.getModifiers();
            boolean ctrlPressed = ( ActionEvent.CTRL_MASK &
                modifiers ) == ActionEvent.CTRL_MASK;

            // LogUtils.printDebug( "Ctrl pressed: " + ctrlPressed );

            view.fireListeners( file, ctrlPressed );
        }
    }
}
