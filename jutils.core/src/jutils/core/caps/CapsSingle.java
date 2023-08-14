package jutils.core.caps;

/*******************************************************************************
 * <ul> <li>Bit 31: Sign</li> <li>Bits 23-30: Exponent</li> <li>Bits 0-22:
 * Mantissa</li> </ul>
 ******************************************************************************/
public class CapsSingle
{
    /**  */
    public static final int SIGN_MASK = 1 << 31;
    /**  */
    public static final int EXPONENT_MASK = 0xFF;
    /**  */
    public static final int HIDDEN_BIT_MASK = 1 << 23;
    /**  */
    public static final int MANTISSA_MASK = ( HIDDEN_BIT_MASK - 1 ) << 8;

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
    public static double calcValue( boolean sign, int exponent, int mantissa )
    {
        int actualExponent = exponent - 128;
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
    public static double calcMantissa( int mantissa )
    {
        double numerator = mantissa | HIDDEN_BIT_MASK;
        double denominator = HIDDEN_BIT_MASK << 1;
        return numerator / denominator;
    }

    /***************************************************************************
     * @param mantissa
     * @return
     **************************************************************************/
    public static double calcMantissaScale( int mantissa )
    {
        double fraction = mantissa / ( double )MANTISSA_MASK;

        return 0.5 * ( 1 + fraction );
    }
}
