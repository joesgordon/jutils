package org.jutils.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/*******************************************************************************
 * Defines an output stream that writes data in a little-endian fashion.
 ******************************************************************************/
public class LeOutputStream implements DataOutput
{
    /** The parent stream to write data in a little-endian fashion. */
    private final OutputStream out;
    /** The bytes to buffer data writes. */
    private final byte [] bytes;
    /** The byte wrapper to write/read data. */
    private final ByteBuffer buffer;

    /***************************************************************************
     * Creates a
     * @param out the stream to be written to.
     **************************************************************************/
    public LeOutputStream( OutputStream out )
    {
        this.out = out;

        this.bytes = new byte[8];
        this.buffer = ByteBuffer.wrap( bytes );

        buffer.order( ByteOrder.LITTLE_ENDIAN );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeBoolean( boolean v ) throws IOException
    {
        out.write( v ? 1 : 0 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeByte( int v ) throws IOException
    {
        out.write( v );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeShort( int v ) throws IOException
    {
        buffer.rewind();
        buffer.putShort( ( short )v );

        out.write( bytes, 0, 2 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeInt( int v ) throws IOException
    {
        buffer.rewind();
        buffer.putInt( v );

        out.write( bytes, 0, 4 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeLong( long v ) throws IOException
    {
        buffer.rewind();
        buffer.putLong( v );
        buffer.get( bytes );

        out.write( bytes, 0, 8 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeFloat( float v ) throws IOException
    {
        buffer.rewind();
        buffer.putFloat( v );

        out.write( bytes, 0, 4 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeDouble( double v ) throws IOException
    {
        buffer.rewind();
        buffer.putDouble( v );

        out.write( bytes, 0, 8 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeBytes( String s ) throws IOException
    {
        out.write( s.getBytes( IOUtils.US_ASCII ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeChar( int v ) throws IOException
    {
        throw new RuntimeException( "Function not implemented" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeChars( String s ) throws IOException
    {
        throw new RuntimeException( "Function not implemented" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeUTF( String s ) throws IOException
    {
        throw new RuntimeException( "Function not implemented" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( int b ) throws IOException
    {
        out.write( b );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte [] b ) throws IOException
    {
        out.write( b );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte [] b, int off, int len ) throws IOException
    {
        out.write( b, off, len );
    }
}
