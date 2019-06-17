package org.jutils.io.parsers;

import org.jutils.NumberParsingUtils;
import org.jutils.ValidationException;
import org.jutils.io.IParser;

/*******************************************************************************
 * A parser that ensures that the provided text represents a hexadecimal
 * integer.
 ******************************************************************************/
public class HexIntegerParser implements IParser<Integer>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Integer min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Integer max;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexIntegerParser()
    {
        this( null, null );
    }

    /***************************************************************************
     * @param min
     * @param max
     **************************************************************************/
    public HexIntegerParser( Integer min, Integer max )
    {
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Integer parse( String text ) throws ValidationException
    {
        try
        {
            int i = NumberParsingUtils.parseHexInteger( text );

            if( min != null && i < min )
            {
                throw new ValidationException( "Value less than minimum: " +
                    Integer.toHexString( i ).toUpperCase() + " < " +
                    Integer.toHexString( min ).toUpperCase() );
            }

            if( max != null && i > max )
            {
                throw new ValidationException( "Value greater than maximum: " +
                    Integer.toHexString( i ).toUpperCase() + " > " +
                    Integer.toHexString( max ).toUpperCase() );
            }

            return i;
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }
    }
}
