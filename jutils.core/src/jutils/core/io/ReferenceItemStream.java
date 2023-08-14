package jutils.core.io;

import java.io.EOFException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import jutils.core.ValidationException;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ReferenceItemStream<T> implements IItemStream<T>
{
    /**  */
    public final IReferenceStream<T> stream;

    /***************************************************************************
     * @param stream
     **************************************************************************/
    public ReferenceItemStream( IReferenceStream<T> stream )
    {
        this.stream = stream;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getCount()
    {
        return stream.getCount();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<T> get( long index, int count )
    {
        try
        {
            return stream.read( index, count );
        }
        catch( EOFException ex )
        {
            throw new RuntimeException( ex );
        }
        catch( IOException ex )
        {
            throw new RuntimeException( ex );
        }
        catch( ValidationException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void add( T item )
    {
        try
        {
            stream.write( item );
        }
        catch( IOException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return stream.iterator();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeAll()
    {
        try
        {
            stream.removeAll();
        }
        catch( IOException ex )
        {
            throw new RuntimeException( ex );
        }
    }
}
