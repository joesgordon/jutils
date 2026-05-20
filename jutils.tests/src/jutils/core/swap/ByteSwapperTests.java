package jutils.core.swap;

import org.junit.Assert;
import org.junit.Test;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ByteSwapperTests
{
    /***************************************************************************
    *
    ***************************************************************************/
    @Test
    public void testSwapInt()
    {
        int value = 0x12345678;
        int expected = 0x78563412;
        int actual = ByteSwapper.swap( value );

        Assert.assertEquals(
            String.format( "Expected 0x%08X; got 0x%08X", expected, actual ),
            expected, actual );
    }
}
