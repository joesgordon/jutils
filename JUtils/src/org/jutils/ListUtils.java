package org.jutils;

import java.util.*;
import java.util.Map.Entry;

import org.jutils.utils.Tuple2;

/*******************************************************************************
 * Utility class for list static functions.
 ******************************************************************************/
public final class ListUtils
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private ListUtils()
    {
    }

    /***************************************************************************
     * Creates a list from the provided iterable.
     * @param <T> the type of item iterated.
     * @param items the iteration of items to seed the list.
     * @return the list of items.
     **************************************************************************/
    public static <T> List<T> asList( Iterable<T> items )
    {
        ArrayList<T> list = new ArrayList<T>();

        for( T item : items )
        {
            list.add( item );
        }

        return list;
    }

    /***************************************************************************
     * Create a list of items from a type that contains both the key and the
     * value.
     * @param <K> The type of the key in the new map.
     * @param <V> The type of the value in the new map.
     * @param <T> The type of the items that contain keys and values.
     * @param items the items to seed the map.
     * @param kv the key/value accessor.
     * @return the map of key/value pairs.
     **************************************************************************/
    public static <K, V, T> Map<K, V> createMap( List<T> items,
        IKeyValue<K, V, T> kv )
    {
        Map<K, V> map = new HashMap<K, V>();

        for( T item : items )
        {
            K key = kv.getKey( item );

            V value = map.get( key );

            value = kv.getValue( item, value );

            map.put( key, value );
        }

        return map;
    }

    /***************************************************************************
     * Defines methods to access a key and a value of a given object type.
     * @param <K> the key type.
     * @param <V> the value type.
     * @param <T> the type of the object that contains the key/value.
     **************************************************************************/
    public static interface IKeyValue<K, V, T>
    {
        /***********************************************************************
         * Returns the key of the provided item.
         * @param item the item containing a key.
         * @return the key from the item.
         **********************************************************************/
        public K getKey( T item );

        /***********************************************************************
         * Returns the value of the provided item
         * @param item the item containing a value.
         * @param oldValue the
         * @return the value from the item.
         **********************************************************************/
        public V getValue( T item, V oldValue );
    }

    /***************************************************************************
     * Finds the maximum found key in the provided map of key/count values.
     * @param <K> The type of the keys in the map.
     * @param <V> The type of the values in the map.
     * @param map the map of key/counts.
     * @return the 2-tuple containing the key with the highest count.
     **************************************************************************/
    public static <K, V extends Number> Tuple2<K, V> findMaxEntry(
        Map<K, V> map )
    {
        Entry<K, V> max = null;

        for( Entry<K, V> entry : map.entrySet() )
        {
            if( max == null ||
                entry.getValue().longValue() > max.getValue().longValue() )
            {
                max = entry;
            }
        }

        return max == null ? null
            : new Tuple2<K, V>( max.getKey(), max.getValue() );
    }
}
