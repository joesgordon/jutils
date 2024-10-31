package jutils.core.io.parsers;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import jutils.core.ValidationException;

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
        LocalDate expected = LocalDate.of( 2018, 4, 27 );
        String str = "";

        testParser( expected, str );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_DayOnly()
    {
        LocalDate expected = LocalDate.of( 2018, 4, 27 );
        String str = "" + expected.getDayOfMonth();

        testParser( expected, str );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_DayMonth()
    {
        LocalDate expected = LocalDate.of( 2018, 4, 27 );
        String str = String.format( "%d/%d", expected.getMonthValue(),
            expected.getDayOfMonth() );

        testParser( expected, str );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_DayMonthYear()
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
            LocalDate actual = parser.parse( str, expected );
            Assert.assertEquals( expected, actual );
        }
        catch( ValidationException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }
}
