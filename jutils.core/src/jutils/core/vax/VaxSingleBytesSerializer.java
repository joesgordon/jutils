package jutils.core.vax;

import jutils.core.io.IStdSerializer;

/*******************************************************************************
 *
 ******************************************************************************/
public class VaxSingleBytesSerializer implements IStdSerializer<Double, byte []>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Double read( byte [] resource )
    {
        int b0 = resource[0] & 0xFF;
        int b1 = resource[1] & 0xFF;
        int b2 = resource[2] & 0xFF;
        int b3 = resource[3] & 0xFF;

        boolean sign;
        int exponent;
        int mantissa;

        sign = ( b0 & VaxSingle.B0_SIGN_MASK ) == VaxSingle.B0_SIGN_MASK;

        exponent = ( b0 & VaxSingle.B0_EXP_MASK ) << 1;
        exponent |= ( b1 & VaxSingle.B1_EXP_MASK ) >> 7;

        mantissa = ( b1 & VaxSingle.B1_MAN_MASK ) << 16;
        mantissa |= b2 << 8;
        mantissa |= b3 << 0;

        return VaxSingle.calcVaxValue( sign, exponent, mantissa );
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
