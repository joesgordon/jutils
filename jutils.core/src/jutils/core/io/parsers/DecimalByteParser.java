package jutils.core.io.parsers;

import jutils.core.ValidationException;
import jutils.core.io.IParser;

/*******************************************************************************
 * A parser that ensures that the provided text represents a hexadecimal
 * integer.
 ******************************************************************************/
public class DecimalByteParser implements IParser<Byte>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Byte min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Byte max;

    /***************************************************************************
     * 
     **************************************************************************/
    public DecimalByteParser()
    {
        this( null, null );
    }

    /***************************************************************************
     * @param min
     * @param max
     **************************************************************************/
    public DecimalByteParser( Byte min, Byte max )
    {
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Byte parse( String text ) throws ValidationException
    {
        byte value;

        try
        {
            value = Byte.valueOf( text );
        }
        catch( NumberFormatException ex )
        {
            String err = LongParser.generateErrorMessage( text, Byte.MIN_VALUE,
                Byte.MAX_VALUE );
            throw new ValidationException( err );
        }

        if( min != null && value < min )
        {
            String err = String.format( "Value less than minimum: %s < %d",
                value, min );
            throw new ValidationException( err );
        }

        if( max != null && value > max )
        {
            String err = String.format( "Value greater than maximum: %s > %d",
                value, max );
            throw new ValidationException( err );
        }

        return value;
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static String toString( byte value )
    {
        return Long.toBinaryString( value & 0xFF );
    }
}
