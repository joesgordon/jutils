package org.jutils.io;

import java.io.EOFException;
import java.io.IOException;

import org.jutils.Utils;

/*******************************************************************************
 * Wraps an {@link IStream} to provide a buffered read-only stream.
 ******************************************************************************/
public class BufferedReadOnlyStream implements IStream
{
    /** The wrapped stream. */
    private final IStream stream;
    /** The stream buffer. */
    private final byte [] buffer;

    /** The current read index into the buffer. */
    private int index;
    /** The number of bytes used in the buffer. */
    private int fillCount;
    /** The position of the buffer in the underlying stream. */
    private long position;
    /** The cached length of the underlying stream. */
    private Long length;

    /***************************************************************************
     * Creates a new buffered stream with the provided underlying stream using a
     * buffer of {@link IOUtils#DEFAULT_BUF_SIZE} size.
     * @param stream the underlying stream.
     **************************************************************************/
    public BufferedReadOnlyStream( IStream stream )
    {
        this( stream, IOUtils.DEFAULT_BUF_SIZE );
    }

    /***************************************************************************
     * Creates a new buffered stream with the provided underlying stream using a
     * buffer of the provided size.
     * @param stream the underlying stream.
     * @param bufferSize the size of this stream's buffer.
     **************************************************************************/
    public BufferedReadOnlyStream( IStream stream, int bufferSize )
    {
        this.stream = stream;
        this.buffer = new byte[bufferSize];

        this.index = 0;
        this.fillCount = 0;
        this.position = 0;
        this.length = null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte read() throws EOFException, IOException
    {
        if( index < fillCount )
        {
            return buffer[index++];
        }
        else if( getAvailable() < 1 )
        {
            throw new EOFException();
        }

        seek( position + index );

        return buffer[index++];
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte [] buf ) throws IOException
    {
        return read( buf, 0, buf.length );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte [] buf ) throws EOFException, IOException
    {
        readFully( buf, 0, buf.length );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte [] buf, int off, int len ) throws IOException
    {
        int bufAvailable = fillCount - index;
        int totalRead = 0;
        int toCopy;

        // ---------------------------------------------------------------------
        // Read from the available buffer if possible.
        // ---------------------------------------------------------------------
        if( bufAvailable > 0 )
        {
            toCopy = Math.min( len - totalRead, bufAvailable );
            Utils.byteArrayCopy( buffer, index, buf, off, toCopy );

            totalRead += toCopy;
            this.index += toCopy;
        }

        // ---------------------------------------------------------------------
        // Read directly from the base stream if needed. Cache next afterwards.
        // ---------------------------------------------------------------------
        if( totalRead < len )
        {
            toCopy = stream.read( buf, off + totalRead, len - totalRead );

            if( toCopy > -1 )
            {
                totalRead += toCopy;

                try
                {
                    fillBuffer( stream.getPosition() );
                }
                catch( EOFException ex )
                {
                    ;
                }
            }
        }

        return totalRead;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte [] buf, int off, int len )
        throws EOFException, IOException
    {
        int bytesRead = 0;

        if( getAvailable() < len )
        {
            throw new EOFException(
                "Cannot read " + len + " bytes from the stream; only " +
                    getAvailable() + " bytes available." );
        }

        while( bytesRead < len )
        {
            bytesRead += read( buf, off + bytesRead, len - bytesRead );
        }
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
    public void seek( long pos ) throws IOException
    {
        if( pos >= this.position && pos < ( this.position + this.fillCount ) )
        {
            this.index = ( int )( pos - position );
        }
        else if( pos < getLength() )
        {
            fillBuffer( pos );
        }
        else
        {
            this.position = getLength();
            this.index = 0;
            this.fillCount = 0;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void skip( long count ) throws IOException
    {
        seek( getPosition() + count );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getAvailable() throws IOException
    {
        return getLength() - getPosition();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getPosition() throws IOException
    {
        return position + index;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getLength() throws IOException
    {
        long len;

        try
        {
            len = length.longValue();
        }
        catch( NullPointerException ex )
        {
            len = stream.getLength();
            this.length = len;
        }

        return len;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte b ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte [] buf ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte [] buf, int off, int len ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * Reads bytes from the underlying stream into the buffer starting at the
     * provided position.
     * @param position the position in the underlying stream at which the read
     * will start.
     * @throws EOFException if no bytes were read into the buffer.
     * @throws IOException any I/O error that occurs.
     **************************************************************************/
    private void fillBuffer( long position ) throws EOFException, IOException
    {
        // LogUtils.printDebug( "Filling buffer @ %X", position );
        stream.seek( position );

        this.position = position;
        this.index = 0;
        this.fillCount = stream.read( buffer, 0, buffer.length );

        if( fillCount < 1 )
        {
            throw new EOFException();
        }
    }
}
