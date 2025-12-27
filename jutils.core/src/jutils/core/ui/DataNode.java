package jutils.core.ui;

import java.util.List;
import java.util.function.Supplier;

import javax.swing.Icon;

import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class DataNode<T> extends AbstractDataNode<T>
{
    /**  */
    protected final INodeCreator<T> childCreator;
    /**  */
    protected final Supplier<IDataView<T>> viewCreator;
    /**  */
    protected final Supplier<Icon> iconCreator;
    /**  */
    protected final String name;

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
        super( parent, data );

        this.childCreator = childCreator;
        this.viewCreator = viewCreator;
        this.iconCreator = iconCreator;
        this.name = name;

        createChildren();

        // LogUtils.printDebug( "%s has %d children", toString(),
        // children.size() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void createChildren()
    {
        childCreator.createChildren( data, children, this );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon()
    {
        return iconCreator.get();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IDataView<T> createView()
    {
        return viewCreator.get();
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static interface INodeCreator<T>
    {
        /**
         * @param data
         * @param parent
         * @param children
         */
        public void createChildren( T data,
            List<? extends AbstractDataNode<?>> children,
            AbstractDataNode<?> parent );
    }
}
