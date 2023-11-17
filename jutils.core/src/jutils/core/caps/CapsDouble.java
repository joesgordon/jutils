package jutils.core.caps;

/*******************************************************************************
 *
 ******************************************************************************/
public class CapsDouble
{
    /**  */
    public static final long SIGN_MASK = 1L << 47;
    /**  */
    public static final long EXPONENT_MASK = 0xFFL;
    /**  */
    public static final long HIDDEN_BIT_MASK = 1L << 39;
    /**  */
    public static final long MANTISSA_MASK = ( HIDDEN_BIT_MASK - 1 ) << 8;

    /**  */
    public static final int B0_SIGN_MASK = 0x80;
    /**  */
    public static final int B0_MAN_MASK = 0x7F;

    /***************************************************************************
     * @param sign
     * @param exponent
     * @param mantissa
     * @return
     **************************************************************************/
    public static double calcValue( boolean sign, long exponent, long mantissa )
    {
        long actualExponent = exponent - 128;
        double signVal = sign ? -1.0 : 1.0;
        double dman = calcMantissa( mantissa );
        double value = signVal * Math.pow( 2, actualExponent ) * dman;

        // LogUtils.printDebug( "got %E for %s, 0x%02X, 0x%016X -> %f", value,
        // sign, exponent, mantissa, dman );

        return value;
    }

    /***************************************************************************
     * @param mantissa
     * @return
     **************************************************************************/
    public static double calcMantissa( long mantissa )
    {
        double numerator = mantissa | HIDDEN_BIT_MASK;
        double denominator = HIDDEN_BIT_MASK << 1;
        return numerator / denominator;
    }

    /***************************************************************************
     * @param mantissa
     * @return
     **************************************************************************/
    public static double calcMantissaScale( long mantissa )
    {
        double fraction = mantissa / ( double )MANTISSA_MASK;

        return 0.5 * ( 1 + fraction );
    }
}
