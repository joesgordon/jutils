package jutils.core.io;

import jutils.core.utils.BitMasks;

/*******************************************************************************
 * Defines a set of methods to read/write a series of bits comprising a
 * sub-field from/to a byte, short, int, or long.
 ******************************************************************************/
public class BitsReader
{
    /** The mask for the bits of the sub-field. */
    private final long mask;
    /** The number of bytes to shift the sub-field down to bit zero. */
    private final long shift;

    /***************************************************************************
     * Creates a reader that reads the bits out of an integer from the start to
     * the end bits.
     * @param start inclusive start bit.
     * @param end inclusive end bit.
     * @throws IllegalArgumentException if <code>start < 0, end < 0, end <=
     * start, or end >= {@link Long#SIZE}<code>.
     **************************************************************************/
    public BitsReader( int start, int end )
    {
        if( end < start )
        {
            throw new IllegalArgumentException(
                "End index must be greater than or equal to the start index" );
        }

        if( end >= Long.SIZE )
        {
            throw new IllegalArgumentException(
                "End index cannot be greater than " + ( Long.SIZE - 1 ) );
        }

        int len = end - start + 1;

        long mask = BitMasks.getFieldMask( len );
        int shift = start;

        this.mask = mask << shift;
        this.shift = shift;

    }

    /***************************************************************************
     * Reads the sub-field from the provided value.
     * @param value the field containing the sub-field.
     * @return the sub-field read.
     **************************************************************************/
    public byte read( byte value )
    {
        return ( byte )( ( value & mask ) >>> shift );
    }

    /***************************************************************************
     * Reads the sub-field from the provided value.
     * @param value the field containing the sub-field.
     * @return the sub-field read.
     **************************************************************************/
    public short read( short value )
    {
        return ( short )( ( value & mask ) >>> shift );
    }

    /***************************************************************************
     * Reads the sub-field from the provided value.
     * @param value the field containing the sub-field.
     * @return the sub-field read.
     **************************************************************************/
    public int read( int value )
    {
        return ( int )( ( value & mask ) >>> shift );
    }

    /***************************************************************************
     * Reads the sub-field from the provided value.
     * @param value the field containing the sub-field.
     * @return the sub-field read.
     **************************************************************************/
    public long read( long value )
    {
        return ( value & mask ) >>> shift;
    }

    /***************************************************************************
     * @param value the value of the sub-field to be written.
     * @param dest the destination of the value to be written.
     * @return the value written to {@code dest}.
     **************************************************************************/
    public long write( long value, long dest )
    {
        long result = dest & ~mask;
        return ( result | ( value << shift ) );
    }

    /***************************************************************************
     * @param value the value of the sub-field to be written.
     * @param dest the destination of the value to be written.
     * @return the value written to {@code dest}.
     **************************************************************************/
    public long write( int value, long dest )
    {
        long result = dest & ~mask;
        return ( result | ( value << shift ) );
    }

    /***************************************************************************
     * @param value the value of the sub-field to be written.
     * @param dest the destination of the value to be written.
     * @return the value written to {@code dest}.
     **************************************************************************/
    public long write( short value, long dest )
    {
        long result = dest & ~mask;
        return ( result | ( value << shift ) );
    }

    /***************************************************************************
     * @param value the value of the sub-field to be written.
     * @param dest the destination of the value to be written.
     * @return the value written to {@code dest}.
     **************************************************************************/
    public long write( byte value, long dest )
    {
        long result = dest & ~mask;
        return ( result | ( value << shift ) );
    }

    /***************************************************************************
     * @param value the value of the sub-field to be written.
     * @param dest the destination of the value to be written.
     * @return the value written to {@code dest}.
     **************************************************************************/
    public int write( int value, int dest )
    {
        int result = ( int )( dest & ~mask );
        return ( result | ( value << shift ) );
    }

    /***************************************************************************
     * @param value the value of the sub-field to be written.
     * @param dest the destination of the value to be written.
     * @return the value written to {@code dest}.
     **************************************************************************/
    public int write( short value, int dest )
    {
        int result = ( int )( dest & ~mask );
        return ( result | ( value << shift ) );
    }

    /***************************************************************************
     * @param value the value of the sub-field to be written.
     * @param dest the destination of the value to be written.
     * @return the value written to {@code dest}.
     **************************************************************************/
    public int write( byte value, int dest )
    {
        int result = ( int )( dest & ~mask );
        return ( result | ( value << shift ) );
    }

    /***************************************************************************
     * @param value the value of the sub-field to be written.
     * @param dest the destination of the value to be written.
     * @return the value written to {@code dest}.
     **************************************************************************/
    public short write( short value, short dest )
    {
        short result = ( short )( dest & ~mask );
        return ( short )( result | ( value << shift ) );
    }

    /***************************************************************************
     * @param value the value of the sub-field to be written.
     * @param dest the destination of the value to be written.
     * @return the value written to {@code dest}.
     **************************************************************************/
    public short write( byte value, short dest )
    {
        short result = ( short )( dest & ~mask );
        return ( short )( result | ( value << shift ) );
    }

    /***************************************************************************
     * @param value the value of the sub-field to be written.
     * @param dest the destination of the value to be written.
     * @return the value written to {@code dest}.
     **************************************************************************/
    public byte write( byte value, byte dest )
    {
        byte result = ( byte )( dest & ~mask );
        return ( byte )( result | ( value << shift ) );
    }

    /***************************************************************************
     * @return
     * @see Long#SIZE
     **************************************************************************/
    public static int getMaskCount()
    {
        return Long.SIZE;
    }
}
