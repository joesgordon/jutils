package org.jutils.utils;

import java.util.*;

/*******************************************************************************
 * Stack containing no more than a user-specified number of unique items.
 * Iterates from the latest to earliest entries.
 ******************************************************************************/
public class UniqueMaxStack<T> implements Iterable<T>
{
    /** The default maximum number of unique items. */
    public static final int DEFAULT_MAX_SIZE = 20;

    /**  */
    private final LinkedList<T> stack;
    /**  */
    private final int maxSize;

    /***************************************************************************
     * Creates the stack with the default maximum number of unique items.
     **************************************************************************/
    public UniqueMaxStack()
    {
        this( DEFAULT_MAX_SIZE );
    }

    /***************************************************************************
     * Creates the stack with the specified maximum number of unique items.
     * @param maxSize The maximum number of unique items.
     **************************************************************************/
    public UniqueMaxStack( int maxSize )
    {
        this.stack = new LinkedList<T>();
        this.maxSize = maxSize;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        stack.clear();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T first()
    {
        return isEmpty() ? null : stack.getFirst();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isEmpty()
    {
        return stack.isEmpty();
    }

    /***************************************************************************
     * @param element
     **************************************************************************/
    public void push( T element )
    {
        int index = stack.indexOf( element );

        if( index > -1 )
        {
            stack.remove( index );
        }

        while( stack.size() >= maxSize )
        {
            stack.removeLast();
        }

        stack.addFirst( element );
    }

    public void pushAll( UniqueMaxStack<T> stack )
    {
        for( T item : stack )
        {
            push( item );
        }
    }

    /***************************************************************************
     * @param o
     * @return
     **************************************************************************/
    public boolean remove( Object o )
    {
        return stack.remove( o );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int size()
    {
        return stack.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return stack.iterator();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<T> toList()
    {
        return new ArrayList<>( stack );
    }
}
