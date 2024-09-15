package jutils.core.caps;

import jutils.core.io.IStdSerializer;

/*******************************************************************************
 *
 ******************************************************************************/
public class CapsDoubleBytesSerializer
    implements IStdSerializer<Double, byte []>
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

        boolean sign;
        long exponent;
        long mantissa;

        sign = ( b0 & CapsDouble.B0_SIGN_MASK ) == CapsDouble.B0_SIGN_MASK;

        mantissa = ( b0 & CapsDouble.B0_MAN_MASK ) << 32;
        mantissa |= b1 << 24;
        mantissa |= b2 << 16;
        mantissa |= b3 << 8;
        mantissa |= b4 << 0;

        exponent = b5 & CapsDouble.EXPONENT_MASK;

        return CapsDouble.calcValue( sign, exponent, mantissa );
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
