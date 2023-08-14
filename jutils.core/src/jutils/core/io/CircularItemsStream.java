package jutils.core.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jutils.core.utils.CircularQueue;

/***************************************************************************
 * @param <T>
 **************************************************************************/
public class CircularItemsStream<T> implements IItemStream<T>
{
    /**  */
    private final CircularQueue<T> queue;

    /***************************************************************************
     * @param count
     **************************************************************************/
    public CircularItemsStream( int count )
    {
        this( new CircularQueue<>( count ) );
    }

    /***************************************************************************
     * @param queue
     **************************************************************************/
    public CircularItemsStream( CircularQueue<T> queue )
    {
        this.queue = queue;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getCount()
    {
        return queue.size();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<T> get( long index, int count )
    {
        List<T> items = new ArrayList<>();
        int max = ( int )( index + count );

        for( int i = count; i < max; i++ )
        {
            items.add( queue.get( i ) );
        }

        return items;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void add( T item )
    {
        queue.push( item );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeAll()
    {
        queue.removeAll();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return queue.iterator();
    }
}
