package jutils.core.io.parsers;

import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Test;

import jutils.core.ValidationException;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TimeParserTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_Empty()
    {
        try
        {
            new TimeParser().parse( "" );
            Assert.fail( "Expected exception for empty string" );
        }
        catch( ValidationException ex )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_HourMin()
    {
        LocalTime expected = LocalTime.of( 3, 4, 0, 0 );
        String str = String.format( "%d:%02d", expected.getHour(),
            expected.getMinute() );

        testParser( expected, str );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_HourMinSec()
    {
        LocalTime expected = LocalTime.of( 3, 4, 5, 0 );
        String str = String.format( "%d:%02d:%02d", expected.getHour(),
            expected.getMinute(), expected.getSecond() );

        testParser( expected, str );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_HourMinSecMicros()
    {
        int millis = 123456;
        int nanos = millis * 1000;
        LocalTime expected = LocalTime.of( 3, 4, 5, nanos );
        String str = String.format( "%d:%02d:%02d.%d", expected.getHour(),
            expected.getMinute(), expected.getSecond(), millis );

        testParser( expected, str );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_HourMinSecTenths()
    {
        int tenths = 1;
        int nanos = tenths * 100 * 1000 * 1000;
        LocalTime expected = LocalTime.of( 3, 4, 5, nanos );
        String str = String.format( "%d:%02d:%02d.%d", expected.getHour(),
            expected.getMinute(), expected.getSecond(), tenths );

        testParser( expected, str );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_HourMinSecNanos()
    {
        LocalTime expected = LocalTime.of( 3, 4, 5, 123456789 );
        String str = String.format( "%d:%02d:%02d.%09d", expected.getHour(),
            expected.getMinute(), expected.getSecond(), expected.getNano() );

        testParser( expected, str );
    }

    /***************************************************************************
     * @param expected
     * @param str
     **************************************************************************/
    private void testParser( LocalTime expected, String str )
    {
        TimeParser parser = new TimeParser();

        try
        {
            LocalTime actual = parser.parse( str );
            Assert.assertEquals( expected, actual );
        }
        catch( ValidationException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }
}
