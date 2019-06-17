package org.jutils.io;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.jutils.utils.ByteOrdering;

/*******************************************************************************
 * According to <a
 * href="http://stackoverflow.com/a/5623638/1741">stackoverflow</a> and several
 * other sources, a {@link RandomAccessFile} should be buffered using a
 * memory-mapped file via {@link FileChannel#map(MapMode, long, long)}. This is
 * an implementation of said buffering.</p> <h1><b>WARNING</b></h1> Do not use
 * this class if you intend to delete the file to be mapped. See <a
 * href="http://bugs.java.com/view_bug.do?bug_id=4715154">bug JDK-4715154</a>.
 * You will be unable to delete any file that has been mapped. The close
 * operation will set the local {@link MappedByteBuffer} (JavaDoc specifies that
 * the mapping remains valid until it is garbage collected) to {@code null} and
 * call {@link System#gc()} in an attempt to garbage collect the mapped buffer
 * prior to any delete call that is made. This is, of course, not garenteed to
 * work since the garbage collection call is only a request and therefore it
 * cannot be ensured that the buffer has been garbage collected prior to the
 * delete call.
 ******************************************************************************/
public class MappedStream implements IDataStream
{
    /** The channel used for buffering. */
    private final FileChannel channel;
    /** The primary I/O device. */
    private final RandomAccessFile raf;
    /** The mapping mode used. */
    private final MapMode mode;
    /** The size of the buffer. */
    private final int bufferSize;
    /** The ordering of bytes in the stream. */
    private ByteOrdering order;

    // TODO do not cache the length of the stream if this is to support writing.
    /** The cached length of the stream. */
    private long length;
    /** The position of the buffer loaded. */
    private long bufferPos;
    /** The stream buffer. */
    private MappedByteBuffer buffer;

    /***************************************************************************
     * Creates a new writable, big endian stream with the default buffer size.
     * @param file the file to be accessed.
     * @throws FileNotFoundException if the given file does not exist and cannot
     * be created or if some other error occurs while opening or creating the
     * file.
     **************************************************************************/
    public MappedStream( File file ) throws FileNotFoundException
    {
        this( file, false );
    }

    /***************************************************************************
     * Creates a new big endian stream with the default buffer size that is read
     * only according the the provided parameter.
     * @param file the file to be accessed.
     * @param readOnly opens the file as read-only if {@code true}.
     * @throws FileNotFoundException if the given file does not exist, or if the
     * read only flag is false and the given file object does not denote an
     * existing, writable file and a new file of that name cannot be created, or
     * if some other error occurs while opening or creating the file.
     **************************************************************************/
    public MappedStream( File file, boolean readOnly )
        throws FileNotFoundException
    {
        this( file, readOnly, ByteOrdering.BIG_ENDIAN );
    }

    /***************************************************************************
     * Creates a new stream with the default buffer size that is read only
     * according the the provided parameter and reads/writes data according to
     * the provided ordering.
     * @param file the file to be accessed.
     * @param readOnly opens the file as read-only if {@code true}.
     * @param order the byte order of data in the stream.
     * @throws FileNotFoundException if the given file does not exist, or if the
     * read only flag is false and the given file object does not denote an
     * existing, writable file and a new file of that name cannot be created, or
     * if some other error occurs while opening or creating the file.
     **************************************************************************/
    public MappedStream( File file, boolean readOnly, ByteOrdering order )
        throws FileNotFoundException
    {
        this( file, readOnly, order, IOUtils.DEFAULT_BUF_SIZE );
    }

    /***************************************************************************
     * Creates a new stream with the provided buffer size that is read only
     * according the the provided parameter and reads/writes data according to
     * the provided ordering.
     * @param file the file to be accessed.
     * @param readOnly opens the file as read-only if {@code true}.
     * @param order the byte order of data in the stream.
     * @param bufferSize the length of the buffer to be used in bytes.
     * @throws FileNotFoundException if the given file does not exist, or if the
     * read only flag is false and the given file object does not denote an
     * existing, writable file and a new file of that name cannot be created, or
     * if some other error occurs while opening or creating the file.
     **************************************************************************/
    public MappedStream( File file, boolean readOnly, ByteOrdering order,
        int bufferSize ) throws FileNotFoundException
    {
        if( bufferSize < 1024 )
        {
            bufferSize = 1024;
        }

        if( !readOnly )
        {
            throw new IllegalArgumentException(
                "Writing is not yet supported." );
        }

        this.raf = new RandomAccessFile( file, "r" );
        this.channel = raf.getChannel();
        this.mode = readOnly ? MapMode.READ_ONLY : MapMode.READ_WRITE;
        this.order = order;
        this.bufferSize = bufferSize;

        this.buffer = null;
        this.length = -1;
        this.bufferPos = -1;
    }

