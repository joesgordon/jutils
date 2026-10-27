package jutils.core.data;

import jutils.core.utils.BitMasks;

/*******************************************************************************
 * Defines a set of functions for identifying and processing bit flags of words.
 ******************************************************************************/
public interface IBitField
{
    /***************************************************************************
     * Returns the 0-relative start bit (inclusive) of the field.
     * @return the start bit.
     **************************************************************************/
    public int getStartBit();

    /***************************************************************************
     * Returns the 0-relative end bit (inclusive) of the field.
     * @return the end bit.
     **************************************************************************/
    public int getEndBit();

    /***************************************************************************
     * The mask created by setting the bits from {@link #getStartBit()} to
     * {@link #getEndBit()}.
     * @return the mask for this field.
     * @see #createMask(int)
     **************************************************************************/
    public long getMask();

    /***************************************************************************
     * @return
     **************************************************************************/
    public default long getMax()
    {
        return getMask() >>> getStartBit();
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public default boolean getFlag( byte word )
    {
        return getFlag( word & BitMasks.BYTE_MASK );
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public default byte setFlag( byte word, boolean value )
    {
        return ( byte )setFlag( word & BitMasks.BYTE_MASK, value );
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public default boolean getFlag( short word )
    {
        return getFlag( word & BitMasks.SHORT_MASK );
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public default short setFlag( short word, boolean value )
    {
        return ( short )setFlag( word & BitMasks.SHORT_MASK, value );
    }

    /***************************************************************************
     * @param word the field that contains this flag.
     * @return {@code true} if this flag is set.
     **************************************************************************/
    public default boolean getFlag( int word )
    {
        return getFlag( word & BitMasks.INT_MASK );
    }

    /***************************************************************************
     * Sets the provided value for this flag in the provided word.
     * @param word the field that contains this flag.
     * @param value the value of the flag.
     * @return the word with this flag {@code set} or {@code cleared}.
     **************************************************************************/
    public default int setFlag( int word, boolean value )
    {
        return ( int )setFlag( word & BitMasks.INT_MASK, value );
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
    public default byte getByte( byte word )
    {
        return ( byte )getField( word & BitMasks.BYTE_MASK );
    }

    /***************************************************************************
     * @param word
     * @return
     **************************************************************************/
    public default byte getByte( short word )
    {
        return ( byte )getField( word & BitMasks.SHORT_MASK );
    }

    /***************************************************************************
     * @param word
     * @return
     **************************************************************************/
    public default byte getByte( int word )
    {
        return ( byte )getField( word & BitMasks.INT_MASK );
    }

    /***************************************************************************
     * @param word
     * @return
     **************************************************************************/
    public default byte getByte( long word )
    {
        return ( byte )getField( word );
    }

    /***************************************************************************
     * @param word
     * @param value
     * @return
     **************************************************************************/
    public default byte setByte( byte word, byte value )
    {
        return ( byte )setField( word & BitMasks.BYTE_MASK, value );
    }

    /***************************************************************************
     * @param word
     * @param value
     * @return
     **************************************************************************/
    public default short setByte( short word, byte value )
    {
        return ( short )setField( word & BitMasks.SHORT_MASK, value );
    }

    /***************************************************************************
     * @param word
     * @param value
     * @return
     **************************************************************************/
    public default int setByte( int word, byte value )
    {
        return ( int )setField( word & BitMasks.INT_MASK, value );
    }

    /***************************************************************************
     * @param word
     * @param value
     * @return
     **************************************************************************/
    public default long setByte( long word, byte value )
    {
        return setField( word, value );
    }

    /***************************************************************************
     * @param word
     * @return
     **************************************************************************/
    public default short getShort( short word )
    {
        return ( short )getField( word & BitMasks.SHORT_MASK );
    }

    /***************************************************************************
     * @param word
     * @param value
     * @return
     **************************************************************************/
    public default short setField( short word, short value )
    {
        return ( short )setField( word & BitMasks.SHORT_MASK, value );
    }

    /***************************************************************************
     * @param word
     * @return
     **************************************************************************/
    public default int getField( int word )
    {
        return ( int )getField( word & BitMasks.INT_MASK );
    }

    /***************************************************************************
     * @param word
     * @param value
     * @return
     **************************************************************************/
    public default int setField( int word, int value )
    {
        return ( int )setField( word & BitMasks.INT_MASK, value );
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
}
