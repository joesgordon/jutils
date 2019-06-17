package org.jutils.io;

import java.io.EOFException;
import java.io.IOException;

import org.jutils.utils.ByteOrdering;

/*******************************************************************************
 * Implements a stream that reads/writes intrinsic data types.
 ******************************************************************************/
public class DataStream implements IDataStream
{
    /** The underlying stream. */
    private final IStream stream;
    /** Buffer to convert data to bytes. */
    private final DataBuffer buffer;

    /***************************************************************************
     * Creates a data stream that reads data with big endian byte ordering.
     * @param stream the stream to be wrapped as a data stream.
     **************************************************************************/
    public DataStream( IStream stream )
    {
        this( stream, ByteOrdering.BIG_ENDIAN );
    }

    /***************************************************************************
     * Creates a data stream that reads data with the provided byte ordering.
     * @param stream the stream to be wrapped as a data stream.
     * @param order the byte order of data in the stream.
     **************************************************************************/
    public DataStream( IStream stream, ByteOrdering order )
    {
        this.stream = stream;
        this.buffer = new DataBuffer( order );
    }

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // IStream specific methods.
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte read() throws EOFException, IOException
    {
        return stream.read();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte [] buf ) throws IOException
    {
        return stream.read( buf );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte [] buf ) throws EOFException, IOException
    {
        stream.readFully( buf );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte [] buf, int off, int len ) throws IOException
    {
        return stream.read( buf, off, len );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte [] buf, int off, int len )
        throws EOFException, IOException
    {
        stream.readFully( buf, off, len );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void seek( long pos ) throws IOException
    {
        stream.seek( pos );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        stream.close();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getPosition() throws IOException
    {
        return stream.getPosition();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getLength() throws IOException
    {
        return stream.getLength();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte b ) throws IOException
    {
        stream.write( b );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte [] buf ) throws IOException
    {
        stream.write( buf );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte [] buf, int off, int len ) throws IOException
    {
        stream.write( buf, off, len );
    }

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // IDataStream specific methods.
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean readBoolean() throws IOException
    {
        return read() != 0;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public short readShort() throws IOException
    {
        readFully( buffer.bytes, 0, 2 );
        return buffer.readShort();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int readInt() throws IOException
    {
        readFully( buffer.bytes, 0, 4 );

        return buffer.readInt();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long readLong() throws IOException
    {
        readFully( buffer.bytes, 0, 8 );
        return buffer.readLong();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public float readFloat() throws IOException
    {
        readFully( buffer.bytes, 0, 4 );
        return buffer.readFloat();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double readDouble() throws IOException
    {
        readFully( buffer.bytes, 0, 8 );
        return buffer.readDouble();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeBoolean( boolean v ) throws IOException
    {
        write( ( byte )( v ? 1 : 0 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeShort( short v ) throws IOException
    {
        buffer.writeShort( v );
        write( buffer.bytes, 0, 2 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeInt( int v ) throws IOException
    {
        buffer.writeInt( v );
        write( buffer.bytes, 0, 4 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeLong( long v ) throws IOException
    {
        buffer.writeLong( v );
        write( buffer.bytes, 0, 8 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeFloat( float v ) throws IOException
    {
        buffer.writeFloat( v );
        write( buffer.bytes, 0, 4 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeDouble( double v ) throws IOException
    {
        buffer.writeDouble( v );
        write( buffer.bytes, 0, 8 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void skip( long count ) throws IOException
    {
        stream.skip( count );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getAvailable() throws IOException
    {
        return stream.getAvailable();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ByteOrdering getOrder()
    {
        return buffer.getOrder();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setOrder( ByteOrdering ordering )
    {
        buffer.setOrder( ordering );
    }
}
