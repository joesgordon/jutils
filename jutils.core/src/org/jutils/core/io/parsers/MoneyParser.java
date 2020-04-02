package org.jutils.core.io.parsers;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IParser;

/*******************************************************************************
 * A parser that ensures that a monetary value falls within a specified range.
 ******************************************************************************/
public class MoneyParser implements IParser<Integer>
{
    /** The minimum bound in cents, inclusive; not checked if {@code null}. */
    private final Integer min;
    /** The maximum bound in cents, inclusive; not checked if {@code null}. */
    private final Integer max;

    /***************************************************************************
     * Creates a parser that ensures the text is a monetary value and performs
     * no bounds checking.
     **************************************************************************/
    public MoneyParser()
    {
        this( null, null );
    }

    /***************************************************************************
     * Creates a parser that ensures the text is a monetary value and performs
     * the specified bounds checking.
     * @param min The minimum bound in cents, inclusive; not checked if
     * {@code null}.
     * @param max The maximum bound in cents, inclusive; not checked if
     * {@code null}.
     **************************************************************************/
    public MoneyParser( Integer min, Integer max )
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
        if( text.isEmpty() )
        {
            throw new ValidationException( "Cannot parse empty string" );
        }

        try
        {
            if( text.charAt( 0 ) == '$' )
            {
                text = text.substring( 1 );
            }

            int idx = text.indexOf( '.' );

            String dStr = "0";
            String pStr = "0";

            if( idx > -1 )
            {
                dStr = text.substring( 0, idx );
                int start = idx + 1;
                int end = text.length();
                int cnt = end - start;

                if( cnt > 2 )
                {
                    throw new ValidationException( "Too many decimal places." );
                }
                else if( cnt > 0 )
                {
                    pStr = text.substring( start, end );
                }
            }
            else
            {
                dStr = text;
            }

            int dollars = Integer.parseInt( dStr );
            int pennies = Integer.parseInt( pStr );

            int amount = dollars * 100 + pennies;

            if( min != null && amount < min )
            {
                throw new ValidationException(
                    "Value less than minimum: " + amount + " < " + min );
            }

            if( max != null && amount > max )
            {
                throw new ValidationException(
                    "Value greater than maximum: " + amount + " > " + max );
            }

            return amount;
        }
        catch( NumberFormatException ex )
        {
            throw new ValidationException( ex.getMessage() );
        }
    }
}
