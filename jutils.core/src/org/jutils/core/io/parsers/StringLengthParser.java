package org.jutils.core.io.parsers;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IParser;

/*******************************************************************************
 * An parser that ensures that a string is a given length.
 ******************************************************************************/
public class StringLengthParser implements IParser<String>
{
    /** The minimum length to be ensured. */
    private final Integer minLength;
    /** The maximum length to be ensured. */
    private final Integer maxLength;
    /**  */
    private final boolean trim;

    /***************************************************************************
     * Creates a new parser that will ensure strings of length {@code 1}.
     **************************************************************************/
    public StringLengthParser()
    {
        this( 1, 1024 );
    }

    /***************************************************************************
     * Creates a new parser that will ensure strings of the specified length.
     * @param minLength the minimum length to ensure.
     * @param maxLength the maximum length to ensure.
     **************************************************************************/
    public StringLengthParser( Integer minLength, Integer maxLength )
    {
        this( minLength, maxLength, true );
    }

    /***************************************************************************
     * Creates a new parser that will ensure strings of the specified length.
     * @param minLength
     * @param maxLength
     * @param trim
     **************************************************************************/
    public StringLengthParser( Integer minLength, Integer maxLength,
        boolean trim )
    {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.trim = trim;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String parse( String text ) throws ValidationException
    {
        if( trim )
        {
            text = text.trim();
        }
        if( ( minLength != null && text.length() < minLength ) ||
            ( maxLength != null && text.length() > maxLength ) )
        {
            int min = minLength != null ? minLength : 0;

            if( maxLength != null )
            {
                throw new ValidationException( "Must be between " + min +
                    " and " + maxLength + " characters." );
            }

            throw new ValidationException(
                "Must be at least " + minLength + " characters." );
        }

        return text;
    }
}
