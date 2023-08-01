package org.jutils.core.data;

/*******************************************************************************
 * Defines a set of functions for identifying and processing bit flags of words.
 ******************************************************************************/
public interface IBitField
{
    /**  */
    public static final long BYTE_MASK = 0xFFL;
    /**  */
    public static final long SHORT_MASK = 0xFFFFL;
    /**  */
    public static final long INT_MASK = 0xFFFFFFFFL;

    /***************************************************************************
     * Returns the 0-relative bit this flag refers to. Bits 0 and 3 are set in
     * the value {@code 0x09}.
     * @return the bit this flag is refers to.
     **************************************************************************/
    public int getStartBit();

    /***************************************************************************
     * Returns the 0-relative bit this flag refers to. Bits 0 and 3 are set in
     * the value {@code 0x09}.
     * @return the bit this flag is refers to.
     **************************************************************************/
    public int getEndBit();

    /***************************************************************************
     * The mask created by shifting the value 0x01, {@link #getBit()} number of
     * times to the left (i.e. {@code 1 << getBit()}.
     * @return the mask for the provided bit.
     * @see #createMask(int)
     **************************************************************************/
    public long getMask();

    /***************************************************************************
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public default boolean getFlag( byte word )
    {
        return getFlag( word & BYTE_MASK );
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public default byte setFlag( byte word, boolean value )
    {
        return ( byte )setFlag( word & BYTE_MASK, value );
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public default boolean getFlag( short word )
    {
        return getFlag( word & SHORT_MASK );
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public default short setFlag( short word, boolean value )
    {
        return ( short )setFlag( word & SHORT_MASK, value );
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public default boolean getFlag( int word )
    {
        return getFlag( word & INT_MASK );
    }

    /***************************************************************************
     * Sets the provided value for this flag in the provided word.
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public default int setFlag( int word, boolean value )
    {
        return ( int )setFlag( word & INT_MASK, value );
    }

    /***************************************************************************
     * Returns {@code true} if this flag is set; {@code false} otherwise.
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public default boolean getFlag( long word )
    {
        return ( word & getMask() ) == getMask();
    }

    /***************************************************************************
     * Sets this flag in the provided word to the provided value.
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public default long setFlag( long word, boolean value )
    {
        if( value )
        {
            word |= getMask();
        }
        else
        {
            word &= ~getMask();
        }

        return word;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public default int size()
    {
        return getEndBit() - getStartBit() + 1;
    }

    /***************************************************************************
     * @param word
     * @return
     **************************************************************************/
    public default byte getField( byte word )
    {
        return ( byte )getField( word & BYTE_MASK );
    }

    /***************************************************************************
     * @param word
     * @param value
     * @return
     **************************************************************************/
    public default byte setField( byte word, byte value )
    {
        return ( byte )setField( word & BYTE_MASK, value );
    }

    /***************************************************************************
     * @param word
     * @return
     **************************************************************************/
    public default short getField( short word )
    {
        return ( short )getField( word & SHORT_MASK );
    }

    /***************************************************************************
     * @param word
     * @param value
     * @return
     **************************************************************************/
    public default short setField( short word, short value )
    {
        return ( short )setField( word & SHORT_MASK, value );
    }

    /***************************************************************************
     * @param word
     * @return
     **************************************************************************/
    public default int getField( int word )
    {
        return ( int )getField( word & INT_MASK );
    }

    /***************************************************************************
     * @param word
     * @param value
     * @return
     **************************************************************************/
    public default int setField( int word, int value )
    {
        return ( int )setField( word & INT_MASK, value );
    }

    /***************************************************************************
     * @param word
     * @return
     **************************************************************************/
    public default long getField( long word )
    {
        return ( word & getMask() ) >>> getStartBit();
    }

    /***************************************************************************
     * @param word
     * @param value
     * @return
     **************************************************************************/
    public default long setField( long word, long value )
    {
        // First clear the mask bits.
        word &= ~getMask();

        // Shift the value to the bit location.
        value = value << getStartBit();

        // Ensure only the mask bits are set in the value.
        value &= getMask();

        // Then set the value bits.
        word |= value;

        return word;
    }

    /***************************************************************************
     * Generates the mask by shifting 1 to the left by the provided number of
     * bits.
     * @param startBit the bit to create a mask for.
     * @param endBit the bit to create a mask for.
     * @return the mask for the provided bit.
     **************************************************************************/
    public static long generateMask( int startBit, int endBit )
    {
        int mask = 0;

        for( int i = startBit; i <= endBit; i++ )
        {
            mask |= ( 1L << i );
        }

        return mask;
    }
}
