package org.jutils.core;

import java.util.Collection;

import org.jutils.core.io.IStringWriter;
import org.jutils.core.ui.hex.HexUtils;

/*******************************************************************************
 *
 ******************************************************************************/
public class ArrayPrinter
{
    // TODO Add comments to this file.

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private ArrayPrinter()
    {
    }

    /***************************************************************************
     * @param <T>
     * @param array
     * @param index
     * @param count
     * @return
     **************************************************************************/
    public static <T> String toString( Iterable<T> array, int count )
    {
        return toString( array, count, ", " );
    }

    /***************************************************************************
     * @param <T>
     * @param array
     * @param index
     * @param count
     * @param delimiter
     * @return
     **************************************************************************/
    private static <T> String toString( Iterable<T> array, int count,
        String delimiter )
    {
        return toString( array, count, delimiter, ( t ) -> t.toString() );
    }

    /***************************************************************************
     * @param array
     * @param <T>
     * @param a
     * @param delimiter
     * @param count
     * @param writer
     * @return
     **************************************************************************/
    public static <T> String toString( Iterable<T> array, int count,
        String delimiter, IStringWriter<T> writer )
    {
        StringBuilder buf = new StringBuilder();

        int i = 0;
        for( T t : array )
        {
            if( i > 0 )
            {
                buf.append( delimiter );
            }

            buf.append( writer.toString( t ) );

            i++;
        }

        return buf.toString();
    }

