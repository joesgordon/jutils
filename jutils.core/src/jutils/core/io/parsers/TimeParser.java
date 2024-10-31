package jutils.core.io.parsers;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.List;

import jutils.core.Utils;
import jutils.core.ValidationException;
import jutils.core.io.IParser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TimeParser implements IParser<LocalTime>
{
    /**  */
    private final IntegerParser hourParser;
    /**  */
    private final IntegerParser minuteParser;
    /**  */
    private final IntegerParser secondParser;
    /**  */
    private final IntegerParser fractionParser;

    /***************************************************************************
     * 
     **************************************************************************/
    public TimeParser()
    {
        this.hourParser = new IntegerParser( 0, 23 );
        this.minuteParser = new IntegerParser( 0, 59 );
        this.secondParser = new IntegerParser( 0, 59 );
        this.fractionParser = new IntegerParser( 0, 999999999 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalTime parse( String str ) throws ValidationException
    {
        if( str.isEmpty() )
        {
            throw new ValidationException(
                "Cannot parse time from empty string" );
        }

        String text = str.replace( " ", "" );
        text = text.replace( '.', ':' );

        List<String> fields = Utils.split( text, ':' );

        if( fields.size() < 2 || fields.size() > 4 )
        {
            throw new ValidationException(
                "Incorrect number of fields in time: " + fields.size() +
                    "; expected 2 to 4" );
        }

        int fcnt = fields.size();
        String hstr = fields.get( 0 );
        String mstr = fields.get( 1 );
        String sstr = fcnt > 2 ? fields.get( 2 ) : "00";
        String fstr = fcnt > 3 ? padRight( fields.get( 3 ), 9 ) : "0";

        if( mstr.length() != 2 )
        {
            throw new ValidationException( "Minutes must have 2 digits" );
        }

        if( sstr.length() != 2 )
        {
            throw new ValidationException( "Seconds must have 2 digits" );
        }

        int hour = parse( hstr, hourParser, "hours" );
        int min = parse( mstr, minuteParser, "minutes" );
        int sec = parse( sstr, secondParser, "seconds" );
        int fracs = parse( fstr, fractionParser, "fractional seconds" );

        try
        {
            return LocalTime.of( hour, min, sec, fracs );
        }
        catch( DateTimeException ex )
        {
            String err = String.format(
                "Cannot create time from format hh:mm:ss.SSSSSS = %02d:%02d:%02d.%06d: %s",
                hour, min, sec, fracs, ex.getMessage() );

            throw new ValidationException( err, ex );
        }
    }

    /***************************************************************************
     * @param str
     * @param cnt
     * @return
     **************************************************************************/
    private static String padRight( String str, int cnt )
    {
        return String.format( "%-" + cnt + "s", str ).replace( ' ', '0' );
    }

    /***************************************************************************
     * @param str
     * @param parser
     * @param unit
     * @return
     * @throws ValidationException
     **************************************************************************/
    private static int parse( String str, IntegerParser parser, String unit )
        throws ValidationException
    {
        try
        {
            return parser.parse( str );
        }
        catch( ValidationException ex )
        {
            String err = String.format( "Unable to parse %s: %s", unit,
                ex.getMessage() );
            throw new ValidationException( err );
        }
    }

    /***************************************************************************
     * @param d
     * @return
     **************************************************************************/
    public static String toString( LocalTime time )
    {
        return String.format( "%02d:%02d:%02d.%09d", time.getHour(),
            time.getMinute(), time.getSecond(), time.getNano() );
    }
}
