package org.jutils.io.parsers;

import org.jutils.ValidationException;
import org.jutils.io.IParser;

/*******************************************************************************
 * A parser that ensures that the provided text represents a float.
 ******************************************************************************/
public class FloatParser implements IParser<Float>
{
    /** The minimum allowed value for this parser (inclusive). */
    private final Float min;
    /** The maximum allowed value for this parser (inclusive). */
    private final Float max;
    /** Flag that indicates that empty values are valid when {@code true}. */
    private final boolean allowEmpty;
    /** Value used when the text to be validated is empty. */
    private final Float emptyValue;

    /***************************************************************************
     * Creates a float parser that has no minimum or maximum and does not allow
     * empty strings.
     **************************************************************************/
    public FloatParser()
    {
        this( null, null );
    }

    /***************************************************************************
     * Creates a float parser with the provided minimum and maximum that does
     * not allow empty strings.
     * @param min the minimum value (inclusive).
     * @param max the maximum value (inclusive).
     **************************************************************************/
    public FloatParser( Float min, Float max )
    {
        this( min, max, false, null );
    }

    /***************************************************************************
     * Creates a float parser with the provided minimum and maximum that allow
     * empty strings according to the user's specifications.
     * @param min the minimum value (inclusive).
     * @param max the maximum value (inclusive).
     * @param allowEmpty allows empty text to be the provided value if
     * {@code true}.
     * @param emptyValue value used when the text to be validated is empty.
     **************************************************************************/
    public FloatParser( Float min, Float max, boolean allowEmpty,
        Float emptyValue )
    {
        this.min = min;
        this.max = max;

        this.allowEmpty = allowEmpty;
        this.emptyValue = emptyValue;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Float parse( String text ) throws ValidationException
    {
        if( text.isEmpty() && allowEmpty )
        {
            return emptyValue;
        }

        try
        {
            Float d = Float.parseFloat( text );

            testBounds( d );

            return d;
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }
    }

    /***************************************************************************
     * @param d
     * @throws ValidationException
     **************************************************************************/
    private void testBounds( float d ) throws ValidationException
    {
        boolean exceedsMin = min != null && d < min;
        boolean exceedsMax = max != null && d > max;

        if( exceedsMin || exceedsMax )
        {
            String msg = "";

            if( min != null )
            {
                msg += min + " <= ";
            }

            msg += "x ";

            if( max != null )
            {
                msg += "<= " + max;
            }

            throw new ValidationException(
                "Number (" + d + ") out of bounds: " + msg );
        }
    }
}
