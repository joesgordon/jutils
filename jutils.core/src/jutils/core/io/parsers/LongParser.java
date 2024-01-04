package jutils.core.io.parsers;

import jutils.core.ValidationException;
import jutils.core.io.IParser;

/*******************************************************************************
 * A parser that ensures that integers fall within a specified range.
 ******************************************************************************/
public class LongParser implements IParser<Long>
{
    /** The minimum bound, inclusive; not checked if {@code null}. */
    private final Long min;
    /** The maximum bound, inclusive; not checked if {@code null}. */
    private final Long max;

    /***************************************************************************
     * Creates a parser that ensures the text is an integer and performs no
     * bounds checking.
     **************************************************************************/
    public LongParser()
    {
        this( null, null );
    }

    /***************************************************************************
     * Creates a parser that ensures the text is an integer and performs the
     * specified bounds checking.
     * @param min The minimum bound, inclusive; not checked if {@code null}.
     * @param max The maximum bound, inclusive; not checked if {@code null}.
     **************************************************************************/
    public LongParser( Long min, Long max )
    {
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Long parse( String text ) throws ValidationException
    {
        try
        {
            long i = Long.parseLong( text );

            if( min != null && i < min )
            {
                throw new ValidationException(
                    "Value less than minimum: " + i + " < " + min );
            }

            if( max != null && i > max )
            {
                throw new ValidationException(
                    "Value greater than maximum: " + i + " > " + max );
            }

            return i;
        }
        catch( NumberFormatException ex )
        {
            String err = generateErrorMessage( text, Long.MIN_VALUE,
                Long.MAX_VALUE );
            throw new ValidationException( err );
        }
    }

    /***************************************************************************
     * @param text
     * @param min
     * @param max
     * @return
     **************************************************************************/
    public static String generateErrorMessage( String text, long min, long max )
    {
        int idx = 0;
        char c;

        if( !text.isEmpty() )
        {
            c = text.charAt( idx );

            if( c == '+' || c == '-' )
            {
                idx++;
            }
        }
        else
        {
            return "No input";
        }

        for( ; idx < text.length(); idx++ )
        {
            c = text.charAt( idx );
            if( !Character.isDigit( c ) )
            {
                String msg = String.format(
                    "Invalid integer character '%c' at index %d", c, idx );
                return msg;
            }
        }

        return "Input outside of range " + min + " to " + max;
    }
}