    /***************************************************************************
     * Writes the decimal value for each integer in the array and places a comma
     * and a space between each.
     * @param a the array to convert to a string.
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( byte [] a )
    {
        return toString( a, ", " );
    }

    /***************************************************************************
     * @param a the array to convert to a string.
     * @param delimiter
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( byte [] a, String delimiter )
    {
        return toString( a, delimiter, 0, a.length );
    }

    /***************************************************************************
     * @param a the array to convert to a string.
     * @param delimiter
     * @param index
     * @param count
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( byte [] a, String delimiter, int index,
        int count )
    {
        return toString( Iterables.buildIteratable( a, index, count ), count,
            delimiter, ( t ) -> HexUtils.getHexString( t ) );
    }

    /***************************************************************************
     * Writes the decimal value for each integer in the array and places a comma
     * and a space between each.
     * @param a the array to convert to a string.
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( short [] a )
    {
        return toString( a, ", " );
    }

    /***************************************************************************
     * @param a the array to convert to a string.
     * @param delimiter
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( short [] a, String delimiter )
    {
        return toString( a, delimiter, 0, a.length );
    }

    /***************************************************************************
     * @param a the array to convert to a string.
     * @param delimiter
     * @param index
     * @param count
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( short [] a, String delimiter, int index,
        int count )
    {
        return toString( Iterables.buildIteratable( a, index, count ), count,
            delimiter );
    }

    /***************************************************************************
     * Writes the decimal value for each integer in the array and places a comma
     * and a space between each.
     * @param a the array to convert to a string.
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( int [] a )
    {
        return toString( a, ", " );
    }

    /***************************************************************************
     * @param a
     * @param delimiter
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( int [] a, String delimiter )
    {
        return toString( a, delimiter, 0, a.length );
    }

    /***************************************************************************
     * @param a
     * @param delimiter
     * @param index
     * @param count
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( int [] a, String delimiter, int index,
        int count )
    {
        return toString( Iterables.buildIteratable( a, index, count ), count,
            delimiter );
    }

    /***************************************************************************
     * Writes the decimal value for each integer in the array and places a comma
     * and a space between each.
     * @param a the array to convert to a string.
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( long [] a )
    {
        return toString( a, ", " );
    }

    /***************************************************************************
     * @param a the array to convert to a string.
     * @param delimiter
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( long [] a, String delimiter )
    {
        return toString( a, delimiter, 0, a.length );
    }

    /***************************************************************************
     * @param a the array to convert to a string.
     * @param delimiter
     * @param index
     * @param count
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( long [] a, String delimiter, int index,
        int count )
    {
        return toString( Iterables.buildIteratable( a, index, count ), count,
            delimiter );
    }

    /***************************************************************************
     * Writes the value for each double in the array and places a comma and a
     * space between each.
     * @param a the array to convert to a string.
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( float [] a )
    {
        return toString( a, ", " );
    }

    /***************************************************************************
     * @param a the array to convert to a string.
     * @param delimiter
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( float [] a, String delimiter )
    {
        return toString( a, delimiter, 0, a.length );
    }

    /***************************************************************************
     * @param a the array to convert to a string.
     * @param index
     * @param count
     * @param delimiter
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( float [] a, String delimiter, int index,
        int count )
    {
        return toString( Iterables.buildIteratable( a, index, count ), count,
            delimiter );
    }

    /***************************************************************************
     * @param a the array to convert to a string.
     * @param delimiter
     * @param index
     * @param count
     * @param width
     * @param precision
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( float [] a, String delimiter, int index,
        int count, int width, int precision )
    {
        String fmt = Utils.buildDoubleFormat( width, precision );

        return toString( Iterables.buildIteratable( a, index, count ), count,
            delimiter, ( t ) -> String.format( fmt, t ) );
    }

    /***************************************************************************
     * Writes the value for each double in the array and places a comma and a
     * space between each.
     * @param a the array to convert to a string.
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( double [] a )
    {
        return toString( a, ", " );
    }

    /***************************************************************************
     * @param a the array to convert to a string.
     * @param delimiter
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( double [] a, String delimiter )
    {
        return toString( a, delimiter, 0, a.length );
    }

    /***************************************************************************
     * @param a the array to convert to a string.
     * @param index
     * @param count
     * @param delimiter
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( double [] a, String delimiter, int index,
        int count )
    {
        return toString( Iterables.buildIteratable( a, index, count ), count,
            delimiter );
    }

    /***************************************************************************
     * @param a the array to convert to a string.
     * @param delimiter
     * @param index
     * @param count
     * @param width
     * @param precision
     * @return the comma separated array of items.
     **************************************************************************/
    public static String toString( double [] a, String delimiter, int index,
        int count, int width, int precision )
    {
        String fmt = Utils.buildDoubleFormat( width, precision );

        return toString( Iterables.buildIteratable( a, index, count ), count,
            delimiter, ( t ) -> String.format( fmt, t ) );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the array and places a
     * comma between each.
     * @param items the items to be concatenated.
     * @return the string of items.
     **************************************************************************/
    public static String argsToString( Object... items )
    {
        return argsToStringDelim( ", ", items );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the array and places
     * the provided delimiter between each.
     * @param delimiter the characters to place between the items.
     * @param items the items to be concatenated.
     * @return the string of items.
     **************************************************************************/
    public static String argsToStringDelim( String delimiter, Object... items )
    {
        return toString( items, delimiter );
    }

    /***************************************************************************
     * @param <T>
     * @param items
     * @return
     **************************************************************************/
    public static <T> String toString( T [] items )
    {
        return toString( items, ", " );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the array and places
     * the provided delimiter between each.
     * @param <T>
     * @param items the objects to convert to a string.
     * @param delimiter the characters to place between the items.
     * @return the string of items.
     **************************************************************************/
    public static <T> String toString( T [] items, String delimiter )
    {
        return toString( items, delimiter, ( t ) -> t.toString() );
    }

    /***************************************************************************
     * @param <T>
     * @param items the objects to convert to a string.
     * @param delimiter the characters to place between the items.
     * @param writer
     * @return the string of items.
     **************************************************************************/
    public static <T> String toString( T [] items, String delimiter,
        IStringWriter<T> writer )
    {
        return toString( items, 0, items.length, delimiter, writer );
    }

    /***************************************************************************
     * @param <T>
     * @param items the objects to convert to a string.
     * @param index
     * @param count
     * @param delimiter the characters to place between the items.
     * @param writer
     * @return the string of items.
     **************************************************************************/
    public static <T> String toString( T [] items, int index, int count,
        String delimiter, IStringWriter<T> writer )
    {
        StringBuilder buf = new StringBuilder();
        for( int i = 0; i < count; i++ )
        {
            if( i > 0 )
            {
                buf.append( delimiter );
            }
            buf.append( writer.toString( items[i + index] ) );
        }

        return buf.toString();
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the collection and
     * places a comma between each.
     * @param <T>
     * @param items the collection to convert to a string.
     * @return the comma separated list of items.
     **************************************************************************/
    public static <T> String toString( Collection<T> items )
    {
        return toString( items, ", " );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the collection and
     * places the provided delimiter between each.
     * @param <T>
     * @param items the items to be concatenated.
     * @param delimiter the characters to place between the items.
     * @return the string of items.
     **************************************************************************/
    public static <T> String toString( Collection<T> items, String delimiter )
    {
        return toString( items, items.size(), delimiter );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the collection and
     * places the provided delimiter between each.
     * @param <T>
     * @param items the items to be concatenated.
     * @param delimiter the characters to place between the items.
     * @param writer
     * @return the string of items.
     **************************************************************************/
    public static <T> String toString( Collection<T> items, String delimiter,
        IStringWriter<T> writer )
    {
        return toString( items, items.size(), delimiter, writer );
    }
}
