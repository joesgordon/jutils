package jutils.core.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.Icon;
import javax.swing.tree.TreeNode;

import jutils.core.io.LogUtils;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class DataNode<T> implements TreeNode
{
    /**  */
    protected final T data;
    /**  */
    protected final INodeCreator<T> childCreator;
    /**  */
    protected final Supplier<IDataView<T>> viewCreator;
    /**  */
    protected final Supplier<Icon> iconCreator;
    /**  */
    protected final String name;
    /**  */
    protected final DataNode<?> parent;
    /**  */
    protected final List<DataNode<?>> children;

    /***************************************************************************
     * @param data
     * @param childCreator
     * @param viewCreator
     * @param iconCreator
     * @param parent
     * @param name
     **************************************************************************/
    public DataNode( T data, INodeCreator<T> childCreator,
        Supplier<IDataView<T>> viewCreator, Supplier<Icon> iconCreator,
        DataNode<?> parent, String name )
    {
        this.data = data;
        this.childCreator = childCreator;
        this.viewCreator = viewCreator;
        this.iconCreator = iconCreator;
        this.name = name;
        this.parent = parent;
        this.children = new ArrayList<>();

        childCreator.createChildren( data, children, this );

        LogUtils.printDebug( "%s has %d children", toString(),
            children.size() );
    }

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
        return name == null ? data.toString() : name;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getData()
    {
        return data;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IDataView<T> createView()
    {
        return viewCreator.get();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface INodeCreator<T>
    {
        /**
         * @param data
         * @param parent
         * @param children
         */
        public void createChildren( T data, List<DataNode<?>> children,
            DataNode<?> parent );
    }
}
