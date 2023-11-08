package jutils.core.io;

import java.io.EOFException;
import java.io.IOException;

/*******************************************************************************
 * IStream is a generic interface for reading and writing to a location (disk,
 * memory, socket, etc).
 ******************************************************************************/
public interface IStream extends IDataFlow
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException;

    /***************************************************************************
     * Sets the file-pointer offset, measured from the beginning of this file,
     * at which the next read or write occurs. The offset may be set beyond the
     * end of the stream. Setting the offset beyond the end of the stream does
     * not change the stream length. The stream length will change only by
     * writing after the offset has been set beyond the end of the file.
     * @param pos the offset position, measured in bytes from the beginning of
     * the stream, at which to set the file pointer. If negative, {@code pos}
     * will be added to the current position before a seek occurs.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    public void seek( long pos ) throws IOException;

    /***************************************************************************
     * Skips the number of bytes given or rewinds if negative. Calls<br><br>
     * {@code seek( getPosition() + count )}
     * @param count the number of bytes to be skipped.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    public void skip( long count ) throws IOException;

    /***************************************************************************
     * Returns the number of bytes available in this stream. Returns<br><br>
     * {@code getLength() - getPosition() )}
     * @return the number of bytes available in this stream.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    public long getAvailable() throws IOException;

    /***************************************************************************
     * Returns the current offset in this file.
     * @return the offset from the beginning of the file, in bytes, at which the
     * next read or write occurs.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    public long getPosition() throws IOException;

    /***************************************************************************
     * Returns the length of this file.
     * @return the length of this file, measured in bytes.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    public long getLength() throws IOException;

    /***************************************************************************
     * Reads a single byte from the stream.
     * @return the byte read.
     * @throws EOFException if the end of the stream is read and there is no
     * byte to be returned.
     * @throws IOException If the first byte cannot be read for any reason other
     * than end of file, or if the stream has been closed, or if some other I/O
     * error occurs.
     **************************************************************************/
    public byte read() throws EOFException, IOException;

    /***************************************************************************
     * Fills the entire buffer with the next consecutive bytes from this stream
     * or throws and EOFException if the end-of-file is reached first.
     * @param buf the buffer into which the data is read.
     * @throws EOFException If the end of file is reached before all of the
     * bytes in the provided buffer are read.
     * @throws IOException If the first byte cannot be read for any reason other
     * than end of file, or if the stream has been closed, or if some other I/O
     * error occurs.
     **************************************************************************/
    public default void readFully( byte [] buf )
        throws EOFException, IOException
    {
        readFully( buf, 0, buf.length );
    }

    /***************************************************************************
     * Fills the buffer with the next consecutive bytes from this stream at the
     * {@code off} for the specified {@code len} number of bytes or throws and
     * EOFException if the end-of-file is reached first.
     * @param buf the buffer into which the data is read.
     * @param off the start offset in array {@code buf} at which the data is
     * written.
     * @param len the maximum number of bytes to read.
     * @throws EOFException If the end of file is reached before the provided
     * number of bytes are read.
     * @throws IOException If the first byte cannot be read for any reason other
     * than end of file, or if the stream has been closed, or if some other I/O
     * error occurs.
     **************************************************************************/
    public void readFully( byte [] buf, int off, int len )
        throws EOFException, IOException;

    /***************************************************************************
     * Writes the given byte to this stream at the current offset, increasing
     * the length of the stream if necessary.
     * @param b the byte to be written.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    public void write( byte b ) throws IOException;
}
