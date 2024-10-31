package jutils.core.io.parsers;

import jutils.core.NumberParsingUtils;
import jutils.core.ValidationException;
import jutils.core.io.IParser;

/*******************************************************************************
 * A parser that ensures that the provided text represents a hexadecimal
 * integer.
 ******************************************************************************/
public class HexLongParser implements IParser<Long>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Long min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Long max;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexLongParser()
    {
        this( null, null );
    }

    /***************************************************************************
     * @param min
     * @param max
     **************************************************************************/
    public HexLongParser( Long min, Long max )
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

        try
        {
            value = NumberParsingUtils.parseHexLong( text );
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }

        if( min != null && value < min )
        {
            throw new ValidationException( "Value less than minimum: " +
                Long.toHexString( value ).toUpperCase() + " < " +
                Long.toHexString( min ).toUpperCase() );
        }

        if( max != null && value > max )
        {
            throw new ValidationException( "Value greater than maximum: " +
                Long.toHexString( value ).toUpperCase() + " > " +
                Long.toHexString( max ).toUpperCase() );
        }

        return value;
    }
}
