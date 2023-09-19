package jutils.core;

import java.util.Collection;

import jutils.core.io.IStringWriter;
import jutils.core.ui.hex.HexUtils;

/*******************************************************************************
 * Defines utility methods for printing arrays, collections, lists, iterables.
 ******************************************************************************/
public class ArrayPrinter
{
    /** A comma followed by a space, {@code ", "}. */
    public static final String COMMA_DELIMITER = ", ";
    /**
     * The default delimiter used by these utility functions. Take care when
     * setting as this will have application-level scope since these functions
     * are static. Set to {@link #COMMA_DELIMITER} by default.
     */
    public static String DEFAULT_DELIMITER = COMMA_DELIMITER;

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private ArrayPrinter()
    {
    }

    // -------------------------------------------------------------------------
    // Iterables
    // -------------------------------------------------------------------------

    /***************************************************************************
     * Prints {@code count} number of items which are accessed through the
     * provided iterable using each item's {@link Object#toString()} separated
     * by a comma.
     * @param <T> the type of items to be printed.
     * @param items the items to be printed.
     * @param count the number of items to print.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static <T> String toString( Iterable<T> items, int count )
    {
        return toString( items, count, DEFAULT_DELIMITER );
    }

    /***************************************************************************
     * Prints {@code count} number of items which are accessed through the
     * provided iterable using each item's {@link Object#toString()} separated
     * by {@code delimiter}.
     * @param <T> the type of items to be printed.
     * @param items the items to be printed.
     * @param count the number of items to print.
     * @param delimiter the string between each item.
     * @return the printed string of the provided items.
     **************************************************************************/
    private static <T> String toString( Iterable<T> items, int count,
        String delimiter )
    {
        return toString( items, count, delimiter, ( t ) -> t.toString() );
    }

