package org.jutils.ui.event;

import java.util.LinkedList;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ItemActionList<T>
{
    /**  */
    private LinkedList<ItemActionListener<T>> listeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public ItemActionList()
    {
        listeners = new LinkedList<>();
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addListener( ItemActionListener<T> l )
    {
        listeners.addFirst( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void removeListener( ItemActionListener<T> l )
    {
        listeners.remove( l );
    }

    /***************************************************************************
     * @param source
     * @param item
     **************************************************************************/
    public void fireListeners( Object source, T item )
    {
        ItemActionEvent<T> evt = new ItemActionEvent<T>( source, item );

        for( ItemActionListener<T> l : listeners )
        {
            l.actionPerformed( evt );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int size()
    {
        return listeners.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void removeAll()
    {
        listeners.clear();
    }
}
