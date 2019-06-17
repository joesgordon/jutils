package org.jutils.io.parsers;

import org.jutils.NumberParsingUtils;
import org.jutils.ValidationException;
import org.jutils.io.IParser;

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
        try
        {
            long i = NumberParsingUtils.parseHexLong( text );

            if( min != null && i < min )
            {
                throw new ValidationException( "Value less than minimum: " +
                    Long.toHexString( i ).toUpperCase() + " < " +
                    Long.toHexString( min ).toUpperCase() );
            }

            if( max != null && i > max )
            {
                throw new ValidationException( "Value greater than maximum: " +
                    Long.toHexString( i ).toUpperCase() + " > " +
                    Long.toHexString( max ).toUpperCase() );
            }

            return i;
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }
    }
}
