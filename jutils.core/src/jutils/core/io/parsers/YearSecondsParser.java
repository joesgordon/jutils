package jutils.core.io.parsers;

import jutils.core.ValidationException;
import jutils.core.io.IParser;
import jutils.core.time.TimeUtils;
import jutils.core.timestamps.YearNanos;

/*******************************************************************************
 * Defines a {@link IParser} that reads <i>years</i> and <i>seconds into the
 * year</i> from a string. The string is expected to be formatted:<ul>a
 * <li>{@code <year> <seconds>.<fraction>} - one or more spaces between
 * {@code year} and {@code seconds}; {@code fraction} can be tenths of a second
 * to nanoseconds.</li> <li>{@code <year> <seconds>} - one or more spaces
 * between {@code year} and {@code seconds}.</li>
 * <li>{@code <seconds>.<fraction>}.</li> <li>{@code <seconds>} - Seconds into
 * the current year.</li> </ul>
 ******************************************************************************/
public class YearSecondsParser implements IParser<YearNanos>
{
    /**  */
    private final ShortParser yearParser;
    /**  */
    private final LongParser secondsParser;
    /**  */
    private final LongParser nanosParser;

    /***************************************************************************
     * 
     **************************************************************************/
    public YearSecondsParser()
    {
        this.yearParser = new ShortParser();
        this.secondsParser = new LongParser( 0L,
            TimeUtils.SECONDS_IN_DAY * 366 );
        this.nanosParser = new LongParser( 0L, TimeUtils.NANOS_IN_SEC );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public YearNanos parse( final String str ) throws ValidationException
    {
        YearNanos yn = new YearNanos();

        String yearStr = "";
        String secsStr = "";
        String nanosStr = "";

        int year = 0;
        long seconds = 0;
        long nanos = 0;

        String text = str.trim();
        text = text.replaceAll( " +", " " );

        if( text.isBlank() )
        {
            throw new ValidationException(
                "Unable to parse date/time from empty string" );
        }

        int spaceIdx = text.indexOf( ' ' );
        int dotIdx = text.indexOf( '.' );

        if( spaceIdx > 0 )
        {
            yearStr = text.substring( 0, spaceIdx );
        }
        else
        {
            yearStr = "";
        }

        spaceIdx++;

        if( dotIdx >= 0 )
        {
            secsStr = text.substring( spaceIdx, dotIdx );
            nanosStr = text.substring( dotIdx + 1 );
        }
        else
        {
            secsStr = text.substring( spaceIdx );
            nanosStr = "";
        }

        if( secsStr.isEmpty() )
        {
            String err = String.format(
                "Unable to parse Year/Nanoseconds from \"%s\"", str );
            throw new ValidationException( err );
        }

        nanosStr = String.format( "%-9s", nanosStr );
        nanosStr = nanosStr.replace( ' ', '0' );

        year = yearStr.isEmpty() ? TimeUtils.getCurrentYear()
            : yearParser.parse( yearStr );
        seconds = secondsParser.parse( secsStr );
        nanos = nanosStr.isEmpty() ? 0L : nanosParser.parse( nanosStr );

        yn.year = ( short )year;
        yn.nanos = seconds * TimeUtils.NANOS_IN_SEC + nanos;

        return yn;
    }
}
