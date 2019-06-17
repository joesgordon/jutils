package org.jutils.utils;

import java.io.File;
import java.util.*;

import org.jutils.utils.FileDataCache.CacheItem;

/*******************************************************************************
 * Defines a class that stores objects in association with a file.
 * @param <T> the type of objects to be stored.
 ******************************************************************************/
public class FileDataCache<T> implements Iterable<CacheItem<T>>
{
    /**  */
    private final IDataUtils<T> dataUtils;
    /**  */
    private final MaxQueue<CacheItem<T>> cache;

    /***************************************************************************
     * @param dataUtils
     * @param maxCount
     **************************************************************************/
    public FileDataCache( IDataUtils<T> dataUtils, int maxCount )
    {
        this.dataUtils = dataUtils;
        this.cache = new MaxQueue<>( maxCount );
    }

    /***************************************************************************
     * Finds the user data indexed by the provided file.
     * @param f
     * @return
     **************************************************************************/
    public T getData( File f )
    {
        CacheItem<T> ci = null;

        ci = find( f.length() );

        if( ci == null )
        {
            ci = find( f.getAbsoluteFile() );

            if( ci == null )
            {
                T t = null;

                ci = cache.last();

                if( ci == null )
                {
                    t = dataUtils.createDefault();
                }
                else
                {
                    t = dataUtils.copy( ci.item );
                }

                ci = createData( f, t );

                cache.push( ci );
            }
        }

        return ci.item;
    }

    /***************************************************************************
     * @param f
     * @param item
     **************************************************************************/
    public void putData( File f, T item )
    {
        CacheItem<T> ci = createData( f, item );

        cache.push( ci );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<CacheItem<T>> getData()
    {
        List<CacheItem<T>> items = new ArrayList<>( cache.size() );

        for( CacheItem<T> item : cache )
        {
            items.add( item );
        }

        return items;
    }

    /***************************************************************************
     * @param optionsData
     **************************************************************************/
    public void setData( List<CacheItem<T>> data )
    {
        cache.clear();

        cache.addAll( data );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<CacheItem<T>> iterator()
    {
        return cache.iterator();
    }

    /***************************************************************************
     * @param f
     * @param item
     * @return
     **************************************************************************/
    private CacheItem<T> createData( File f, T item )
    {
        CacheItem<T> ci = new CacheItem<>();

        ci.item = item;
        ci.path = f.getAbsoluteFile();
        ci.fileLength = f.length();

        return ci;
    }

    /***************************************************************************
     * @param f
     * @return
     **************************************************************************/
    private CacheItem<T> find( File f )
    {
        for( CacheItem<T> ci : cache )
        {
            if( f.equals( ci.path ) )
            {
                return ci;
            }
        }

        return null;
    }

    /***************************************************************************
     * @param length
     * @return
     **************************************************************************/
    private CacheItem<T> find( long length )
    {
        for( CacheItem<T> ci : cache )
        {
            if( length == ci.fileLength )
            {
                return ci;
            }
        }

        return null;
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static class CacheItem<T>
    {
        public long fileLength;
        public File path;
        public T item;

        @Override
        public int hashCode()
        {
            return path.hashCode();
        }

        /***********************************************************************
         * 
         **********************************************************************/
        @Override
        public boolean equals( Object obj )
        {
            if( obj == null )
            {
                return false;
            }
            else if( this == obj )
            {
                return true;
            }
            else if( obj instanceof CacheItem )
            {
                CacheItem<?> hit = ( CacheItem<?> )obj;

                return equals( hit );
            }

            return false;
        }

        /***********************************************************************
         * @param hit
         * @return
         **********************************************************************/
        public boolean equals( CacheItem<?> item )
        {
            return path.equals( item.path );
        }
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static interface IDataUtils<T>
    {
        T copy( T item );

        T createDefault();
    }
}
