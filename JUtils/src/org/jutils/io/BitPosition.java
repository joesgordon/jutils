package org.jutils.io;

/*******************************************************************************
 * Defines the position of a bit in a byte array.
 ******************************************************************************/
public class BitPosition
{
    /** Bit indexes for each throughput index. */
    public static final int [] BIT_INDEXES;
    /** Bit indexes for each throughput index. */
    public static final int [] NEXT_BIT_INDEXES;
    /** Bit indexes for each throughput index. */
    public static final int [] PREV_BIT_INDEXES;
    /** Bit indexes for each throughput index. */
    public static final int [] NEXT_BYTE_INC;
    /** Bit indexes for each throughput index. */
    public static final int [] PREV_BYTE_INC;

    static
    {
        BIT_INDEXES = new int[8];
        NEXT_BIT_INDEXES = new int[8];
        PREV_BIT_INDEXES = new int[8];
        NEXT_BYTE_INC = new int[8];
        PREV_BYTE_INC = new int[8];

        NEXT_BYTE_INC[7] = 1;
        PREV_BYTE_INC[0] = -1;

        for( int i = 0; i < BIT_INDEXES.length; i++ )
        {
            BIT_INDEXES[i] = 7 - i;
            NEXT_BIT_INDEXES[i] = i + 1;
            PREV_BIT_INDEXES[i] = i - 1;
        }

        NEXT_BIT_INDEXES[7] = 0;
        PREV_BIT_INDEXES[0] = 7;
    }

    /** The zero-relative index of the byte. Must be a positive value. */
    private int byteIndex;
    /** The zero-relative index of the bit. Must be between 0-7 inclusive. */
    private int bitIndex;

    /***************************************************************************
     * Creates a new position that points to the first bit of a byte array.
     **************************************************************************/
    public BitPosition()
    {
        reset();
    }

    /***************************************************************************
     * Creates a new position that points to the same bit as the provided
     * position.
     * @param position the position to be copied.
     **************************************************************************/
    public BitPosition( BitPosition position )
    {
        this.byteIndex = position.byteIndex;
        this.bitIndex = position.bitIndex;
    }

    /***************************************************************************
     * Creates a new position that points to the same bit as the provided
     * indexes.
     * @param byteIndex the zero-relative byte index of the position.
     * @param bitIndex the zero-relative bit index of the position.
     **************************************************************************/
    public BitPosition( int byteIndex, int bitIndex )
    {
        this.byteIndex = byteIndex;
        this.bitIndex = bitIndex;
    }

    /***************************************************************************
     * Increments the position to the next bit.
     **************************************************************************/
    public void increment()
    {
        byteIndex += NEXT_BYTE_INC[bitIndex];
        bitIndex = NEXT_BIT_INDEXES[bitIndex];
    }

    /***************************************************************************
     * Increments the position by the specified number of bits.
     * @param bitCount the distance (in bits) to seek.
     **************************************************************************/
    public void increment( int bitCount )
    {
        long bits = byteIndex * 8 + bitIndex + bitCount;

        byteIndex = ( int )( bits / 8 );
        bitIndex = ( int )( bits % 8 );
    }

    /***************************************************************************
     * Returns the byte index of this position.
     * @return the byte index of this position.
     **************************************************************************/
    public int getByte()
    {
        return byteIndex;
    }

    /***************************************************************************
     * Returns the bit index of this position.
     * @return the bit index of this position.
     **************************************************************************/
    public int getBit()
    {
        return bitIndex;
    }

    /***************************************************************************
     * Returns the position in bits.
     * @return the position in bits.
     **************************************************************************/
    public int getPosition()
    {
        return byteIndex * 8 + bitIndex;
    }

    /***************************************************************************
     * Sets this position to the provided position.
     * @param bitpos the bit and byte indexes to be copied.
     **************************************************************************/
    public void set( BitPosition bitpos )
    {
        this.byteIndex = bitpos.byteIndex;
        this.bitIndex = bitpos.bitIndex;
    }

    /***************************************************************************
     * Sets the position to the provided byte and bit indexes.
     * @param byteIndex the byte index (must be > 0).
     * @param bitIndex the bit index (must be 0 - 7 inclusive).
     * @throws IllegalArgumentException if either index is invalid.
     **************************************************************************/
    public void set( int byteIndex, int bitIndex )
        throws IllegalArgumentException
    {
        if( byteIndex < 0 )
        {
            throw new IllegalArgumentException(
                "The byte index must be > -1: " + byteIndex );
        }

        if( bitIndex < 0 || bitIndex > 7 )
        {
            throw new IllegalArgumentException(
                "The bit index must be > -1 and < 8: " + bitIndex );
        }

        this.byteIndex = byteIndex;
        this.bitIndex = bitIndex;
    }

    /***************************************************************************
     * Sets the position to point to the first bit of a byte array.
     **************************************************************************/
    public void reset()
    {
        this.byteIndex = 0;
        this.bitIndex = 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return "" + byteIndex + "[" + bitIndex + "]";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        else if( obj instanceof BitPosition )
        {
            BitPosition pos = ( BitPosition )obj;

            if( pos.byteIndex == byteIndex && pos.bitIndex == bitIndex )
            {
                return true;
            }
        }

        return false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return getPosition();
    }
}
