package org.jutils.core.io;

import java.util.Iterator;

import org.jutils.core.ArrayPrinter;
import org.jutils.core.Iterables;
import org.jutils.core.Iterables.Iteratorable;
import org.jutils.core.Utils;
import org.jutils.core.ui.hex.HexUtils;

/*******************************************************************************
 * Defines the methods to print fields of a structure/section/tier.
 ******************************************************************************/
public class FieldPrinter
{
    /** The stream to print to. */
    public final StringPrintStream stream;
    /** The string to use for each indentation. */
    public final String indentString;
    /** The string to use to indent for this tier. */
    public final String tierString;

    /***************************************************************************
     * 
     **************************************************************************/
    @SuppressWarnings( "resource")
    public FieldPrinter()
    {
        this( new StringPrintStream() );
    }

    /***************************************************************************
     * @param stream
     **************************************************************************/
    public FieldPrinter( StringPrintStream stream )
    {
        this( stream, "    " );
    }

    /***************************************************************************
     * @param stream
     * @param indentString
     **************************************************************************/
    public FieldPrinter( StringPrintStream stream, String indentString )
    {
        this.stream = stream;
        this.indentString = indentString;
        this.tierString = "";
    }

    /***************************************************************************
     * @param stream
     * @param indentString
     * @param tierString
     **************************************************************************/
    private FieldPrinter( StringPrintStream stream, String indentString,
        String tierString )
    {
        this.stream = stream;
        this.indentString = indentString;
        this.tierString = tierString;
    }

    /***************************************************************************
     * @param tierName
     * @return
     **************************************************************************/
    public FieldPrinter createTier( String tierName )
    {
        stream.println( "%s%s:", tierString, tierName );

        return new FieldPrinter( stream, indentString,
            tierString + indentString );
    }

