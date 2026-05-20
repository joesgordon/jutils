package jutils.core.io.parsers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jutils.core.ValidationException;
import jutils.core.io.IParser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DateTimeParser implements IParser<LocalDateTime>
{
    /**  */
    private final DateParser dateParser;
    /**  */
    private final TimeParser timeParser;

    /***************************************************************************
     * 
     **************************************************************************/
    public DateTimeParser()
    {
        this.dateParser = new DateParser();
        this.timeParser = new TimeParser();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDateTime parse( String str ) throws ValidationException
    {
        int colon = str.indexOf( ':' );

        if( colon < 1 )
        {
            String err = "No colon found; time must be specified";
            throw new ValidationException( err );
        }

        int space = str.indexOf( ' ', 0, colon );

        String dateStr = "";
        String timeStr = "";

        if( space < 0 )
        {
            timeStr = str;
        }
        else
        {
            dateStr = str.substring( 0, space );
            timeStr = str.substring( space );
        }

        LocalDate date = dateParser.parse( dateStr );
        LocalTime time = timeParser.parse( timeStr );

        return LocalDateTime.of( date, time );
    }

    /***************************************************************************
     * @param dt
     * @return
     **************************************************************************/
    public static String toString( LocalDateTime dt )
    {
        return String.format( "%s %s", DateParser.toString( dt.toLocalDate() ),
            TimeParser.toString( dt.toLocalTime() ) );
    }
}
