package jutils.core.utils;

import java.util.Enumeration;
import java.util.Iterator;

/*******************************************************************************
 * Class that provides a way to iterate through an Enumeration via an iterator
 * (for each) block.
 * @param <T> the type of objects to be iterated.
 ******************************************************************************/
public class EnumerationIteratorAdapter<T> implements Iterator<T>, Iterable<T>
{
    /** The enumeration to be iterated. */
    private final Enumeration<T> it;

    /***************************************************************************
     * Creates the iterator with the given enumeration.
     * @param it the enumeration to be iterated.
     **************************************************************************/
    public EnumerationIteratorAdapter( Enumeration<T> it )
    {
        this.it = it;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean hasNext()
    {
        return it.hasMoreElements();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T next()
    {
        return it.nextElement();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void remove()
    {
        throw new UnsupportedOperationException(
            "Cannot remove from an Enumeration." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return this;
    }
}
