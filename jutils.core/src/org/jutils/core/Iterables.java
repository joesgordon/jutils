package org.jutils.core;

import java.util.Iterator;
import java.util.function.Function;

/*******************************************************************************
 *
 ******************************************************************************/
public class Iterables
{
    // TODO Add comments to this file.

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private Iterables()
    {
    }

    /***************************************************************************
     * @param a
     * @return
     **************************************************************************/
    public static Iterable<Byte> buildIteratable( byte [] a )
    {
        return buildIteratable( a, 0, a.length );
    }

    /***************************************************************************
     * @param a
     * @param index
     * @param count
     * @return
     **************************************************************************/
    public static Iterable<Byte> buildIteratable( byte [] a, int index,
        int count )
    {
        return buildIterable( ( i ) -> a[i], index, count );
    }

    /***************************************************************************
     * @param a
     * @return
     **************************************************************************/
    public static Iterable<Short> buildIteratable( short [] a )
    {
        return buildIteratable( a, 0, a.length );
    }

    /***************************************************************************
     * @param a
     * @param index
     * @param count
     * @return
     **************************************************************************/
    public static Iterable<Short> buildIteratable( short [] a, int index,
        int count )
    {
        return buildIterable( ( i ) -> a[i], index, count );
    }

    /***************************************************************************
     * @param a
     * @return
     **************************************************************************/
    public static Iterable<Integer> buildIteratable( int [] a )
    {
        return buildIteratable( a, 0, a.length );
    }

    /***************************************************************************
     * @param a
     * @param index
     * @param count
     * @return
     **************************************************************************/
    public static Iterable<Integer> buildIteratable( int [] a, int index,
        int count )
    {
        return buildIterable( ( i ) -> a[i], index, count );
    }

    /***************************************************************************
     * @param a
     * @return
     **************************************************************************/
    public static Iterable<Long> buildIteratable( long [] a )
    {
        return buildIteratable( a, 0, a.length );
    }

    /***************************************************************************
     * @param a
     * @param index
     * @param count
     * @return
     **************************************************************************/
    public static Iterable<Long> buildIteratable( long [] a, int index,
        int count )
    {
        return buildIterable( ( i ) -> a[i], index, count );
    }

    /***************************************************************************
     * @param a
     * @return
     **************************************************************************/
    public static Iterable<Float> buildIteratable( float [] a )
    {
        return buildIteratable( a, 0, a.length );
    }

    /***************************************************************************
     * @param a
     * @param index
     * @param count
     * @return
     **************************************************************************/
    public static Iterable<Float> buildIteratable( float [] a, int index,
        int count )
    {
        return buildIterable( ( i ) -> a[i], index, count );
    }

    /***************************************************************************
     * @param a
     * @return
     **************************************************************************/
    public static Iterable<Double> buildIteratable( double [] a )
    {
        return buildIteratable( a, 0, a.length );
    }

    /***************************************************************************
     * @param a
     * @param index
     * @param count
     * @return
     **************************************************************************/
    public static Iterable<Double> buildIteratable( double [] a, int index,
        int count )
    {
        return buildIterable( ( i ) -> a[i], index, count );
    }

    /***************************************************************************
     * @param <T>
     * @param a
     * @return
     **************************************************************************/
    public static <T> Iterable<T> buildIteratable( T [] a )
    {
        return buildIteratable( a, 0, a.length );
    }

    /***************************************************************************
     * @param a
     * @param index
     * @param count
     * @return
     **************************************************************************/
    public static <T> Iterable<T> buildIteratable( T [] a, int index,
        int count )
    {
        return buildIterable( ( i ) -> a[i], index, count );
    }

    /***************************************************************************
     * @param <T>
     * @param indexer
     * @param index
     * @param count
     * @return
     **************************************************************************/
    private static <T> Iterable<T> buildIterable( Function<Integer, T> indexer,
        int index, int count )
    {
        return new Iteratorable<>( buildIterator( indexer, index, count ) );
    }

    /***************************************************************************
     * @param <T>
     * @param indexer
     * @param index
     * @param count
     * @return
     **************************************************************************/
    private static <T> Iterator<T> buildIterator( Function<Integer, T> indexer,
        int index, int count )
    {
        return new IndexableIterator<>( indexer, index, count );
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static class IndexableIterator<T> implements Iterator<T>
    {
        /**  */
        private final Function<Integer, T> indexer;
        /**  */
        private final int end;

        /**  */
        private int index;

        /**
         * @param indexer
         * @param index
         * @param count
         */
        public IndexableIterator( Function<Integer, T> indexer, int index,
            int count )
        {
            this.indexer = indexer;
            this.end = index + count;

            this.index = index;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public T next()
        {
            T item = indexer.apply( index );
            index++;
            return item;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext()
        {
            return index < end;
        }
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static class Iteratorable<T> implements Iterable<T>
    {
        /**  */
        private Iterator<T> it;

        /**
         * @param it
         */
        public Iteratorable( Iterator<T> it )
        {
            this.it = it;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Iterator<T> iterator()
        {
            return it;
        }
    }
}