    /***************************************************************************
     * Loads the buffer with data from the provided position.
     * @param position the location at which to load data.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    private void readBuffer( long position ) throws IOException
    {
        int size = bufferSize;

        if( position > -1 && position < getLength() )
        {
            if( getAvailable() < size )
            {
                size = ( int )( getAvailable() );
            }
        }
        else
        {
            throw new EOFException( "End of file" );
        }

        buffer = channel.map( mode, position, size );
        buffer.load();
        buffer.order( order.order );

        bufferPos = position;
    }

    /***************************************************************************
     * Loads the buffer if it has never been loaded or if no data is remaining.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    private void checkBuffer() throws IOException
    {
        if( buffer == null )
        {
            readBuffer( bufferPos );
        }
        else if( !buffer.hasRemaining() )
        {
            readBuffer( bufferPos + buffer.position() );
        }
    }

    /***************************************************************************
     * Loads the buffer if it has never been loaded or if no data is remaining
     * and ensures that the provided number of bytes are available.
     * @param smallSize the number of bytes to be checked.
     * @throws EOFException if there are fewer than the provided number of bytes
     * remaining in the stream.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    private void checkBuffer( int smallSize ) throws EOFException, IOException
    {
        if( buffer == null )
        {
            readBuffer( bufferPos );
        }
        else if( getAvailable() < smallSize )
        {
            throw new EOFException( "Too few bytes left in stream (" +
                getAvailable() + ") to read " + smallSize );
        }
        else if( buffer.remaining() < smallSize )
        {
            readBuffer( bufferPos + buffer.position() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte read() throws EOFException, IOException
    {
        checkBuffer();

        return buffer.get();
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
        int totalRead = 0;
        int toCopy;

        while( totalRead < len && getAvailable() > 0 )
        {
            try
            {
                checkBuffer();
            }
            catch( EOFException ex )
            {
                return totalRead;
            }

            toCopy = Math.min( len - totalRead, buffer.remaining() );

            buffer.get( buf, off, toCopy );

            totalRead += toCopy;
            off += toCopy;
        }

        return len;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte [] buf, int off, int len )
        throws EOFException, IOException
    {
        int bytesRead = 0;

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
        buffer = null;
        channel.close();
        raf.close();
        System.gc();

        // LogUtils.printDebug( "Closing mapped file" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void seek( long pos ) throws IOException
    {
        if( pos >= this.bufferPos &&
            pos < ( this.bufferPos + this.buffer.position() ) )
        {
            buffer.position( ( int )( pos - bufferPos ) );
        }
        else if( pos < getLength() )
        {
            readBuffer( pos );
        }
        else
        {
            this.bufferPos = getLength();
            this.buffer = null;
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
        return bufferPos + ( buffer != null ? buffer.position() : 0 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getLength() throws IOException
    {
        if( length < 0 )
        {
            length = raf.length();
        }

        return length;
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

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // IDataStream specific methods.
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ByteOrdering getOrder()
    {
        return order;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean readBoolean() throws IOException
    {
        checkBuffer();

        return buffer.get() != 0;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public short readShort() throws IOException
    {
        checkBuffer( 2 );

        return buffer.getShort();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int readInt() throws IOException
    {
        checkBuffer( 4 );

        return buffer.getInt();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long readLong() throws IOException
    {
        checkBuffer( 8 );

        return buffer.getLong();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public float readFloat() throws IOException
    {
        checkBuffer( 4 );

        return buffer.getFloat();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double readDouble() throws IOException
    {
        checkBuffer( 8 );

        return buffer.getDouble();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeBoolean( boolean v ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeShort( short v ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeInt( int v ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeLong( long v ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeFloat( float v ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeDouble( double v ) throws IOException
    {
        throw new IOException( "Cannot write to a read only stream." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setOrder( ByteOrdering ordering )
    {
        this.order = ordering;
    }
}