    /***************************************************************************
     * @param <T> the type of items to be printed.
     * @param items the items to be printed.
     * @param count the number of items to print.
     * @param delimiter the string between each item.
     * @param writer the method with which each item is written.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static <T> String toString( Iterable<T> items, int count,
        String delimiter, IStringWriter<T> writer )
    {
        StringBuilder buf = new StringBuilder();

        int i = 0;
        for( T t : items )
        {
            if( i > 0 )
            {
                buf.append( delimiter );
            }

            buf.append( writer.toString( t ) );

            i++;

            if( i >= count )
            {
                break;
            }
        }

        return buf.toString();
    }

    // -------------------------------------------------------------------------
    // byte arrays
    // -------------------------------------------------------------------------

    /***************************************************************************
     * Writes the decimal value for each integer in the array and places a comma
     * and a space between each.
     * @param items the items to be printed.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( byte [] items )
    {
        return toString( items, DEFAULT_DELIMITER );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( byte [] items, String delimiter )
    {
        return toString( items, delimiter, 0, items.length );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @param index the beginning index to start printing items.
     * @param count the number of items to print.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( byte [] items, String delimiter, int index,
        int count )
    {
        return toString( Iterables.buildIteratable( items, index, count ),
            count, delimiter, ( t ) -> HexUtils.getHexString( t ) );
    }

    // -------------------------------------------------------------------------
    // short arrays
    // -------------------------------------------------------------------------

    /***************************************************************************
     * Writes the decimal value for each integer in the array and places a comma
     * and a space between each.
     * @param items the items to be printed.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( short [] items )
    {
        return toString( items, DEFAULT_DELIMITER );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( short [] items, String delimiter )
    {
        return toString( items, delimiter, 0, items.length );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @param index the beginning index to start printing items.
     * @param count the number of items to print.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( short [] items, String delimiter, int index,
        int count )
    {
        return toString( Iterables.buildIteratable( items, index, count ),
            count, delimiter );
    }

    // -------------------------------------------------------------------------
    // int arrays
    // -------------------------------------------------------------------------

    /***************************************************************************
     * Writes the decimal value for each integer in the array and places a comma
     * and a space between each.
     * @param items the items to be printed.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( int [] items )
    {
        return toString( items, DEFAULT_DELIMITER );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( int [] items, String delimiter )
    {
        return toString( items, delimiter, 0, items.length );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @param index the beginning index to start printing items.
     * @param count the number of items to print.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( int [] items, String delimiter, int index,
        int count )
    {
        return toString( Iterables.buildIteratable( items, index, count ),
            count, delimiter );
    }

    // -------------------------------------------------------------------------
    // long arrays
    // -------------------------------------------------------------------------

    /***************************************************************************
     * Writes the decimal value for each integer in the array and places a comma
     * and a space between each.
     * @param items the items to be printed.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( long [] items )
    {
        return toString( items, DEFAULT_DELIMITER );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( long [] items, String delimiter )
    {
        return toString( items, delimiter, 0, items.length );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @param index the beginning index to start printing items.
     * @param count the number of items to print.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( long [] items, String delimiter, int index,
        int count )
    {
        return toString( Iterables.buildIteratable( items, index, count ),
            count, delimiter );
    }

    // -------------------------------------------------------------------------
    // float arrays
    // -------------------------------------------------------------------------

    /***************************************************************************
     * Writes the value for each double in the array and places a comma and a
     * space between each.
     * @param items the items to be printed.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( float [] items )
    {
        return toString( items, DEFAULT_DELIMITER );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( float [] items, String delimiter )
    {
        return toString( items, delimiter, 0, items.length );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param index the beginning index to start printing items.
     * @param count the number of items to print.
     * @param delimiter the string between each item.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( float [] items, String delimiter, int index,
        int count )
    {
        return toString( Iterables.buildIteratable( items, index, count ),
            count, delimiter );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @param index the beginning index to start printing items.
     * @param count the number of items to print.
     * @param width the width of the field to write each float to.
     * @param precision the number of decimal places for each float.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( float [] items, String delimiter, int index,
        int count, int width, int precision )
    {
        String fmt = Utils.buildDoubleFormat( width, precision );

        return toString( Iterables.buildIteratable( items, index, count ),
            count, delimiter, ( t ) -> String.format( fmt, t ) );
    }

    // -------------------------------------------------------------------------
    // double arrays
    // -------------------------------------------------------------------------

    /***************************************************************************
     * Writes the value for each double in the array and places a comma and a
     * space between each.
     * @param items the items to be printed.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( double [] items )
    {
        return toString( items, DEFAULT_DELIMITER );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( double [] items, String delimiter )
    {
        return toString( items, delimiter, 0, items.length );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param index the beginning index to start printing items.
     * @param count the number of items to print.
     * @param delimiter the string between each item.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( double [] items, String delimiter, int index,
        int count )
    {
        return toString( Iterables.buildIteratable( items, index, count ),
            count, delimiter );
    }

    /***************************************************************************
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @param index the beginning index to start printing items.
     * @param count the number of items to print.
     * @param width the width of the field to write each float to.
     * @param precision the number of decimal places for each float.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String toString( double [] items, String delimiter, int index,
        int count, int width, int precision )
    {
        String fmt = Utils.buildDoubleFormat( width, precision );

        return toString( Iterables.buildIteratable( items, index, count ),
            count, delimiter, ( t ) -> String.format( fmt, t ) );
    }

    // -------------------------------------------------------------------------
    // Variable arguments
    // -------------------------------------------------------------------------

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the array and places a
     * comma between each.
     * @param items the items to be printed.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String argsToString( Object... items )
    {
        return argsToStringDelim( DEFAULT_DELIMITER, items );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the array and places
     * the provided delimiter between each.
     * @param delimiter the string between each item.
     * @param items the items to be printed.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static String argsToStringDelim( String delimiter, Object... items )
    {
        return toString( items, delimiter );
    }

    // -------------------------------------------------------------------------
    // Generic arrays
    // -------------------------------------------------------------------------

    /***************************************************************************
     * @param <T> the type of items to be printed.
     * @param items the items to be printed.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static <T> String toString( T [] items )
    {
        return toString( items, DEFAULT_DELIMITER );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the array and places
     * the provided delimiter between each.
     * @param <T> the type of items to be printed.
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static <T> String toString( T [] items, String delimiter )
    {
        return toString( items, delimiter, ( t ) -> t.toString() );
    }

    /***************************************************************************
     * @param <T> the type of items to be printed.
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @param writer the method with which each item is written.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static <T> String toString( T [] items, String delimiter,
        IStringWriter<T> writer )
    {
        return toString( items, 0, items.length, delimiter, writer );
    }

    /***************************************************************************
     * @param <T> the type of items to be printed.
     * @param items the items to be printed.
     * @param index the beginning index to start printing items.
     * @param count the number of items to print.
     * @param delimiter the string between each item.
     * @param writer the method with which each item is written.
     * @return the printed string of the provided items.
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

    // -------------------------------------------------------------------------
    // Collections
    // -------------------------------------------------------------------------

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the collection and
     * places a comma between each.
     * @param <T> the type of items to be printed.
     * @param items the items to be printed.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static <T> String toString( Collection<T> items )
    {
        return toString( items, DEFAULT_DELIMITER );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the collection and
     * places the provided delimiter between each.
     * @param <T> the type of items to be printed.
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static <T> String toString( Collection<T> items, String delimiter )
    {
        return toString( items, items.size(), delimiter );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the collection and
     * places the provided delimiter between each.
     * @param <T> the type of items to be printed.
     * @param items the items to be printed.
     * @param delimiter the string between each item.
     * @param writer the method with which each item is written.
     * @return the printed string of the provided items.
     **************************************************************************/
    public static <T> String toString( Collection<T> items, String delimiter,
        IStringWriter<T> writer )
    {
        return toString( items, items.size(), delimiter, writer );
    }
}
