package jutils.core.io;

import java.nio.ByteBuffer;

import jutils.core.Utils;
import jutils.core.utils.ByteOrdering;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataBuffer
{
    /** The bytes that seed the buffer. */
    public final byte [] bytes;
    /** Buffer to be used for intrinsic reading/writing. */
    private final ByteBuffer buffer;

    /** The byte order used for reading/writing. */
    private ByteOrdering order;

    /***************************************************************************
     * Creates a {@link ByteOrdering#BIG_ENDIAN} data buffer.
     **************************************************************************/
    public DataBuffer()
    {
        this( ByteOrdering.BIG_ENDIAN );
    }

    /***************************************************************************
     * Creates a data buffer of the provided byte ordering.
     * @param order the byte ordering used to interpret bytes.
     **************************************************************************/
    public DataBuffer( ByteOrdering order )
    {
        this( order, 8 );
    }

    /***************************************************************************
     * @param order
     * @param size
     **************************************************************************/
    public DataBuffer( ByteOrdering order, int size )
    {
        this.bytes = new byte[size];
        this.buffer = ByteBuffer.wrap( bytes );

        setOrder( order );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ByteOrdering getOrder()
    {
        return order;
    }

    /***************************************************************************
     * @param order
     **************************************************************************/
    public void setOrder( ByteOrdering order )
    {
        this.order = order;
        buffer.order( order.order );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        buffer.rewind();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public short readShort()
    {
        buffer.rewind();
        return buffer.getShort();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int readInt()
    {
        buffer.rewind();
        return buffer.getInt();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long readLong()
    {
        buffer.rewind();
        return buffer.getLong();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public float readFloat()
    {
        buffer.rewind();
        return buffer.getFloat();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double readDouble()
    {
        buffer.rewind();
        return buffer.getDouble();
    }

    /***************************************************************************
     * @param v
     **************************************************************************/
    public void writeShort( short v )
    {
        buffer.rewind();
        buffer.putShort( v );
    }

    /***************************************************************************
     * @param v
     **************************************************************************/
    public void writeInt( int v )
    {
        buffer.rewind();
        buffer.putInt( v );
    }

    /***************************************************************************
     * @param v
     **************************************************************************/
    public void writeLong( long v )
    {
        buffer.rewind();
        buffer.putLong( v );
    }

    /***************************************************************************
     * @param v
     **************************************************************************/
    public void writeFloat( float v )
    {
        buffer.rewind();
        buffer.putFloat( v );
    }

    /***************************************************************************
     * @param v
     **************************************************************************/
    public void writeDouble( double v )
    {
        buffer.rewind();
        buffer.putDouble( v );
    }

    /***************************************************************************
     * @param bytes
     **************************************************************************/
    public void write( byte [] bytes )
    {
        writeAt( bytes, 0 );
    }

    /***************************************************************************
     * @param bytes
     * @param position
     **************************************************************************/
    public void writeAt( byte [] bytes, int position )
    {
        int avail = this.bytes.length - position;
        int length = Math.min( bytes.length, avail );
        Utils.byteArrayCopy( bytes, 0, this.bytes, position, length );
    }
}
