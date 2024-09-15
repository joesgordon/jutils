package jutils.core.io;

import jutils.core.utils.BitMasks;

/*******************************************************************************
 * Creates a reader to return a bit flag from byte, short, int, or long values.
 ******************************************************************************/
public class BitReader
{
    /** The mask for the desired bit. */
    private final long mask;

    /***************************************************************************
     * Creates a new reader with the provided bit.
     * @param bit the bit to be read.
     * @throws IllegalArgumentException - if
     * {@code bit >= BitMasks.SET_MASKS.length}
     * @see BitMasks#SET_MASKS
     **************************************************************************/
    public BitReader( int bit )
    {
        if( bit >= Long.SIZE )
        {
            throw new IllegalArgumentException(
                "BitReader can read up to only " + Long.SIZE + "-bits" );
        }

        this.mask = BitMasks.getBitMask( bit );
    }

    /***************************************************************************
     * Reads the bit from the provided value.
     * @param value the field containing the bit flag.
     * @return the boolean representation of the bit.
     **************************************************************************/
    public boolean read( byte value )
    {
        return ( value & mask ) == mask;
    }

    /***************************************************************************
     * Reads the bit from the provided value.
     * @param value the field containing the bit flag.
     * @return the boolean representation of the bit.
     **************************************************************************/
    public boolean read( short value )
    {
        return ( value & mask ) == mask;
    }

    /***************************************************************************
     * Reads the bit from the provided value.
     * @param value the field containing the bit flag.
     * @return the boolean representation of the bit.
     **************************************************************************/
    public boolean read( int value )
    {
        return ( value & mask ) == mask;
    }

    /***************************************************************************
     * Reads the bit from the provided value.
     * @param value the field containing the bit flag.
     * @return the boolean representation of the bit.
     **************************************************************************/
    public boolean read( long value )
    {
        return ( value & mask ) == mask;
    }

    /***************************************************************************
     * Writes the provided bit to the provided value at the position indicated
     * upon construction.
     * @param bit the value to be written ({@code true} indicates 0b1).
     * @param value the field to be written to.
     * @return the provided value with the bit written.
     **************************************************************************/
    public byte write( boolean bit, byte value )
    {
        if( bit )
        {
            value |= mask;
        }
        else
        {
            value &= ~mask;
        }

        return value;
    }

    /***************************************************************************
     * Writes the provided bit to the provided value at the position indicated
     * upon construction.
     * @param bit the value to be written ({@code true} indicates 0b1).
     * @param value the field to be written to.
     * @return the provided value with the bit written.
     **************************************************************************/
    public short write( boolean bit, short value )
    {
        if( bit )
        {
            value |= mask;
        }
        else
        {
            value &= ~mask;
        }

        return value;
    }

    /***************************************************************************
     * Writes the provided bit to the provided value at the position indicated
     * upon construction.
     * @param bit the value to be written ({@code true} indicates 0b1).
     * @param value the field to be written to.
     * @return the provided value with the bit written.
     **************************************************************************/
    public int write( boolean bit, int value )
    {
        if( bit )
        {
            value |= mask;
        }
        else
        {
            value &= ~mask;
        }

        return value;
    }

    /***************************************************************************
     * Writes the provided bit to the provided value at the position indicated
     * upon construction.
     * @param bit the value to be written ({@code true} indicates 0b1).
     * @param value the field to be written to.
     * @return the provided value with the bit written.
     **************************************************************************/
    public long write( boolean bit, long value )
    {
        if( bit )
        {
            value |= mask;
        }
        else
        {
            value &= ~mask;
        }

        return value;
    }
}
