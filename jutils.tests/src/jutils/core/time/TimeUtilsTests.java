package jutils.core.time;

import org.junit.Assert;
import org.junit.Test;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TimeUtilsTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test()
    {
        long expected = 18921600000L;
        long actual = TimeUtils.getMillisIntoYear( 8, 8, 2018 );

        Assert.assertEquals( expected, actual );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testConstants()
    {
        Assert.assertEquals( TimeUtils.DAYS_IN_LEAP_YEAR, 366 );
        Assert.assertEquals( TimeUtils.DAYS_IN_WEEK, 7 );

        Assert.assertEquals( TimeUtils.HOURS_IN_DAY, 24 );
        Assert.assertEquals( TimeUtils.HOURS_IN_LEAP_YEAR, 24 * 366 );

        Assert.assertEquals( TimeUtils.MINUTES_IN_HOUR, 60 );
        Assert.assertEquals( TimeUtils.MINUTES_IN_DAY, 1440 );
        Assert.assertEquals( TimeUtils.MINUTES_IN_LEAP_YEAR, 527040 );

        Assert.assertEquals( TimeUtils.SECONDS_IN_MIN, 60 );
        Assert.assertEquals( TimeUtils.SECONDS_IN_HOUR, 3600 );
        Assert.assertEquals( TimeUtils.SECONDS_IN_DAY, 86400 );
        Assert.assertEquals( TimeUtils.SECONDS_IN_LEAP_YEAR, 31622400 );

        Assert.assertEquals( TimeUtils.MILLIS_IN_SEC, 1000 );
        Assert.assertEquals( TimeUtils.MILLIS_IN_MIN, 60000 );
        Assert.assertEquals( TimeUtils.MILLIS_IN_HOUR, 3600000 );
        Assert.assertEquals( TimeUtils.MILLIS_IN_DAY, 86400000 );
        Assert.assertEquals( TimeUtils.MILLIS_IN_LEAP_YEAR, 31622400000L );

        Assert.assertEquals( TimeUtils.MICROS_IN_MILLI, 1000 );
        Assert.assertEquals( TimeUtils.MICROS_IN_SEC, 1000000 );
        Assert.assertEquals( TimeUtils.MICROS_IN_MIN, 60000000 );
        Assert.assertEquals( TimeUtils.MICROS_IN_HOUR, 3600000000L );
        Assert.assertEquals( TimeUtils.MICROS_IN_DAY, 86400000000L );
        Assert.assertEquals( TimeUtils.MICROS_IN_LEAP_YEAR, 31622400000000L );

        Assert.assertEquals( TimeUtils.NANOS_IN_MICRO, 1000 );
        Assert.assertEquals( TimeUtils.NANOS_IN_MILLI, 1000000 );
        Assert.assertEquals( TimeUtils.NANOS_IN_SEC, 1000000000 );
        Assert.assertEquals( TimeUtils.NANOS_IN_MIN, 60000000000l );
        Assert.assertEquals( TimeUtils.NANOS_IN_HOUR, 3600000000000L );
        Assert.assertEquals( TimeUtils.NANOS_IN_DAY, 86400000000000L );
        Assert.assertEquals( TimeUtils.NANOS_IN_LEAP_YEAR, 31622400000000000L );
    }
}
