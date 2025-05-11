package jutils.core.io.parsers;

import java.time.LocalDate;

import jutils.core.ValidationException;
import jutils.core.io.IParser;
import jutils.core.time.TimeUtils;
import jutils.core.time.YearNanos;

/*******************************************************************************
 * Defines a {@link IParser} that reads <i>years</i> and <i>seconds into the
 * year</i> from a string. The string is expected to be formatted
 * {@code yyyy SSSSSSSS.sssssssss}.
 ******************************************************************************/
public class YearSecondsParser implements IParser<YearNanos>
{
    /**  */
    private final IntegerParser yearParser;
    /**  */
    private final LongParser nanosParser;

    /***************************************************************************
     * 
     **************************************************************************/
    public YearSecondsParser()
    {
        LocalDate min = LocalDate.MIN;
        LocalDate max = LocalDate.MAX;

        this.yearParser = new IntegerParser( min.getYear(), max.getYear() );
        this.nanosParser = new LongParser( 0L, TimeUtils.NANOS_PER_DAY * 366 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public YearNanos parse( String str ) throws ValidationException
    {
        YearNanos yn = new YearNanos();
        String yearStr = null;
        String fracSecsStr = "0.0";
        String secsStr = "0";
        String nanosStr = "0";

        int year = 0;
        int seconds = 0;
        int nanos = 0;

        int spaceIdx = -1;
        int dotIdx = -1;

        str = str.trim();

        if( str.isBlank() )
        {
            throw new ValidationException(
                "Unable to parse date/time from empty string" );
        }

        spaceIdx = str.indexOf( ' ' );

        if( spaceIdx < 0 )
        {
            yearStr = str;
        }
        else
        {

        }

        fracSecsStr = str.substring( spaceIdx ).trim();

        if( fracSecsStr.length() < 0 )
        {
        }
        else
        {
            dotIdx = fracSecsStr.indexOf( '.' );

            if( dotIdx < 0 )
            {
                ;
            }

        }

        return yn;
    }
}
