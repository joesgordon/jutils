package org.jutils.core.io.vax;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.core.data.Pair;
import org.jutils.core.data.PairList;
import org.jutils.core.ui.hex.HexUtils;

/*******************************************************************************
 *
 ******************************************************************************/
public class VaxDoubleTests
{
    /**  */
    public static final PairList<Long, Double> EXAMPLES;

    static
    {
        EXAMPLES = new PairList<>();

        EXAMPLES.add( 0x4080000000000000L, 1.000000000000000 );
        EXAMPLES.add( 0xC080000000000000L, -1.000000000000000 );
        EXAMPLES.add( 0x4160000000000000L, 3.500000000000000 );
        EXAMPLES.add( 0xC160000000000000L, -3.500000000000000 );
        EXAMPLES.add( 0x41490FDAA22168BEL, 3.141592653589793 );
        EXAMPLES.add( 0xC1490FDAA22168BEL, -3.141592653589793 );
        EXAMPLES.add( 0x7DF0BDC21ABB48DBL, 1.0000000000000000E+37 );
        EXAMPLES.add( 0xFDF0BDC21ABB48DBL, -1.0000000000000000E+37 );
        EXAMPLES.add( 0x03081CEA14545C75L, 9.9999999999999999E-38 );
        EXAMPLES.add( 0x83081CEA14545C75L, -9.9999999999999999E-38 );
        EXAMPLES.add( 0x409E06521462CEE7L, 1.234567890123450 );
        EXAMPLES.add( 0xC09E06521462CEE7L, -1.234567890123450 );
        EXAMPLES.add( 0x48A71844DD9CF08EL, 85552.53801309344 );
        EXAMPLES.add( 0x48A718C4DDAF4E02L, 85553.53801528271 );
        EXAMPLES.add( 0x48A71944DDC17F4FL, 85554.53801745144 );
        EXAMPLES.add( 0x48A719C4DDD3E66FL, 85555.53801964523 );
        EXAMPLES.add( 0x48A71A44DDE6219CL, 85556.53802181856 );
        EXAMPLES.add( 0x48A71AC4DDF893BAL, 85557.53802401746 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_vaxGBinFracEqualsScale()
    {
        for( Pair<Long, Double> example : EXAMPLES )
        {
            long value = example.a;
            long mantissa = value & VaxDouble.MANTISSA_MASK;
            double expected = VaxDouble.calcVaxMantissa(
                mantissa );
            double actual = VaxDouble.calcVaxMantissaScale( mantissa );

            String msg = String.format( "For mantissa 0x%X from 0x%X", mantissa,
                value );
            Assert.assertEquals( msg, expected, actual, 0.00000000001 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_longExamples()
    {
        VaxDoubleLongSerializer vgls = new VaxDoubleLongSerializer();

        for( Pair<Long, Double> example : EXAMPLES )
        {
            long value = example.a;
            double expected = example.b;
            double actual = vgls.read( value );
            String msg = String.format( "For value 0x%X", value );
            Assert.assertEquals( msg, expected, actual, 0.00000000001 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_bytesExamples()
    {
        VaxDoubleBytesSerializer vgls = new VaxDoubleBytesSerializer();

        for( Pair<Long, Double> example : EXAMPLES )
        {
            byte [] value = longToBytes( example.a );
            double expected = example.b;
            double actual = vgls.read( value );
            String msg = String.format( "For value %s",
                HexUtils.toHexString( value, " " ) );
            Assert.assertEquals( msg, expected, actual, 0.00000000001 );
        }
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static byte [] longToBytes( long value )
    {
        byte [] result = new byte[8];
        for( int i = 7; i >= 0; i-- )
        {
            result[i] = ( byte )( value & 0xFF );
            value >>= 8;
        }
        return result;
    }
}
