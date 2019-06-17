package org.jutils.ui;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

/*******************************************************************************
 *
 ******************************************************************************/
public class CheckTreeSelectionModel implements TreeSelectionModel
{
    /**  */
    private final TreeModel model;
    /**  */
    private final DefaultTreeSelectionModel selectionModel;

    /***************************************************************************
     * @param model TreeModel
     **************************************************************************/
    public CheckTreeSelectionModel( TreeModel model )
    {
        this.model = model;
        this.selectionModel = new DefaultTreeSelectionModel();

        selectionModel.setSelectionMode(
            TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION );
    }

    /***************************************************************************
     * tests whether there is any unselected node in the subtree of given path
     * @param path TreePath
     * @return boolean
     **************************************************************************/
    public boolean isPartiallySelected( TreePath path )
    {
        if( isPathSelected( path, true ) )
        {
            return false;
        }

        TreePath [] selectionPaths = getSelectionPaths();

        if( selectionPaths == null )
        {
            return false;
        }

        for( int j = 0; j < selectionPaths.length; j++ )
        {
            if( isDescendant( selectionPaths[j], path ) )
            {
                return true;
            }
        }

        return false;
    }

    /***************************************************************************
     * Tells whether given path is selected. if dig is true, then a path is
     * assumed to be selected if one of its ancestors is selected.
     * @param path TreePathd
     * @param dig boolean
     * @return boolean
     **************************************************************************/
    public boolean isPathSelected( TreePath path, boolean dig )
    {
        if( !dig )
        {
            return selectionModel.isPathSelected( path );
        }
        while( path != null && !selectionModel.isPathSelected( path ) )
        {
            path = path.getParentPath();
        }
        return path != null;
    }

    /***************************************************************************
     * is path1 descendant of path2
     * @param path1 TreePath
     * @param path2 TreePath
     * @return boolean
     **************************************************************************/
    private static boolean isDescendant( TreePath path1, TreePath path2 )
    {
        Object obj1[] = path1.getPath();
        Object obj2[] = path2.getPath();
        for( int i = 0; i < obj2.length; i++ )
        {
            if( obj1[i] != obj2[i] )
            {
                return false;
            }
        }
        return true;
    }

    /***************************************************************************
     * @param pPaths TreePath[]
     **************************************************************************/
    @Override
    public void setSelectionPaths( TreePath [] paths )
    {
        this.clearSelection();
        this.addSelectionPaths( paths );
    }

    /***************************************************************************
     * @param paths TreePath[]
     **************************************************************************/
    @Override
    public void addSelectionPaths( TreePath [] paths )
    {
        // unselect all descendants of paths[]
        for( int i = 0; i < paths.length; i++ )
        {
            TreePath path = paths[i];
            TreePath [] selectionPaths = getSelectionPaths();
            if( selectionPaths == null )
            {
                break;
            }
            ArrayList<TreePath> toBeRemoved = new ArrayList<TreePath>();
            for( int j = 0; j < selectionPaths.length; j++ )
            {
                if( isDescendant( selectionPaths[j], path ) )
                {
                    toBeRemoved.add( selectionPaths[j] );
                }
            }
            selectionModel.removeSelectionPaths(
                toBeRemoved.toArray( new TreePath[0] ) );
        }

        // if all siblings are selected then unselect them and select parent
        // recursively otherwize just select that path.
        for( int i = 0; i < paths.length; i++ )
        {
            TreePath path = paths[i];
            TreePath temp = null;
            while( areSiblingsSelected( path ) )
            {
                temp = path;
                if( path.getParentPath() == null )
                {
                    break;
                }
                path = path.getParentPath();
            }
            if( temp != null )
            {
                if( temp.getParentPath() != null )
                {
                    addSelectionPath( temp.getParentPath() );
                }
                else
                {
                    if( !isSelectionEmpty() )
                    {
                        removeSelectionPaths( getSelectionPaths() );
                    }
                    selectionModel.addSelectionPaths( new TreePath[] { temp } );
                }
            }
            else
            {
                selectionModel.addSelectionPaths( new TreePath[] { path } );
            }
        }
    }

    /***************************************************************************
     * tells whether all siblings of given path are selected.
     * @param path TreePath
     * @return boolean
     **************************************************************************/
    private boolean areSiblingsSelected( TreePath path )
    {
        TreePath parent = path.getParentPath();
        if( parent == null )
        {
            return true;
        }
        Object node = path.getLastPathComponent();
        Object parentNode = parent.getLastPathComponent();

        int childCount = model.getChildCount( parentNode );
        for( int i = 0; i < childCount; i++ )
        {
            Object childNode = model.getChild( parentNode, i );
            if( childNode == node )
            {
                continue;
            }
            if( !isPathSelected( parent.pathByAddingChild( childNode ) ) )
            {
                return false;
            }
        }
        return true;
    }

