package org.jutils.core.io.vax;

import org.jutils.core.io.IStdSerializer;

/*******************************************************************************
 *
 ******************************************************************************/
public class VaxDoubleBytesSerializer implements IStdSerializer<Double, byte []>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Double read( byte [] resource )
    {
        int b0 = resource[0] & 0xFF;
        long b1 = resource[1] & 0xFF;
        long b2 = resource[2] & 0xFF;
        long b3 = resource[3] & 0xFF;
        long b4 = resource[4] & 0xFF;
        long b5 = resource[5] & 0xFF;
        long b6 = resource[6] & 0xFF;
        long b7 = resource[7] & 0xFF;

        boolean sign;
        long exponent;
        long mantissa;

        sign = ( b0 & VaxDouble.B0_SIGN_MASK ) == VaxDouble.B0_SIGN_MASK;

        exponent = ( b0 & VaxDouble.B0_EXP_MASK ) << 1;
        exponent |= ( b1 & VaxDouble.B1_EXP_MASK ) >> 7;

        mantissa = ( b1 & VaxDouble.B1_MAN_MASK ) << 48;
        mantissa |= b2 << 40;
        mantissa |= b3 << 32;
        mantissa |= b4 << 24;
        mantissa |= b5 << 16;
        mantissa |= b6 << 8;
        mantissa |= b7 << 0;

        return VaxDouble.calcVaxValue( sign, exponent, mantissa );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( Double item, byte [] resource )
    {
        // TODO Auto-generated method stub
    }
}
