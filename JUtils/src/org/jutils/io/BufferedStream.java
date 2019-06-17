package org.jutils.io;

import java.io.EOFException;
import java.io.IOException;

/*******************************************************************************
 * Defines an {@link IStream} that buffers another {@code IStream}.
 ******************************************************************************/
public class BufferedStream implements IStream
{
    // -------------------------------------------------------------------------
    // Immutable data members.
    // -------------------------------------------------------------------------

    /** The stream to do all actual reads/writes */
    private final IStream stream;
    /** The buffer to be used */
    private final ByteCache buffer;

    // -------------------------------------------------------------------------
    // Mutable data members.
    // -------------------------------------------------------------------------

    /**
     * Flag that specifies whether the buffer should be written ({@code true})
     * on next flush of the streams or not ({@code false}).
     */
    private boolean writeOnNextFlush = false;
    /** The position in this stream of the next read/write. */
    private long position;
    /**  */
    private long streamLen;

    /***************************************************************************
     * Buffers the provided stream with a buffer of
     * {@link ByteCache#DEFAULT_SIZE default size}.
     * @param stream the underlying stream to be buffered.
     **************************************************************************/
    public BufferedStream( IStream stream )
    {
        this( stream, ByteCache.DEFAULT_SIZE );
    }

    /***************************************************************************
     * Buffers the provided stream with a buffer of the provided size.
     * @param stream the underlying stream to be buffered.
     * @param bufSize the size of the buffer.
     **************************************************************************/
    public BufferedStream( IStream stream, int bufSize )
    {
        this.position = 0;
        this.stream = stream;
        this.buffer = new ByteCache( bufSize );
        this.streamLen = -1;
    }

