package jutils.core.caps;

import jutils.core.io.IStdSerializer;

/*******************************************************************************
 *
 ******************************************************************************/
public class CapsSingleBytesSerializer
    implements IStdSerializer<Double, byte []>
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

        sign = ( b0 & CapsSingle.B0_SIGN_MASK ) == CapsSingle.B0_SIGN_MASK;

        mantissa = ( b0 & CapsSingle.B0_MAN_MASK ) << 16;
        mantissa |= b1 << 8;
        mantissa |= b2 << 0;

        exponent = b3 & CapsSingle.EXPONENT_MASK;

        return CapsSingle.calcValue( sign, exponent, mantissa );
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
