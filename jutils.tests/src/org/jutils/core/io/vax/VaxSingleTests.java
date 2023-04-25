package org.jutils.core.io.vax;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.core.data.Pair;
import org.jutils.core.data.PairList;
import org.jutils.core.ui.hex.HexUtils;
import org.jutils.core.vax.VaxSingle;
import org.jutils.core.vax.VaxSingleBytesSerializer;
import org.jutils.core.vax.VaxSingleIntegerSerializer;

/*******************************************************************************
 *
 ******************************************************************************/
public class VaxSingleTests
{
    /**  */
    public static final PairList<Integer, Double> EXAMPLES;

    static
    {
        EXAMPLES = new PairList<>();

        EXAMPLES.add( 0x40800000, 1.000000000000000 );
        EXAMPLES.add( 0xC0800000, -1.000000000000000 );
        EXAMPLES.add( 0x41600000, 3.500000000000000 );
        EXAMPLES.add( 0xC1600000, -3.500000000000000 );
        EXAMPLES.add( 0x41490FD0, 3.141590 );
        EXAMPLES.add( 0xC1490FD0, -3.141590 );
        EXAMPLES.add( 0x7DF0BDC2, 9.999999933815813E+36 );
        EXAMPLES.add( 0xFDF0BDC2, -9.999999933815813E+36 );
        EXAMPLES.add( 0x03081CEA, 9.999999E-38 );
        EXAMPLES.add( 0x83081CEA, -9.999999E-38 );
        EXAMPLES.add( 0x409E0652, 1.2345678 );
        EXAMPLES.add( 0xC09E0652, -1.2345678 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_vaxSingleBinFracEqualsScale()
    {
        for( Pair<Integer, Double> example : EXAMPLES )
        {
            int value = example.a;
            int mantissa = value & VaxSingle.MANTISSA_MASK;
            double expected = VaxSingle.calcVaxMantissa( mantissa );
            double actual = VaxSingle.calcVaxMantissaScale( mantissa );

            String msg = String.format( "For mantissa 0x%X from 0x%X", mantissa,
                value );
            Assert.assertEquals( msg, expected, actual, 0.000001 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_intExamples()
    {
        VaxSingleIntegerSerializer vgls = new VaxSingleIntegerSerializer();

        for( Pair<Integer, Double> example : EXAMPLES )
        {
            int value = example.a;
            double expected = example.b;
            double actual = vgls.read( value );
            String msg = String.format( "For value 0x%X", value );
            Assert.assertEquals( msg, expected, actual, 0.000001 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_bytesExamples()
    {
        VaxSingleBytesSerializer vgls = new VaxSingleBytesSerializer();

        for( Pair<Integer, Double> example : EXAMPLES )
        {
            byte [] value = intToBytes( example.a );
            double expected = example.b;
            double actual = vgls.read( value );
            String msg = String.format( "For value %s",
                HexUtils.toHexString( value, " " ) );
            Assert.assertEquals( msg, expected, actual, 0.000001 );
        }
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static byte [] intToBytes( int value )
    {
        byte [] result = new byte[4];
        for( int i = result.length - 1; i > -1; i-- )
        {
            result[i] = ( byte )( value & 0xFF );
            value >>= 8;
        }
        return result;
    }
}
