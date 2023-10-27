package jutils.core.io.parsers;

import jutils.core.NumberParsingUtils;
import jutils.core.ValidationException;
import jutils.core.io.IParser;

/*******************************************************************************
 * A parser that ensures that the provided text represents a hexadecimal short.
 ******************************************************************************/
public class HexShortParser implements IParser<Short>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Short min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Short max;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexShortParser()
    {
        this( null, null );
    }

    /***************************************************************************
     * @param min
     * @param max
     **************************************************************************/
    public HexShortParser( Short min, Short max )
    {
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Short parse( String text ) throws ValidationException
    {
        try
        {
            short i = ( short )NumberParsingUtils.parseHexShort( text );

            if( min != null && i < min )
            {
                throw new ValidationException( "Value less than minimum: " +
                    Integer.toHexString( i & 0xFFFF ).toUpperCase() + " < " +
                    Integer.toHexString( min & 0xFFFF ).toUpperCase() );
            }

            if( max != null && i > max )
            {
                throw new ValidationException( "Value greater than maximum: " +
                    Integer.toHexString( i & 0xFFFF ).toUpperCase() + " > " +
                    Integer.toHexString( max & 0xFFFF ).toUpperCase() );
            }

            return i;
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }
    }
}
