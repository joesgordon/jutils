package org.jutils.core.data;

/*******************************************************************************
 * Class for supporting {@link IBitFlag}.
 ******************************************************************************/
public class BitFlagInfo
{
    /** The 0-relative bit this flag refers to. */
    public final int bit;
    /**
     * The mask created by shifting the value 0x01, {@link #bit} number of times
     * to the left
     */
    public final long mask;
    /** The name of this item. */
    public final String name;

    /***************************************************************************
     * Creates a new structure for supporting {@link IBitFlag}.
     * @param bit the bit to create a mask for.
     * @param name the name of this bit flag.
     **************************************************************************/
    public BitFlagInfo( int bit, String name )
    {
        this.bit = bit;
        this.mask = generateMask( bit );
        this.name = name;
    }

    /***************************************************************************
     * Sets this flag in the provided word to the provided value.
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public long setFlag( long word, boolean value )
    {
        if( value )
        {
            word |= mask;
        }
        else
        {
            word &= ~mask;
        }

        return word;
    }

    /***************************************************************************
     * Returns {@code true} if this flag is set; {@code false} otherwise.
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public boolean getFlag( long word )
    {
        return ( word & mask ) == mask;
    }

    /***************************************************************************
     * Sets the provided value for this flag in the provided word.
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public int setFlag( int word, boolean value )
    {
        if( value )
        {
            word |= mask;
        }
        else
        {
            word &= ~mask;
        }

        return word;
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public boolean getFlag( int word )
    {
        long lword = word & 0xFFFFFFFFL;
        return getFlag( lword );
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public short setFlag( short word, boolean value )
    {
        if( value )
        {
            word |= mask;
        }
        else
        {
            word &= ~mask;
        }

        return word;
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public boolean getFlag( short word )
    {
        long lword = word & 0xFFFFL;
        return getFlag( lword );
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public long setFlag( byte word, boolean value )
    {
        if( value )
        {
            word |= mask;
        }
        else
        {
            word &= ~mask;
        }

        return word;
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public boolean getFlag( byte word )
    {
        long lword = word & 0xFFL;
        return getFlag( lword );
    }

    /***************************************************************************
     * Generates the mask by shifting 1 to the left by the provided number of
     * bits.
     * @param bit the bit to create a mask for.
     * @return the mask for the provided bit.
     **************************************************************************/
    public static long generateMask( int bit )
    {
        return 1L << bit;
    }
}
