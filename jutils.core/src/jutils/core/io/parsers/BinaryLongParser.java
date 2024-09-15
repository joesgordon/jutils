package jutils.core.io.parsers;

import jutils.core.ValidationException;
import jutils.core.io.IParser;

/*******************************************************************************
 * A parser that ensures that the provided text represents a hexadecimal
 * integer.
 ******************************************************************************/
public class BinaryLongParser implements IParser<Long>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Long min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Long max;

    /***************************************************************************
     * 
     **************************************************************************/
    public BinaryLongParser()
    {
        this( null, null );
    }

    /***************************************************************************
     * @param min
     * @param max
     **************************************************************************/
    public BinaryLongParser( Long min, Long max )
    {
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Long parse( String text ) throws ValidationException
    {
        long value = 0;
        int digits = 0;

        for( int i = 0; i < text.length(); i++ )
        {
            char c = text.charAt( i );

            if( c == '0' )
            {
                value = value << 1;
                digits++;
            }
            else if( c == '1' )
            {
                value = value << 1;
                value |= 1;
                digits++;
            }
            else if( c == ' ' )
            {
                continue;
            }
            else
            {
                throw new ValidationException( "Invalid binary character '" +
                    c + "' found at index " + i );
            }

            if( digits > Long.SIZE )
            {
                throw new ValidationException( "Too many binary digits found" );
            }
        }

        if( min != null && value < min )
        {
            String err = String.format( "Value less than minimum: %s < %s",
                Long.toBinaryString( value ), Long.toBinaryString( min ) );
            throw new ValidationException( err );
        }

        if( max != null && value > max )
        {
            String err = String.format( "Value greater than maximum: %s > %s",
                Long.toBinaryString( value ), Long.toBinaryString( max ) );
            throw new ValidationException( err );
        }

        return value;
    }
}
