package jutils.core.ui.event;

/*******************************************************************************
 * @param <T> Type of the item on which an action will be performed.
 ******************************************************************************/
public class ItemActionEvent<T>
{
    /**  */
    private Object source;
    /**  */
    private T item;

    /***************************************************************************
     * @param source
     * @param item
     **************************************************************************/
    public ItemActionEvent( Object source, T item )
    {
        this.source = source;
        this.item = item;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getItem()
    {
        return item;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Object getSource()
    {
        return source;
    }
}