    /***************************************************************************
     * @param tierName
     * @param tier
     **************************************************************************/
    public void printTier( String tierName, ITierPrinter tier )
    {
        FieldPrinter p = createTier( tierName );

        tier.printFields( p );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, String value )
    {
        stream.println( "%s%s: %s", tierString, name, value );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, Object value )
    {
        printField( name, value.toString() );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, boolean value )
    {
        printField( name, value ? "true" : "false" );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, char value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, byte value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, short value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, int value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, long value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     * @param digits
     **************************************************************************/
    public void printField( String name, long value, int digits )
    {
        String fmt = "0x%0" + digits + "X";
        long mask = 0;

        if( digits == 16 )
        {
            mask = -1;
        }
        else if( digits > 0 && digits < 16 )
        {
            mask = ( 1L << ( digits * 4 ) ) - 1;
        }
        else
        {
            throw new IllegalArgumentException(
                "Invalid number of digits: " + digits );
        }

        value = value & mask;

        printField( name, String.format( fmt, value ) );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, float value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     * @param width
     * @param precision
     **************************************************************************/
    public void printField( String name, float value, int width, int precision )
    {
        printField( name, ( double )value, width, precision );
    }

    /***************************************************************************
     * @param name
     * @param value
     **************************************************************************/
    public void printField( String name, double value )
    {
        printField( name, "" + value );
    }

    /***************************************************************************
     * @param name
     * @param value
     * @param width
     * @param precision
     **************************************************************************/
    public void printField( String name, double value, int width,
        int precision )
    {
        String fmt = Utils.buildDoubleFormat( width, precision );
        printField( name, String.format( fmt, value ) );
    }

    /***************************************************************************
     * @param <T>
     * @param name
     * @param array
     * @param count
     **************************************************************************/
    public <T> void printIterableField( String name, Iterable<T> array,
        int count )
    {
        printIterableField( name, array, count, 0 );
    }

    /***************************************************************************
     * @param <T>
     * @param name
     * @param array
     * @param count
     * @param writer
     **************************************************************************/
    public <T> void printIterableField( String name, Iterable<T> array,
        int count, IStringWriter<T> writer )
    {
        printIterableField( name, array, count, 0, writer );
    }

    /***************************************************************************
     * @param <T>
     * @param name
     * @param array
     * @param count
     * @param itemsPerLine
     **************************************************************************/
    public <T> void printIterableField( String name, Iterable<T> array,
        int count, int itemsPerLine )
    {
        printIterableField( name, array, count, itemsPerLine,
            ( t ) -> t.toString() );
    }

    /***************************************************************************
     * @param <T>
     * @param name
     * @param array
     * @param count
     * @param itemsPerLine
     * @param delimiter
     **************************************************************************/
    public <T> void printIterableField( String name, Iterable<T> array,
        int count, int itemsPerLine, String delimiter )
    {
        printIterableField( name, array, count, itemsPerLine, delimiter,
            ( t ) -> t.toString() );
    }

    /***************************************************************************
     * @param <T>
     * @param name
     * @param array
     * @param count
     * @param itemsPerLine
     * @param writer
     **************************************************************************/
    public <T> void printIterableField( String name, Iterable<T> array,
        int count, int itemsPerLine, IStringWriter<T> writer )
    {
        printIterableField( name, array, count, itemsPerLine, ", ", writer );
    }

    /***************************************************************************
     * @param <T>
     * @param name
     * @param array
     * @param count
     * @param itemsPerLine
     * @param delimiter
     * @param writer
     **************************************************************************/
    public <T> void printIterableField( String name, Iterable<T> array,
        int count, int itemsPerLine, String delimiter, IStringWriter<T> writer )
    {
        String cntName = name + " (" + count + ")";
        if( itemsPerLine < 1 || count < itemsPerLine )
        {
            printField( cntName,
                ArrayPrinter.toString( array, count, delimiter, writer ) );
        }
        else
        {
            FieldPrinter s = createTier( cntName );
            String idxFmt = genIndexFormat( count );
            Iterator<T> iterator = array.iterator();

            for( int i = 0; i < count; i += itemsPerLine )
            {
                int remaining = count - i;
                int lineCount = Math.min( itemsPerLine, remaining );
                String lineName = genLineName( name, lineCount, idxFmt, i );
                Iterator<T> subIter = new SubIterator<>( iterator, lineCount );
                Iterable<T> subArray = new Iteratorable<>( subIter );
                String line = ArrayPrinter.toString( subArray, lineCount,
                    delimiter, writer );

                s.printField( lineName, line );
            }
        }
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static final class SubIterator<T> implements Iterator<T>
    {
        /**  */
        private final Iterator<T> array;
        /**  */
        private final int count;

        /**  */
        private int index;

        /**
         * @param array
         * @param count
         */
        public SubIterator( Iterator<T> array, int count )
        {
            this.array = array;
            this.count = count;
            this.index = 0;
        }

        @Override
        public boolean hasNext()
        {
            return index < count;
        }

        @Override
        public T next()
        {
            T item = array.next();
            index++;
            return item;
        }
    }

    /***************************************************************************
     * @param name
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, byte... values )
    {
        printFieldValues( name, 16, values );
    }

    /***************************************************************************
     * @param name
     * @param itemsPerLine
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, int itemsPerLine,
        byte... values )
    {
        Iterable<Byte> array = Iterables.buildIteratable( values );
        printIterableField( name, array, values.length, itemsPerLine, " ",
            ( t ) -> HexUtils.getHexString( t ) );
    }

    /***************************************************************************
     * @param name
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, short... values )
    {
        printFieldValues( name, 0, values );
    }

    /***************************************************************************
     * @param name
     * @param values
     **************************************************************************/
    public void printFieldValuesHex( String name, short... values )
    {
        Iterable<Short> array = Iterables.buildIteratable( values );
        printIterableField( name, array, values.length, 0,
            ( t ) -> HexUtils.getHexString( t ) );
    }

    /***************************************************************************
     * @param name
     * @param itemsPerLine
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, int itemsPerLine,
        short... values )
    {
        Iterable<Short> array = Iterables.buildIteratable( values );
        printIterableField( name, array, values.length, itemsPerLine );
    }

    /***************************************************************************
     * @param name
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, int... values )
    {
        printFieldValues( name, 0, values );
    }

    /***************************************************************************
     * @param name
     * @param values
     **************************************************************************/
    public void printFieldValuesHex( String name, int... values )
    {
        Iterable<Integer> array = Iterables.buildIteratable( values );
        printIterableField( name, array, values.length, 0,
            ( t ) -> HexUtils.getHexString( t ) );
    }

    /***************************************************************************
     * @param name
     * @param itemsPerLine
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, int itemsPerLine, int... values )
    {
        Iterable<Integer> array = Iterables.buildIteratable( values );
        printIterableField( name, array, values.length, itemsPerLine );
    }

    /***************************************************************************
     * @param name
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, long... values )
    {
        printFieldValues( name, 0, values );
    }

    /***************************************************************************
     * @param name
     * @param values
     **************************************************************************/
    public void printFieldValuesHex( String name, long... values )
    {
        Iterable<Long> array = Iterables.buildIteratable( values );
        printIterableField( name, array, values.length, 0,
            ( t ) -> HexUtils.getHexString( t ) );
    }

    /***************************************************************************
     * @param name
     * @param itemsPerLine
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, int itemsPerLine,
        long... values )
    {
        Iterable<Long> array = Iterables.buildIteratable( values );
        printIterableField( name, array, values.length, itemsPerLine );
    }

    /***************************************************************************
     * @param name
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, float... values )
    {
        printFieldValues( name, 0, values );
    }

    /***************************************************************************
     * @param name
     * @param width
     * @param precision
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, int itemsPerLine,
        float... values )
    {
        Iterable<Float> array = Iterables.buildIteratable( values );
        printIterableField( name, array, values.length, itemsPerLine );
    }

    /***************************************************************************
     * @param name
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, int width, int precision,
        float... values )
    {
        printFieldValues( name, 0, width, precision, values );
    }

    /***************************************************************************
     * @param name
     * @param width
     * @param precision
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, int itemsPerLine, int width,
        int precision, float... values )
    {
        Iterable<Float> array = Iterables.buildIteratable( values );
        String fmt = Utils.buildDoubleFormat( width, precision );
        printIterableField( name, array, values.length, itemsPerLine,
            ( t ) -> String.format( fmt, t ) );
    }

    /***************************************************************************
     * @param name
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, double... values )
    {
        printFieldValues( name, 0, values );
    }

    /***************************************************************************
     * @param name
     * @param itemsPerLine
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, int itemsPerLine,
        double... values )
    {
        Iterable<Double> array = Iterables.buildIteratable( values );
        printIterableField( name, array, values.length, itemsPerLine );
    }

    /***************************************************************************
     * @param name
     * @param width
     * @param precision
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, int width, int precision,
        double... values )
    {
        printFieldValues( name, 0, width, precision, values );
    }

    /***************************************************************************
     * @param name
     * @param width
     * @param precision
     * @param values
     **************************************************************************/
    public void printFieldValues( String name, int itemsPerLine, int width,
        int precision, double... values )
    {
        Iterable<Double> array = Iterables.buildIteratable( values );
        String fmt = Utils.buildDoubleFormat( width, precision );
        printIterableField( name, array, values.length, itemsPerLine,
            ( t ) -> String.format( fmt, t ) );
    }

    /***************************************************************************
     * @param <T>
     * @param name
     * @param writer
     * @param values
     **************************************************************************/
    public <T> void printFieldValues( String name, T [] values )
    {
        printFieldValues( name, values, ( d ) -> d.toString(), 0 );
    }

    /***************************************************************************
     * @param <T>
     * @param name
     * @param writer
     * @param values
     **************************************************************************/
    public <T> void printFieldValues( String name, T [] values,
        IStringWriter<T> writer )
    {
        printFieldValues( name, values, writer, 0 );
    }

    /***************************************************************************
     * @param <T>
     * @param name
     * @param writer
     * @param itemsPerLine
     * @param values
     **************************************************************************/
    public <T> void printFieldValues( String name, T [] values,
        IStringWriter<T> writer, int itemsPerLine )
    {
        String cntName = String.format( "%s (%d)", name, values.length );
        if( itemsPerLine < 1 )
        {
            printField( cntName,
                ArrayPrinter.toString( values, ", ", writer ) );
        }
        else
        {
            FieldPrinter s = createTier( cntName );
            String idxFmt = genIndexFormat( values.length );

            for( int i = 0; i < values.length; i += itemsPerLine )
            {
                int remaining = values.length - i;
                int count = Math.min( itemsPerLine, remaining );
                String lineName = genLineName( name, count, idxFmt, i );

                s.printField( lineName,
                    ArrayPrinter.toString( values, i, count, ", ", writer ) );
            }
        }
    }

    /***************************************************************************
     * @param count
     * @return
     **************************************************************************/
    private static String genIndexFormat( int count )
    {
        int spaces = ( int )Math.ceil( Math.log10( count ) );
        String idxFmt = spaces < 2 ? "%d" : "%" + spaces + "d";
        return idxFmt;
    }

    /***************************************************************************
     * @param name
     * @param count
     * @param idxFmt
     * @param i
     * @return
     **************************************************************************/
    private static String genLineName( String name, int count, String idxFmt,
        int i )
    {
        if( count == 1 )
        {
            String fmt = "%s [" + idxFmt + "]";
            return String.format( fmt, name, i );
        }

        int last = i + count - 1;
        String fmt = "%s [" + idxFmt + " - " + idxFmt + "]";
        return String.format( fmt, name, i, last );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return stream.toString();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface ITierPrinter
    {
        /**
         * @param printer
         */
        public void printFields( FieldPrinter printer );
    }
}
