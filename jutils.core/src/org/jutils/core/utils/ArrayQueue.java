package org.jutils.core.utils;

import java.util.Iterator;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ArrayQueue<T> implements Iterable<T>
{
    /**  */
    protected Object [] items;
    /**  */
    protected int start;
    /**  */
    protected int end;
    /**  */
    protected int size;

    /***************************************************************************
     * 
     **************************************************************************/
    public ArrayQueue()
    {
        this( 1000 );
    }

    /***************************************************************************
     * @param maxSize
     **************************************************************************/
    public ArrayQueue( int maxSize )
    {
        this.items = new Object[maxSize];
        this.start = 0;
        this.end = 0;
        this.size = 0;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isFull()
    {
        return items.length == size;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int size()
    {
        return size;
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    public void push( T item )
    {
        items[end++] = item;
        if( isFull() )
        {
            start++;
        }
        else
        {
            // do not increase the capacity, just overwrite the old one
            size++;
        }

        if( end >= items.length )
        {
            end = 0;
        }

        if( start >= items.length )
        {
            start = 0;
        }

        // LogUtils.printDebug( "Pushed: start (%d), end(%d), size (%d)", start,
        // end, size );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T peek()
    {
        if( size < 1 )
        {
            throw new ArrayIndexOutOfBoundsException( "No items in the queue" );
        }

        @SuppressWarnings( "unchecked")
        T item = ( T )items[start];

        return item;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T pop()
    {
        if( size < 1 )
        {
            throw new ArrayIndexOutOfBoundsException( "No items in the queue" );
        }

        @SuppressWarnings( "unchecked")
        T item = ( T )items[start++];
        size--;

        if( start >= items.length )
        {
            start = 0;
        }

        return item;
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public T get( int index )
    {
        int qidx = toInternalIndex( index );

        @SuppressWarnings( "unchecked")
        T item = ( T )items[qidx];

        return item;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return new ArrayQueueIterator<>( this );
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    protected int toInternalIndex( int index )
    {
        int size = size();
        int idx = -1;

        if( index < size )
        {
            idx = start + index;

            if( idx >= items.length )
            {
                idx = idx - items.length;
            }

            if( idx < 0 || idx >= items.length )
            {
                String msg = String.format(
                    "List Index (%d) => Queue index (%d) of length (%d) with start (%d) and end (%d)",
                    index, idx, items.length, start, end );
                throw new RuntimeException( msg );
            }
        }
        else
        {
            throw new ArrayIndexOutOfBoundsException(
                "Unable to access element " + index + " from array of size " +
                    size );
        }

        return idx;
    }

    private static final class ArrayQueueIterator<T> implements Iterator<T>
    {
        private final ArrayQueue<T> queue;

        private int index;

        public ArrayQueueIterator( ArrayQueue<T> queue )
        {
            this.queue = queue;
            this.index = 0;
        }

        @Override
        public boolean hasNext()
        {
            return index < queue.size();
        }

        @Override
        public T next()
        {
            T item = queue.get( index );

            index++;

            return item;
        }
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        int count = 10;
        ArrayQueue<Integer> queue = new ArrayQueue<>( count );

        for( int i = 0; i < ( 2 * count ); i++ )
        {
            queue.push( 1 );

            for( int j = 0; j < queue.size(); j++ )
            {
                queue.get( j );
            }
        }
    }
}
