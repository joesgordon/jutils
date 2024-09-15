package jutils.core.io.parsers;

import jutils.core.NumberParsingUtils;
import jutils.core.ValidationException;
import jutils.core.io.IParser;

/*******************************************************************************
 * A parser that ensures that the provided text represents a hexadecimal
 * integer.
 ******************************************************************************/
public class BinaryByteParser implements IParser<Byte>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Byte min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Byte max;

    /***************************************************************************
     * 
     **************************************************************************/
    public BinaryByteParser()
    {
        this( null, null );
    }

    /***************************************************************************
     * @param min
     * @param max
     **************************************************************************/
    public BinaryByteParser( Byte min, Byte max )
    {
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Byte parse( String text ) throws ValidationException
    {
        int ival = 0;
        int digits = 0;

        for( int i = 0; i < text.length(); i++ )
        {
            char c = text.charAt( i );

            if( c == '0' )
            {
                ival = ival << 1;
                digits++;
            }
            else if( c == '1' )
            {
                ival = ival << 1;
                ival |= 1;
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

            if( digits > Byte.SIZE )
            {
                throw new ValidationException( "Too many binary digits found" );
            }
        }

        byte value = ( byte )ival;

        if( min != null && value < min )
        {
            String err = String.format( "Value less than minimum: %s < %s",
                toString( value ), toString( min ) );
            throw new ValidationException( err );
        }

        if( max != null && value > max )
        {
            String err = String.format( "Value greater than maximum: %s > %s",
                toString( value ), toString( max ) );
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
        return NumberParsingUtils.toLeadingBinaryString( value );
    }
}
