package jutils.core.time;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Assert;
import org.junit.Test;

import jutils.core.timestamps.YearNanos;
import jutils.core.timestamps.YearNanos.NanoTimeUnit;

/*******************************************************************************
 * 
 ******************************************************************************/
public class YearNanosTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testDefaultConstructor()
    {
        YearNanos time = new YearNanos();
        LocalDateTime dateTime = time.toDateTime();

        Assert.assertEquals( 1970, dateTime.getYear() );
        Assert.assertEquals( Month.JANUARY, dateTime.getMonth() );
        Assert.assertEquals( 1, dateTime.getDayOfMonth() );
        Assert.assertEquals( 0, dateTime.getHour() );
        Assert.assertEquals( 0, dateTime.getMinute() );
        Assert.assertEquals( 0, dateTime.getSecond() );
        Assert.assertEquals( 0L, dateTime.getNano() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testDateTimeConstructor()
    {
        LocalDateTime dateTime = createSampleDateTime();
        YearNanos time = new YearNanos( dateTime );

        long nanosExpected = 3985445123456789L;

        Assert.assertEquals( 2005, time.year );
        Assert.assertEquals( nanosExpected, time.nanos );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static LocalDateTime createSampleDateTime()
    {
        int year = 2005;
        int hour = 3;
        int minute = 4;
        int second = 5;
        int nanosOfSecond = 123456789;
        LocalDateTime dateTime = LocalDateTime.of( year, Month.FEBRUARY, 16,
            hour, minute, second, nanosOfSecond );

        return dateTime;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testSetTimeSecondsRetain()
    {
        LocalDateTime dateTime = createSampleDateTime();
        YearNanos time = new YearNanos( dateTime );

        time.setTime( NanoTimeUnit.SECONDS, 21, true );

        long nanosExpected = 21 * 1000000000L + 123456789;

        Assert.assertEquals( nanosExpected, time.nanos );
    }
}
