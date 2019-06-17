package org.jutils.time;

import org.junit.Assert;
import org.junit.Test;

public class TimeUtilsTests
{
    @Test
    public void test()
    {
        long expected = 18921600000L;
        long actual = TimeUtils.getMillisIntoYear( 8, 8, 2018 );

        Assert.assertEquals( expected, actual );
    }
}
