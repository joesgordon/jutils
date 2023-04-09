package org.jutils.core.data;

import org.jutils.core.INamedItem;

/*******************************************************************************
 * Defines a set of functions for identifying and processing bit flags of words.
 ******************************************************************************/
public interface IBitFlag extends INamedItem
{
    /***************************************************************************
     * Returns the 0-relative bit this flag refers to. Bits 0 and 3 are set in
     * the value {@code 0x09}.
     * @return the bit this flag is refers to.
     **************************************************************************/
    public int getBit();

    /***************************************************************************
     * The mask created by shifting the value 0x01, {@link #getBit()} number of
     * times to the left (i.e. {@code 1 << getBit()}.
     * @return the mask for the provided bit.
     * @see #createMask(int)
     **************************************************************************/
    public long getMask();

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName();

    /***************************************************************************
     * Calculates the mask by shifting 1 to the left by the provided number of
     * bits.
     * @param bit the bit to create a mask for.
     * @return the mask for the provided bit.
     **************************************************************************/
    public static long createMask( int bit )
    {
        return 1L << bit;
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
     * Returns {@code true} if this flag is set; {@code false} otherwise.
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public default boolean getFlag( long word )
    {
        long mask = getMask();
        return ( word & mask ) == mask;
    }

    /***************************************************************************
     * Sets the provided value for this flag in the provided word.
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public default int setFlag( int word, boolean value )
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
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public default boolean getFlag( int word )
    {
        long lword = word & 0xFFFFFFFFL;
        return getFlag( lword );
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public default short setFlag( short word, boolean value )
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
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public default boolean getFlag( short word )
    {
        long lword = word & 0xFFFFL;
        return getFlag( lword );
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public default long setFlag( byte word, boolean value )
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
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public default boolean getFlag( byte word )
    {
        long lword = word & 0xFFL;
        return getFlag( lword );
    }
}
