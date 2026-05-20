package jutils.core.io.parsers;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import jutils.core.ValidationException;
import jutils.core.time.TimeUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DateParserTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_Empty()
    {
        String str = "";

        DateParser parser = new DateParser();

        try
        {
            parser.parse( str );
            Assert.fail( "Should not parse an empty string" );
        }
        catch( ValidationException ex )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_DayMonth()
    {
        LocalDate now = TimeUtils.getUtcDateNow();
        LocalDate expected = LocalDate.of( now.getYear(), 4, 27 );
        String str = String.format( "%d/%d", expected.getMonthValue(),
            expected.getDayOfMonth() );

        testParser( expected, str );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_YearMonthDay()
    {
        LocalDate expected = LocalDate.of( 2018, 4, 27 );
        String str = String.format( "%d/%d/%d", expected.getYear(),
            expected.getMonthValue(), expected.getDayOfMonth() );

        testParser( expected, str );
    }

    /***************************************************************************
     * @param expected
     * @param str
     **************************************************************************/
    private void testParser( LocalDate expected, String str )
    {
        DateParser parser = new DateParser();

        try
        {
            LocalDate actual = parser.parse( str );
            Assert.assertEquals( expected, actual );
        }
        catch( ValidationException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }
}
