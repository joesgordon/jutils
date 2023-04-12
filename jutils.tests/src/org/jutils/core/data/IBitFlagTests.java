package org.jutils.core.data;

import org.junit.Assert;
import org.junit.Test;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IBitFlagTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testBit0Set()
    {
        TestBitFlag flag = TestBitFlag.BIT00;

        Assert.assertTrue( flag.info.getFlag( 0x01 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testBit0Clear()
    {
        TestBitFlag flag = TestBitFlag.BIT00;

        Assert.assertFalse( flag.info.getFlag( 0xFFFFFFFFFFFFFFFEL ) );
    }

    /**
     * 
     */
    private static enum TestBitFlag implements IBitFlag
    {
        /**  */
        BIT00( 0 ),
        /**  */
        BIT04( 4 ),
        /**  */
        BIT08( 8 ),
        /**  */
        BIT12( 12 ),
        /**  */
        BIT16( 16 ),
        /**  */
        BIT20( 20 ),
        /**  */
        BIT24( 24 );

        /**  */
        public final BitFlagInfo info;

        /**
         * @param bit
         */
        private TestBitFlag( int bit )
        {
            this.info = new BitFlagInfo( bit, "Bit " + bit );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getBit()
        {
            return info.bit;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long getMask()
        {
            return info.mask;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return info.name;
        }
    }
}
