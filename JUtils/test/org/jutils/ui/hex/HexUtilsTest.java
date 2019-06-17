package org.jutils.ui.hex;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.NumberParsingUtils;
import org.jutils.io.LogUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexUtilsTest
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testToUnsigned()
    {
        for( int b = Byte.MIN_VALUE; b <= Byte.MAX_VALUE; b++ )
        {
            int i = HexUtils.toUnsigned( ( byte )b );

            if( b < 0 )
            {
                Assert.assertEquals( b + 256, i );
            }
            else
            {
                Assert.assertEquals( b, i );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testStuff()
    {
        int max = 10000000;
        long start = System.nanoTime();
        String str;
        long num;

        for( int i = -max; i < max; i++ )
        {
            str = Long.toHexString( i );
            // num = Long.parseLong( str, 16 );
            num = NumberParsingUtils.parseHexLong( str );
            // num = new BigInteger( str, 16 ).longValue();
            // num = parseHexLong2( str );

            if( num != i )
            {
                String msg = String.format( "%d != %d for %s", i, num, str );
                Assert.assertEquals( msg, i, num );
            }
        }
        long estTime = System.nanoTime() - start;
        double sec = estTime / 1000000000.0;
        LogUtils.printDebug( String.format( "Took: %3fs", sec ) );
    }

    /***************************************************************************
     * From <a
     * href="http://stackoverflow.com/questions/1410168">StackOverflow</a>.
     * @param s the string containing no more than 16 hexadecimal characters
     * (and no other characters) to be parsed as a 64 signed integral value.
     * @return the long parsed.
     * @throws NumberFormatException if the string is empty or if the provided
     * characters cannot be parsed as a long value.
     * @see Long#parseLong(String, int)
     **************************************************************************/
    public static long parseHexLong2( String s ) throws NumberFormatException
    {
        int len = s.length();

        if( len > 16 )
        {
            throw new NumberFormatException(
                "A long may be no longer than 16 hexadecimal characters: [" +
                    len + "] '" + s + "'" );
        }
        else if( len == 0 )
        {
            throw new NumberFormatException( "Empty string" );
        }

        long msb = 0;
        long lsb = 0;

        if( len > 8 )
        {
            msb = Long.parseLong( s.substring( 0, len - 8 ), 16 );
            lsb = Long.parseLong( s.substring( len - 8 ), 16 );
        }
        else
        {
            lsb = Long.parseLong( s, 16 );
        }

        return msb << 32 | lsb;
    }
}
