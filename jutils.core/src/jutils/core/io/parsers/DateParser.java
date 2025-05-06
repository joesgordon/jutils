package jutils.core.io.parsers;

import java.time.DateTimeException;
import java.time.LocalDate;

import jutils.core.ValidationException;
import jutils.core.io.IParser;
import jutils.core.time.TimeUtils;

/*******************************************************************************
 * Parses a date string into a {@link LocalDate}.
 * @see #parse(String)
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
        this.yearParser = new IntegerParser();
        this.monthParser = new IntegerParser( 1, 12 );
        this.dayParser = new IntegerParser( 1, 31 );
    }

    /***************************************************************************
     * Parses a date string (delimiter may be {@code '-'} or {@code '/'} using
     * the formats: <ul> <li>{@code y/m/d}</li> <li>{@code m/d} - using current
     * year</li> </ul> where: <ul> <li>{@code y} is a year. 2-digit year will be
     * interpreted as that 2-digit year AD rather than centered around
     * 2000.</li> <li>{@code m} is a month number, 1 - 12.</li> <li>{@code d} is
     * a day, 1 - 31 (or the max day for that month/year)</li> </ul>
     * @param str the date string to be parsed.
     * @return the parsed date; guaranteed non-null.
     * @throws ValidationException if the provided string is empty, a value
     * other than a number or date delimiter is found, or the parsed values do
     * not make sense as a date.
     **************************************************************************/
    @Override
    public LocalDate parse( String str ) throws ValidationException
    {
        if( str.isEmpty() )
        {
            throw new ValidationException(
                "Cannot parse date from empty string" );
        }

        int yearCoef = 1;

        int [] ifrom = { -1, -1, -1 };
        int [] ito = { -1, -1, -1 };
        int ii = 0;
        boolean isField = false;
        int fieldCount = 0;

        for( int i = 0; i < str.length(); i++ )
        {
            if( ii > ifrom.length )
            {
                String err = String.format(
                    "Incorrect number of fields in date at %d: found %d; expected 2 or 3",
                    i, ii + 1 );
                throw new ValidationException( err );
            }

            char c = str.charAt( i );
            int idx = ifrom[ii];

            if( c == ' ' && !isField )
            {
                continue;
            }
            else if( c == '-' )
            {
                if( ii == 0 && idx < 0 )
                {
                    ifrom[ii] = i;
                    ito[ii] = i + 1;
                    fieldCount++;

                    yearCoef = -1;
                }
                else
                {
                    ii++;
                    isField = false;
                }
            }
            else if( c == '/' )
            {
                ii++;
                isField = false;
            }
            else if( c < 0x29 && c > 0x3A )
            {
                String err = String.format(
                    "Invalid character found at %d: found %c (0x%02X); expected a number",
                    i, c, c );
                throw new ValidationException( err );
            }
            else
            {
                if( idx < 0 )
                {
                    ifrom[ii] = i;
                    ito[ii] = i + 1;
                    fieldCount++;
                }
                else
                {
                    ito[ii] = i + 1;
                }
            }
        }

        if( fieldCount == 2 )
        {
            ifrom[2] = ifrom[1];
            ifrom[1] = ifrom[0];
            ifrom[0] = 0;

            ito[2] = ito[1];
            ito[1] = ito[0];
            ito[0] = 0;
        }
        else if( fieldCount != 3 )
        {
            String err = String.format(
                "Incorrect number of fields in date: found %d; expected 2 or 3",
                fieldCount );
            throw new ValidationException( err );
        }

        String yearStr = ifrom[0] > -1
            ? str.substring( ifrom[0], ito[0] ).trim()
            : "";
        String monthStr = str.substring( ifrom[1], ito[1] ).trim();
        String dayStr = str.substring( ifrom[2], ito[2] ).trim();

        LocalDate date = TimeUtils.utcDateNow();

        int year;
        int month;
        int day;

        if( yearStr.isEmpty() )
        {
            year = date.getYear();
        }
        else
        {
            year = yearParser.parse( yearStr );
        }

        month = monthParser.parse( monthStr );
        day = dayParser.parse( dayStr );

        year *= yearCoef;

        try
        {
            return LocalDate.of( year, month, day );
        }
        catch( DateTimeException ex )
        {
            String err = String.format(
                "Cannot parse date from y/m/d: %d/%d/%d: %s", year, month, day,
                ex.getMessage() );
            throw new ValidationException( err, ex );
        }
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
