package jutils.core.utils;

import java.util.Enumeration;
import java.util.Iterator;

/*******************************************************************************
 * Enumerates items from an iterator.
 * @param <T> the type of items to be enumerated.
 ******************************************************************************/
public class IteratorEnumerationAdapter<T> implements Enumeration<T>
{
    /** The iterator to be enumerated. */
    private Iterator<T> it;

    /***************************************************************************
     * Creates a new enumerator seeded with the provided iterator.
     * @param it the iterator to be enumerated.
     **************************************************************************/
    public IteratorEnumerationAdapter( Iterator<T> it )
    {
        this.it = it;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean hasMoreElements()
    {
        return it.hasNext();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T nextElement()
    {
        return it.next();
    }
}
