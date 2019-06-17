package org.jutils.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

import org.jutils.ValidationException;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

/*******************************************************************************
 * Provides random access to a group of constant-size items and serializes all
 * but a certain number of items to the provided stream.
 * @param <T> the type of item in the group.
 ******************************************************************************/
public class CachedList<T> implements List<T>, Closeable
{
    /**  */
    private final int itemSize;
    /** The serializer error reporter for this list. */
    private final IDataSerializer<T> cacher;
    /** The stream to be serialized to/from for this list. */
    private final IDataStream stream;
    /** The current item cache. */
    private final ArrayList<T> cache;

    /** The index of the current cache. */
    private int cachedIndex;
    /** The number of items in the entire list. */
    private int size;
    /** The number of items to store in each cache. */
    private int cacheSize;
    /**
     * {@code true} if the loaded cache needs to be written to disk;
     * {@code false} otherwise.
     */
    private boolean unwritten;
    /** {@code true} if the stream is open; {@code false} otherwise. */
    private boolean open;

    /***************************************************************************
     * @param cacher
     * @param stream
     **************************************************************************/
    public CachedList( int itemSize, IDataSerializer<T> cacher,
        IDataStream stream )
    {
        this( itemSize, cacher, stream, 8 * 1024 * 1024 / itemSize );
    }

    /***************************************************************************
     * @param cacher
     * @param stream
     * @param cacheSize
     **************************************************************************/
    public CachedList( int itemSize, IDataSerializer<T> cacher,
        IDataStream stream, int cacheSize )
    {
        this.itemSize = itemSize;
        this.cacher = cacher;
        this.stream = stream;
        this.cacheSize = cacheSize;
        this.cache = new ArrayList<>( cacheSize );

        this.cachedIndex = 0;
        this.unwritten = true;
        this.open = true;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T last()
    {
        return isEmpty() ? null : get( size() - 1 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close()
    {
        cache.clear();

        try
        {
            stream.close();
        }
        catch( IOException ex )
        {
            throw new RuntimeException( "Error closing cache file", ex );
        }

        open = false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean add( T item )
    {
        if( !open )
        {
            return false;
        }

        ensureCached( size++ );

        cache.add( item );

        unwritten = true;

        return true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean contains( Object o )
    {
        for( T t : this )
        {
            if( t.equals( o ) )
            {
                return true;
            }
        }

        return false;
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    @Override
    public T get( int index )
    {
        if( !open )
        {
            throw new IllegalStateException(
                "Cannot add items when stream closed" );
        }

        ensureCached( index );

        return cache.get( index - cachedIndex );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return new CacheIterator<>( this );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    @Override
    public int size()
    {
        return size;
    }

    /***************************************************************************
     * Returns a view of the portion of this list between the specified
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
     * @param fromIndex
     * @param toIndex
     * @return
     * @see List#subList(int, int)
     **************************************************************************/
    @Override
    public List<T> subList( int fromIndex, int toIndex )
    {
        int len = toIndex - fromIndex;
        ArrayList<T> list = new ArrayList<>( len );

        for( int i = fromIndex; i < toIndex; i++ )
        {
            list.add( get( i ) );
        }

        return list;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object [] toArray()
    {
        Object [] items = new Object[size()];

        for( int i = 0; i < size(); i++ )
        {
            items[i] = get( i );
        }

        return items;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public <K> K [] toArray( K [] a )
    {
        if( a.length < size )
        {
            for( int i = 0; i < size(); i++ )
            {
                @SuppressWarnings( "unchecked")
                K k = ( K )get( i );
                a[i] = k;
            }
        }
        else
        {
            @SuppressWarnings( "unchecked")
            K [] ks = ( K [] )toArray();
            return ks;
        }
        return a;
    }

    /***************************************************************************
     * @param index
     * @return true if the item can be cached.
     * @throws ArrayIndexOutOfBoundsException if the index < 0.
     **************************************************************************/
    private boolean ensureCached( int index )
    {
        if( index < 0 || index > size )
        {
            return false;
        }

        if( index >= cachedIndex && index < cachedIndex + cacheSize )
        {
            return true;
        }

        return loadCacheSafe( index );
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    private boolean loadCacheSafe( int index )
    {
        try
        {
            // -----------------------------------------------------------------
            // Write cache if necessary.
            // -----------------------------------------------------------------
            if( unwritten )
            {
                // LogUtils.printDebug( "writing @ position %016X, index %d",
                // stream.getPosition(), index );
                writeCache();
                // LogUtils.printDebug( " post-write len: %016X",
                // stream.getLength() );

                unwritten = false;
            }

            // -----------------------------------------------------------------
            // Read cache if necessary
            // -----------------------------------------------------------------
            // LogUtils.printDebug( "reading @ position %016X, index %d",
            // stream.getPosition(), index );
            readCache( index );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            return false;
        }
        catch( ValidationException ex )
        {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    /***************************************************************************
     * @param index
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    private void readCache( int index ) throws IOException, ValidationException
    {
        int cacheIndex = index / cacheSize;

        long position = cacheIndex * cacheSize * itemSize;
        // LogUtils.printDebug( " reading from position: %d", position );

        cache.clear();

        cachedIndex = cacheIndex * cacheSize;

        if( position != stream.getLength() )
        {
            stream.seek( position );

            for( int i = 0; i < cacheSize && stream.getAvailable() > 0; i++ )
            {
                cache.add( cacher.read( stream ) );
            }
        }

        // LogUtils.printDebug( " read: %d from %016X", cache.size(),
        // position );
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    private void writeCache() throws IOException
    {
        for( int i = 0; i < cache.size(); i++ )
        {
            cacher.write( cache.get( i ), stream );
        }

        // LogUtils.printDebug(
        // " wrote " + ( cache.size() * cacher.getItemSize() ) + " bytes" );
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static class CacheIterator<T> implements Iterator<T>
    {
        private final CachedList<T> list;

        private int index;

        public CacheIterator( CachedList<T> list )
        {
            this.list = list;
            this.index = 0;
        }

        @Override
        public boolean hasNext()
        {
            return index < list.size();
        }

        @Override
        public T next()
        {
            if( !hasNext() )
            {
                throw new NoSuchElementException(
                    "No element exists at index " + index );
            }

            return list.get( index++ );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean remove( Object o )
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean containsAll( Collection<?> c )
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean addAll( Collection<? extends T> c )
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean addAll( int index, Collection<? extends T> c )
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean removeAll( Collection<?> c )
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean retainAll( Collection<?> c )
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void clear()
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void add( int index, T element )
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T remove( int index )
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int indexOf( Object o )
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int lastIndexOf( Object o )
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ListIterator<T> listIterator()
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ListIterator<T> listIterator( int index )
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T set( int index, T element )
    {
        throw new UnsupportedOperationException(
            "Functionality is not yet supported" );

        // T existing = null;
        //
        // if( ensureCached( index ) )
        // {
        // existing = cache.set( index - cachedIndex, element );
        // }
        //
        // return existing;
    }
}
