package jutils.core.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.Icon;
import javax.swing.tree.TreeNode;

import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * Defines a {@link TreeNode} that contains a data of the specified type.
 * Implementing classes must call {@link #createChildren()} within their
 * constructor..
 * @param <T> the type of data stored in this node.
 ******************************************************************************/
public abstract class AbstractDataNode<T> implements TreeNode
{
    /** The parent of this node. */
    protected final AbstractDataNode<?> parent;
    /** The data stored in this node. */
    protected final T data;
    /** The children of this node. */
    protected final List<AbstractDataNode<?>> children;

    /***************************************************************************
     * @param parent the parent of this node.
     * @param data the data stored in this node.
     **************************************************************************/
    public AbstractDataNode( AbstractDataNode<?> parent, T data )
    {
        this.parent = parent;
        this.data = data;
        this.children = new ArrayList<>();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public abstract String getName();

    /***************************************************************************
     * 
     **************************************************************************/
    public abstract void createChildren();

    /***************************************************************************
     * @return
     **************************************************************************/
    public abstract Icon getIcon();

    /***************************************************************************
     * @return
     **************************************************************************/
    public abstract IDataView<T> createView();

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public TreeNode getChildAt( int childIndex )
    {
        return children.get( childIndex );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getChildCount()
    {
        return children.size();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public TreeNode getParent()
    {
        return parent;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getIndex( TreeNode node )
    {
        return children.indexOf( node );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean getAllowsChildren()
    {
        return true;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isLeaf()
    {
        return children.isEmpty();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Enumeration<? extends TreeNode> children()
    {
        return Collections.enumeration( children );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        String name = getName();
        return name == null ? data.toString() : name;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getData()
    {
        return data;
    }
}
