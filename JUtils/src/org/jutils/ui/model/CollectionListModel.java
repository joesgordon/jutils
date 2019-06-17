package org.jutils.ui.model;

import java.util.*;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

/*******************************************************************************
 * Provides a {@link ListModel} with Generics.
 ******************************************************************************/
public class CollectionListModel<T> extends AbstractListModel<T>
    implements Iterable<T>
{
    /**  */
    private static final long serialVersionUID = 3561040955359760505L;

    /** The items contained within the list. */
    private List<T> items;

    /***************************************************************************
     * Creates a new empty model.
     **************************************************************************/
    public CollectionListModel()
    {
        items = new ArrayList<T>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getSize()
    {
        return items.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T getElementAt( int index )
    {
        return items.get( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return items.iterator();
    }

    /***************************************************************************
     * Adds all the provided items to the list.
     * @param items the items to be added.
     **************************************************************************/
    public void addAll( List<T> items )
    {
        int from = this.items.size();

        this.items.addAll( items );

        fireContentsChanged( this, from, this.items.size() - 1 );
    }

    /***************************************************************************
     * Sets the list items to those provided.
     * @param items the items to compose the list.
     **************************************************************************/
    public void setData( List<T> items )
    {
        int maxIndex = Math.max( this.items.size(), items.size() ) - 1;

        // fireIntervalRemoved( this, 0, getSize() );

        this.items = items;

        // fireIntervalAdded( this, 0, getSize() );

        fireContentsChanged( this, 0, maxIndex );
    }

    /***************************************************************************
     * Retrieves the item at the provided index.
     * @param index the index of the element to return.
     * @return the item at the provided index.
     **************************************************************************/
    public T get( int index )
    {
        return items.get( index );
    }

    /***************************************************************************
     * Returns a copy of the data in this list.
     * @return a copy of the data in this list.
     **************************************************************************/
    public List<T> getData()
    {
        return items;
    }

    /***************************************************************************
     * Adds an item to this list.
     * @param item the item to be added.
     **************************************************************************/
    public void add( T item )
    {
        int index = getSize();

        items.add( item );

        fireIntervalAdded( this, index, index );
    }

    /***************************************************************************
     * Adds an item to this list at the provided index.
     * @param item the item to be added.
     * @param index the index at which the item will be insterted.
     **************************************************************************/
    public void add( T item, int index )
    {
        items.add( index, item );

        fireIntervalAdded( this, index, index );
    }

    /***************************************************************************
     * Removed the provided item from the list or does nothing if not found.
     * @param item the item to be removed.
     **************************************************************************/
    public void remove( T item )
    {
        int index = items.indexOf( item );

        if( index > -1 )
        {
            remove( index );
        }
    }

    /***************************************************************************
     * Removes the item at the specified index.
     * @param index the index of the element to be removed
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0
     * || index >= getSize())
     **************************************************************************/
    public T remove( int index )
    {
        T t = items.remove( index );

        fireIntervalRemoved( this, index, index );

        return t;
    }

    /***************************************************************************
     * Removes all items from the list.
     **************************************************************************/
    public void clear()
    {
        items.clear();

        fireIntervalRemoved( this, 0, getSize() );
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public void refreshIndex( int index )
    {
        fireContentsChanged( this, index, index );
    }
}