    /***************************************************************************
     * @param paths TreePath[]
     **************************************************************************/
    @Override
    public void removeSelectionPaths( TreePath [] paths )
    {
        for( int i = 0; i < paths.length; i++ )
        {
            TreePath path = paths[i];
            if( path.getPathCount() == 1 )
            {
                selectionModel.removeSelectionPaths( new TreePath[] { path } );
            }
            else
            {
                toggleRemoveSelection( path );
            }
        }
    }

    /***************************************************************************
     * If any ancestor node of given path is selected then unselect it and
     * selection all its descendants except given path and descendants.
     * otherwise just unselect the given path
     * @param path TreePath
     **************************************************************************/
    private void toggleRemoveSelection( TreePath path )
    {
        Stack<TreePath> stack = new Stack<>();
        TreePath parent = path.getParentPath();

        while( parent != null && !isPathSelected( parent ) )
        {
            stack.push( parent );
            parent = parent.getParentPath();
        }

        if( parent != null )
        {
            stack.push( parent );
        }
        else
        {
            selectionModel.removeSelectionPaths( new TreePath[] { path } );
            return;
        }

        while( !stack.isEmpty() )
        {
            TreePath temp = stack.pop();
            TreePath peekPath = stack.isEmpty() ? path
                : ( TreePath )stack.peek();
            Object node = temp.getLastPathComponent();
            Object peekNode = peekPath.getLastPathComponent();
            int childCount = model.getChildCount( node );
            for( int i = 0; i < childCount; i++ )
            {
                Object childNode = model.getChild( node, i );
                if( childNode != peekNode )
                {
                    selectionModel.addSelectionPaths( new TreePath[] {
                        temp.pathByAddingChild( childNode ) } );
                }
            }
        }
        selectionModel.removeSelectionPaths( new TreePath[] { parent } );
        selectionModel.removeSelectionPaths( new TreePath[] { path } );
    }

    @Override
    public void setSelectionMode( int mode )
    {
        selectionModel.setSelectionMode( mode );
    }

    @Override
    public int getSelectionMode()
    {
        return selectionModel.getSelectionMode();
    }

    @Override
    public void setSelectionPath( TreePath path )
    {
        selectionModel.setSelectionPath( path );
    }

    @Override
    public void addSelectionPath( TreePath path )
    {
        selectionModel.addSelectionPath( path );
    }

    @Override
    public void removeSelectionPath( TreePath path )
    {
        toggleRemoveSelection( path );
    }

    @Override
    public TreePath getSelectionPath()
    {
        return selectionModel.getSelectionPath();
    }

    @Override
    public TreePath [] getSelectionPaths()
    {
        return selectionModel.getSelectionPaths();
    }

    @Override
    public int getSelectionCount()
    {
        return selectionModel.getSelectionCount();
    }

    @Override
    public boolean isPathSelected( TreePath path )
    {
        return selectionModel.isPathSelected( path );
    }

    @Override
    public boolean isSelectionEmpty()
    {
        return selectionModel.isSelectionEmpty();
    }

    @Override
    public void clearSelection()
    {
        selectionModel.clearSelection();
    }

    @Override
    public void setRowMapper( RowMapper newMapper )
    {
        selectionModel.setRowMapper( newMapper );
    }

    @Override
    public RowMapper getRowMapper()
    {
        return selectionModel.getRowMapper();
    }

    @Override
    public int [] getSelectionRows()
    {
        return selectionModel.getSelectionRows();
    }

    @Override
    public int getMinSelectionRow()
    {
        return selectionModel.getMinSelectionRow();
    }

    @Override
    public int getMaxSelectionRow()
    {
        return selectionModel.getMaxSelectionRow();
    }

    @Override
    public boolean isRowSelected( int row )
    {
        return selectionModel.isRowSelected( row );
    }

    @Override
    public void resetRowSelection()
    {
        selectionModel.resetRowSelection();
    }

    @Override
    public int getLeadSelectionRow()
    {
        return selectionModel.getLeadSelectionRow();
    }

    @Override
    public TreePath getLeadSelectionPath()
    {
        return selectionModel.getLeadSelectionPath();
    }

    @Override
    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        selectionModel.addPropertyChangeListener( listener );
    }

    @Override
    public void removePropertyChangeListener( PropertyChangeListener listener )
    {
        selectionModel.removePropertyChangeListener( listener );
    }

    @Override
    public void addTreeSelectionListener( TreeSelectionListener x )
    {
        selectionModel.addTreeSelectionListener( x );
    }

    @Override
    public void removeTreeSelectionListener( TreeSelectionListener x )
    {
        selectionModel.removeTreeSelectionListener( x );
    }
}