    /***************************************************************************
     * Flushes the buffer to the underlying stream if there is data to be
     * written.
     * @throws IOException If an I/O error occurs during write.
     **************************************************************************/
    public void flush() throws IOException
    {
        if( writeOnNextFlush )
        {
            buffer.writeToStream( stream );
            writeOnNextFlush = false;

            streamLen = getLength();
        }

        // LogUtils.printDebug( "Flushing Stream: Length: %016X", getLength() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte read() throws IOException
    {
        byte b;

        // printDebug( "read-pre" );

        if( getPosition() >= getLength() )
        {
            throw new EOFException( "Tried to read past end of stream" );
        }

        ensureReadCache();

        b = buffer.read();

        position++;

        // printDebug( "read-post" );

        return b;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int read( byte [] buf ) throws IOException
    {
        int len = read( buf, 0, buf.length );

        return len;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int read( byte [] buf, int off, int len ) throws IOException
    {
        int bytesRead = 0;

        // printDebug( "read-pre" );

        // ---------------------------------------------------------------------
        // If the current position is cached and there is enough in the cache.
        // ---------------------------------------------------------------------
        if( buffer.isReadCached( position ) && buffer.remainingRead() > 0 )
        {
            buffer.setPosition( position );

            int toRead = Math.min( buffer.remainingRead(), len );

            buffer.read( buf, off, toRead );

            bytesRead = toRead;
            position += toRead;
        }
        else
        {
            flush();
        }

        if( bytesRead < len )
        {
            if( position != stream.getPosition() )
            {
                stream.seek( position );
            }

            int byteCount = stream.read( buf, off + bytesRead,
                len - bytesRead );

            if( byteCount > -1 )
            {
                position += byteCount;
                bytesRead += byteCount;

                ensureReadCache();
            }
        }

        if( bytesRead == 0 )
        {
            bytesRead = -1;
        }

        // printDebug( "read-post: " + bytesRead );

        return bytesRead;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void readFully( byte [] buf ) throws IOException
    {
        readFully( buf, 0, buf.length );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void readFully( byte [] buf, int off, int len )
        throws EOFException, IOException
    {
        int totalRead = 0;

        if( getAvailable() < len )
        {
            throw new EOFException( "Cannot read " + len +
                " bytes from the stream; only " + getAvailable() +
                " bytes available @ position " + getPosition() +
                " with stream length " + getLength() + "." );
        }

        while( totalRead < len )
        {
            totalRead += read( buf, off + totalRead, len - totalRead );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void seek( long pos ) throws IOException
    {
        if( pos < 0 )
        {
            pos = position - pos;
        }

        if( pos < 0 )
        {
            throw new IOException( "Negative seek offset: " + pos );
        }

        this.position = pos;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        flush();
        stream.close();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void skip( long count ) throws IOException
    {
        seek( getPosition() + count );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getAvailable() throws IOException
    {
        return getLength() - getPosition();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getPosition() throws IOException
    {
        return position;
    }

    /***************************************************************************
     * Returns the virtual length of this stream (i.e. the returned value
     * includes any bytes written past the end of the underlying stream which
     * have not yet been written to disk).
     * @return the virtual length of this stream.
     **************************************************************************/
    @Override
    public long getLength() throws IOException
    {
        long len = buffer.getPosition() + buffer.getReadLength();

        if( streamLen < 0 )
        {
            streamLen = stream.getLength();
        }

        return Math.max( len, streamLen );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( byte b ) throws IOException
    {
        // ---------------------------------------------------------------------
        // If cached, write and set flush-write flag.
        // ---------------------------------------------------------------------
        if( buffer.isWriteCached( position ) )
        {
            buffer.write( b );
            position++;
            writeOnNextFlush = true;
        }
        else
        {
            // -----------------------------------------------------------------
            // Otherwise, write buffer, then directly to the stream and cache
            // afterwards.
            // -----------------------------------------------------------------
            if( writeOnNextFlush )
            {
                flush();
            }

            stream.seek( position );
            stream.write( b );
            position++;
            buffer.readFromStream( stream );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( byte [] buf ) throws IOException
    {
        write( buf, 0, buf.length );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( byte [] buf, int off, int len ) throws IOException
    {
        if( buffer.isWriteCached( position ) )
        {
            if( len > buffer.remainingWrite() )
            {
                int nextWrite = len - buffer.remainingWrite();
                int nextOff = off + buffer.remainingWrite();

                if( writeOnNextFlush )
                {
                    buffer.write( buf, off, buffer.remainingWrite() );
                    flush();
                }

                if( nextWrite > 0 )
                {
                    stream.write( buf, nextOff, nextWrite );
                }

                position += len;

                loadBufferFromFile( position );
            }
            else
            {
                buffer.write( buf, off, len );
                writeOnNextFlush = true;

                position += len;
            }
        }
        else
        {
            // -----------------------------------------------------------------
            // Otherwise, write directly to the stream and cache afterwards.
            // -----------------------------------------------------------------
            flush();

            stream.seek( position );
            stream.write( buf, off, len );
            position += len;

            loadBufferFromFile( position );
        }
    }

    /***************************************************************************
     * Write a little info about the state of the stream along with the provided
     * message.
     * @param msg the message to be written.
     **************************************************************************/
    protected void printDebug( String msg )
    {
        LogUtils.printDebug( "----------------------------" );
        LogUtils.printDebug( "- " + msg );
        LogUtils.printDebug( "----------------------------" );
        buffer.printDebug();
        LogUtils.printDebug( "" );
    }

    /***************************************************************************
     * Flushes and then loads the buffer starting at the provided position into
     * the stream.
     * @param pos the position into the underlying stream at which the buffer
     * will be loaded.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    private void loadBufferFromFile( long pos ) throws IOException
    {
        // printDebug( "pre-load" );

        flush();

        buffer.readFromStream( stream, pos );

        // printDebug( "post-load" );
    }

    /***************************************************************************
     * Ensures that the current position is cached to be read.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    private void ensureReadCache() throws IOException
    {
        long pos = getPosition();

        if( !buffer.isReadCached( pos ) )
        {
            loadBufferFromFile( pos );
        }
        else
        {
            buffer.setPosition( pos );
        }
    }
}
