package org.jutils.io;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.utils.BitArray;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitBufferTest
{
    /**  */
    private static final byte [] fromArray;
    /**  */
    private static final byte [] toArray;

    static
    {
        fromArray = new byte[10 * ( 1 << 20 )];
        toArray = new byte[fromArray.length];

        new Random().nextBytes( fromArray );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testNonZero()
    {
        for( int i = 0; i < fromArray.length; i++ )
        {
            if( fromArray[i] != 0 )
            {
                return;
            }
        }

        Assert.assertTrue( "The from array is all zeros", false );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testFind()
    {
        byte [] bytes = new byte[] { ( byte )0x9E, ( byte )0xFE };
        BitBuffer buf = new BitBuffer( bytes );
        BitArray bits = new BitArray();

        bits.set( "111011111110" );

        BitPosition pos = buf.find( bits, 0, true );

        Assert.assertNotNull( pos );
        Assert.assertEquals( 0, pos.getByte() );
        Assert.assertEquals( 4, pos.getBit() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testRead()
    {
        BitBuffer from = new BitBuffer( fromArray );
        BitBuffer to = new BitBuffer( toArray );

        int bitCount = fromArray.length * 8;

        from.writeTo( to, bitCount );

        for( int i = 0; i < fromArray.length; i++ )
        {
            Assert.assertEquals( fromArray[i], toArray[i] );
        }
    }
}
