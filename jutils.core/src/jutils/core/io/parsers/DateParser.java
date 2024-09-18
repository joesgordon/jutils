package jutils.core.io.parsers;

import java.time.LocalDate;
import java.util.List;

import jutils.core.Utils;
import jutils.core.ValidationException;
import jutils.core.io.IParser;
import jutils.core.time.TimeUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DateParser implements IParser<LocalDate>
{
    /**  */
    private final IntegerParser yearParser;
    /**  */
    private final IntegerParser monthParser;
    /**  */
    private final IntegerParser dayParser;

    /***************************************************************************
     * 
     **************************************************************************/
    public DateParser()
    {
        this.yearParser = new IntegerParser( 1401, null );
        this.monthParser = new IntegerParser( 1, 12 );
        this.dayParser = new IntegerParser( 1, 31 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDate parse( String str ) throws ValidationException
    {
        LocalDate date = TimeUtils.utcDateNow();

        return parse( str, date );
    }

    /***************************************************************************
     * @param str
     * @param date
     * @return
     * @throws ValidationException
     **************************************************************************/
    public LocalDate parse( String str, LocalDate date )
        throws ValidationException
    {
        String s = str.trim();

        List<String> fields = Utils.split( s, '/' );

        if( fields.size() > 3 )
        {
            String err = String.format(
                "Too many fields separated by '/'; Expected <= 3, found %d",
                fields.size() );
            throw new ValidationException( err );
        }

        int missing = 3 - fields.size();

        for( int i = 0; i < missing; i++ )
        {
            fields.add( 0, "" );
        }

        return parse( fields.get( 0 ), fields.get( 1 ), fields.get( 2 ), date );
    }

    /***************************************************************************
     * @param yearStr
     * @param monthStr
     * @param dayStr
     * @param date
     * @return
     * @throws ValidationException
     **************************************************************************/
    public LocalDate parse( String yearStr, String monthStr, String dayStr,
        LocalDate date ) throws ValidationException
    {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        if( !yearStr.isEmpty() )
        {
            year = yearParser.parse( yearStr );
        }

        if( !monthStr.isEmpty() )
        {
            month = monthParser.parse( monthStr );
        }

        if( !dayStr.isEmpty() )
        {
            day = dayParser.parse( dayStr );
        }

        return LocalDate.of( year, month, day );
    }

    /***************************************************************************
     * @param date
     * @return
     **************************************************************************/
    public static String toString( LocalDate date )
    {
        return String.format( "%04d/%02d/%02d", date.getYear(),
            date.getMonthValue(), date.getDayOfMonth() );
    }
}
