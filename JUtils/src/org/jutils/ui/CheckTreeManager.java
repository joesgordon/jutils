package org.jutils.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.jutils.io.LogUtils;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.UpdaterList;

/*******************************************************************************
 *
 ******************************************************************************/
public class CheckTreeManager implements TreeSelectionListener
{
    /**  */
    private final JTree tree;
    /**  */
    private final CheckTreeSelectionModel selectionModel;
    /**  */
    private final UpdaterList<TreePath> checkListeners;

    /**  */
    private final int defaultWidth;

    /***************************************************************************
     * @param tree JTree
     **************************************************************************/
    public CheckTreeManager( JTree tree )
    {
        this.tree = tree;
        this.selectionModel = new CheckTreeSelectionModel( tree.getModel() );
        this.checkListeners = new UpdaterList<>();
        this.defaultWidth = new JCheckBox().getPreferredSize().width;

        tree.setCellRenderer( new CheckTreeCellRenderer( tree.getCellRenderer(),
            selectionModel ) );
        tree.addMouseListener( new CheckClickListener( this ) );
        selectionModel.addTreeSelectionListener( this );
    }

    /***************************************************************************
     * @param path
     * @param selected
     **************************************************************************/
    public void setPathSelected( TreePath path, boolean selected )
    {
        boolean isSelected = selectionModel.isPathSelected( path, true );

        if( isSelected != selected )
        {
            selectionModel.removeTreeSelectionListener( this );

            try
            {
                if( selected )
                {
                    selectionModel.addSelectionPaths( new TreePath[] { path } );
                }
                else
                {
                    LogUtils.printDebug( "removing %s",
                        path.getLastPathComponent().toString() );
                    selectionModel.removeSelectionPath( path );
                }
            }
            finally
            {
                selectionModel.addTreeSelectionListener( this );
                tree.treeDidChange();
            }
        }
    }

    /***************************************************************************
     * @return CheckTreeSelectionModel
     **************************************************************************/
    public CheckTreeSelectionModel getSelectionModel()
    {
        return selectionModel;
    }

    /***************************************************************************
     * @param e TreeSelectionEvent
     **************************************************************************/
    @Override
    public void valueChanged( TreeSelectionEvent e )
    {
        tree.treeDidChange();
    }

    /***************************************************************************
     * @param e
     * @return
     **************************************************************************/
    public TreePath getPath( MouseEvent e )
    {
        TreePath path = tree.getPathForLocation( e.getX(), e.getY() );

        if( path == null )
        {
            return null;
        }

        if( e.getX() > tree.getPathBounds( path ).x + defaultWidth )
        {
            return null;
        }

        return path;
    }

    /***************************************************************************
     * Adds a callback to be invoked when a node is check/unchecked. The
     * callback is invoked for only the node checked/unchecked. It is not called
     * for other nodes whose checked state is changed as a result of the
     * original node's check state changing.
     * @param l the callback to be invoked.
     **************************************************************************/
    public void addCheckListener( IUpdater<TreePath> l )
    {
        checkListeners.add( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private final class CheckClickListener extends MouseAdapter
    {
        private CheckTreeManager manager = null;

        private TreePath pressedPath = null;

        public CheckClickListener( CheckTreeManager manager )
        {
            this.manager = manager;
        }

        @Override
        public void mousePressed( MouseEvent e )
        {
            pressedPath = manager.getPath( e );
        }

        @Override
        public void mouseReleased( MouseEvent e )
        {
            TreePath releasedPath = manager.getPath( e );

            if( releasedPath != null && releasedPath.equals( pressedPath ) )
            {
                boolean isSelected = manager.selectionModel.isPathSelected(
                    releasedPath, true );

                manager.setPathSelected( releasedPath, !isSelected );
                manager.checkListeners.fire( releasedPath );
            }

            pressedPath = null;
        }
    }
}
