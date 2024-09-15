package jutils.core.utils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitMasks
{
    /** Masks needed to set a particular bit. */
    private static final long [] SET_MASKS;
    /** Masks needed to clear a particular bit. */
    private static final long [] CLEAR_MASKS;
    /** Masks needed to read a field of a particular size. */
    private static final long [] FIELD_MASKS;

    /**  */
    public static final long BYTE_MASK;
    /**  */
    public static final long SHORT_MASK;
    /**  */
    public static final long INT_MASK;

    static
    {
        SET_MASKS = new long[Long.SIZE];
        CLEAR_MASKS = new long[Long.SIZE];
        FIELD_MASKS = new long[Long.SIZE];

        long field = 1;

        for( int m = 0; m < FIELD_MASKS.length; m++ )
        {
            FIELD_MASKS[m] = field;
            SET_MASKS[m] = 1 << m;
            CLEAR_MASKS[m] = ~SET_MASKS[m];

            field = ( field << 1 ) | 1;
        }

        BYTE_MASK = FIELD_MASKS[7];
        SHORT_MASK = FIELD_MASKS[15];
        INT_MASK = FIELD_MASKS[31];
    }

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private BitMasks()
    {
    }

    /***************************************************************************
     * @param bit
     * @return
     **************************************************************************/
    public static long getBitMask( int bit )
    {
        return SET_MASKS[bit];
    }

    /***************************************************************************
     * @param bit
     * @return
     **************************************************************************/
    public static long getBitClearMask( int bit )
    {
        return CLEAR_MASKS[bit];
    }

    /***************************************************************************
     * Returns the mask needed to remove the upper bits for values up to the
     * provided maximum value.
     * @param maxValue the value for which the mask will be generated.
     * @return the generated mask.
     **************************************************************************/
    public static int getMaskForValue( int maxValue )
    {
        int v = maxValue--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        // v++;

        return v;
    }

    /***************************************************************************
     * @param bit
     * @param count
     * @return
     **************************************************************************/
    public static long getFieldMask( int bit, int count )
    {
        long mask = getFieldMask( count );

        return mask << bit;
    }

    /***************************************************************************
     * @param len
     * @return
     **************************************************************************/
    public static long getFieldMask( int len )
    {
        return FIELD_MASKS[len - 1];
    }
}
