package jutils.core.io;

import org.junit.Assert;
import org.junit.Test;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitReaderTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testLongBits()
    {
        for( int flagBit = 0; flagBit < Long.SIZE; flagBit++ )
        {
            long value = 1L << flagBit;
            for( int bit = 0; bit < Long.SIZE; bit++ )
            {
                BitReader br = new BitReader( bit );
                boolean bitSet = br.read( value );

                assertBit( flagBit, bit, value, bitSet );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testIntBits()
    {
        for( int flagBit = 0; flagBit < Integer.SIZE; flagBit++ )
        {
            int value = 1 << flagBit;
            for( int bit = 0; bit < Integer.SIZE; bit++ )
            {
                BitReader br = new BitReader( bit );
                boolean bitSet = br.read( value );

                assertBit( flagBit, bit, value, bitSet );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testShortBits()
    {
        for( int flagBit = 0; flagBit < Short.SIZE; flagBit++ )
        {
            short value = ( short )( 1 << flagBit );
            for( int bit = 0; bit < Short.SIZE; bit++ )
            {
                BitReader br = new BitReader( bit );
                boolean bitSet = br.read( value );

                assertBit( flagBit, bit, value, bitSet );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testByteBits()
    {
        for( int flagBit = 0; flagBit < Byte.SIZE; flagBit++ )
        {
            byte value = ( byte )( 1 << flagBit );
            for( int bit = 0; bit < Byte.SIZE; bit++ )
            {
                BitReader br = new BitReader( bit );
                boolean bitSet = br.read( value );

                assertBit( flagBit, bit, value, bitSet );
            }
        }
    }

    /***************************************************************************
     * @param flagBit
     * @param bit
     * @param value
     * @param bitSet
     **************************************************************************/
    private static void assertBit( int flagBit, int bit, Number value,
        boolean bitSet )
    {
        boolean bitExpected = bit == flagBit;
        String msg = String.format(
            "Flag %d did not pass for bit %d with value 0x%016X: Expected %b, found %b",
            flagBit, bit, value.longValue(), bitExpected, bitSet );
        Assert.assertTrue( msg, bitSet == bitExpected );
    }
}
