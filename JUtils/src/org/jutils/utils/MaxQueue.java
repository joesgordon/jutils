package org.jutils.utils;

import java.util.*;

/*******************************************************************************
 * Defines a queue that contain zero to the provided maximum number of unique
 * elements.
 * @param <T> The type of element in this queue.
 ******************************************************************************/
public class MaxQueue<T> implements Iterable<T>
{
    /** The collection elements in this queue. */
    private final ArrayDeque<T> elements;
    /** The maximum number of elements in this queue. */
    private final int maxCount;

    /***************************************************************************
     * Creates a new queue to contain up to the provided number of unique
     * elements.
     * @param maxElementCount the maximum number of unique elements in this
     * queue.
     **************************************************************************/
    public MaxQueue( int maxElementCount )
    {
        this.maxCount = maxElementCount;
        this.elements = new ArrayDeque<T>( maxElementCount );
    }

    /***************************************************************************
     * Creates a copy of the provided queue.
     * @param queue the queue to be copied.
     **************************************************************************/
    public MaxQueue( MaxQueue<T> queue )
    {
        this.maxCount = queue.maxCount;
        this.elements = new ArrayDeque<>( queue.elements );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return elements.iterator();
    }

    /***************************************************************************
     * Returns the number of elements in this queue.
     * @return the number of elements in this queue.
     **************************************************************************/
    public int size()
    {
        return elements.size();
    }

    /***************************************************************************
     * Returns {@code true} if this queue contains no elements.
     * @return {@code true} if this queue contains no elements.
     **************************************************************************/
    public boolean isEmpty()
    {
        return elements.isEmpty();
    }

    /***************************************************************************
     * Removes a single instance of the specified element from this queue. If
     * the queue does not contain the element, it is unchanged. More formally,
     * removes the first element {@code e} such that {@code o.equals(e)} (if
     * such an element exists). Returns {@code true} if this queue contained the
     * specified element (or equivalently, if this queue changed as a result of
     * the call).
     * @param o the element to be removed from this queue, if present.
     * @return {@code true} if this queue contained the specified element
     **************************************************************************/
    public boolean remove( Object o )
    {
        return elements.remove( o );
    }

    /***************************************************************************
     * Removes all of the elements from this queue. The queue will be empty
     * after this call returns.
     **************************************************************************/
    public void clear()
    {
        elements.clear();
    }

    /***************************************************************************
     * Adds the provided element to the beginning of the queue if it is not
     * already added, otherwise it is moved to the beginning.
     * @param e the element to be added.
     * @return {@code true}, if the queue was modified, {@code false} otherwise.
     **************************************************************************/
    public boolean push( T e )
    {
        boolean changed = false;

        if( elements.contains( e ) )
        {
            elements.remove( e );
            elements.addFirst( e );
        }
        else
        {
            elements.addFirst( e );

            if( size() > maxCount )
            {
                elements.removeLast();
            }
        }

        return changed;
    }

    /***************************************************************************
     * Adds the provided items to this queue. If the size of the provided list
     * is larger than the maximum size of this queue, only the first items up to
     * max size will be added.
     * @param items the items to be added to this queue.
     **************************************************************************/
    public void addAll( Iterable<T> items )
    {
        List<T> buf = new ArrayList<T>( elements );

        elements.clear();

        for( T t : items )
        {
            elements.add( t );
        }

        elements.addAll( buf );

        while( elements.size() > maxCount )
        {
            elements.removeLast();
        }
    }

    /***************************************************************************
     * Returns the first item in the queue.
     * @return the first item in the queue or {@code null} if empty.
     **************************************************************************/
    public T first()
    {
        return elements.isEmpty() ? null : elements.getFirst();
    }

    /***************************************************************************
     * Returns the last item in the queue.
     * @return the last item in the queue, or {@code null} if empty.
     **************************************************************************/
    public T last()
    {
        return elements.isEmpty() ? null : elements.getLast();
    }

    /***************************************************************************
     * Creates a new list populated with the items in this queue.
     * @return the new list of items in the queue.
     **************************************************************************/
    public List<T> toList()
    {
        List<T> items = new ArrayList<>();

        for( T item : this )
        {
            items.add( item );
        }

        return items;
    }
}
