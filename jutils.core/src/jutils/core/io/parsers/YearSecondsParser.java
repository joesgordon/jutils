package jutils.core.io.parsers;

import jutils.core.ValidationException;
import jutils.core.io.IParser;
import jutils.core.time.YearNanos;

/*******************************************************************************
 * 
 ******************************************************************************/
public class YearSecondsParser implements IParser<YearNanos>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public YearNanos parse( String str ) throws ValidationException
    {
        YearNanos nanos = new YearNanos();

        str = str.trim();

        int space = str.indexOf( ' ' );

        if( space < 0 )
        {
            throw new ValidationException(
                "Year and seconds must be separated by a space" );
        }

        String yearStr = str.substring( 0, space ).trim();
        String secsStr = str.substring( space ).trim();

        // TODO Auto-generated method stub
        return nanos;
    }
}
