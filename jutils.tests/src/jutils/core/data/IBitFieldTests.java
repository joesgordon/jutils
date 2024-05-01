package jutils.core.data;

import org.junit.Assert;
import org.junit.Test;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IBitFieldTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testBit0Set()
    {
        TestBitFlag flag = TestBitFlag.BIT00;

        Assert.assertTrue( flag.getFlag( 0x01 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testBit0Clear()
    {
        TestBitFlag flag = TestBitFlag.BIT00;

        Assert.assertFalse( flag.getFlag( 0xFFFFFFFFFFFFFFFEL ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testFieldSet()
    {
        Assert.assertEquals( 26 - 14 + 1, TestBitsField.BITS14_26.size() );

        short expected = 0x1B95;
        long word = expected << TestBitsField.BITS14_26.getStartBit();

        Assert.assertEquals( expected,
            TestBitsField.BITS14_26.getField( word ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static enum TestBitFlag implements INamedBitField
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
        private final BitFieldInfo info;

        /**
         * @param bit
         */
        private TestBitFlag( int bit )
        {
            this.info = new BitFieldInfo( bit, "Bit " + bit );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getStartBit()
        {
            return info.getStartBit();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getEndBit()
        {
            return info.getEndBit();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long getMask()
        {
            return info.getMask();
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static enum TestBitsField implements INamedBitField
    {
        /**  */
        BITS00_07( 0, 7 ),
        /**  */
        BITS14_26( 14, 26 );

        /**  */
        private final BitFieldInfo info;

        /**
         * @param bit
         */
        private TestBitsField( int b0, int b1 )
        {
            this.info = new BitFieldInfo( "Bits " + b0 + " - " + b1, b0, b1 );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getStartBit()
        {
            return info.getStartBit();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getEndBit()
        {
            return info.getEndBit();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long getMask()
        {
            return info.getMask();
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
