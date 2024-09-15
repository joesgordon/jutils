package jutils.core.io;

import java.io.IOException;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IDataFlow extends AutoCloseable
{
    /***************************************************************************
     * Reads up to {@code buf.length} bytes from the stream.
     * @param buf the buffer into which the data is read.
     * @return the total number of bytes read into the buffer, or -1 if there is
     * no more data because the end of the stream has been reached.
     * @throws IOException If the first byte cannot be read for any reason other
     * than end of file, or if the stream has been closed, or if some other I/O
     * error occurs.
     * @see java.io.InputStream#read(byte[])
     **************************************************************************/
    public default int read( byte [] buf ) throws IOException
    {
        return read( buf, 0, buf.length );
    }

    /***************************************************************************
     * @param buf the buffer into which the data is read.
     * @param off the start offset in array {@code buf} at which the data is
     * written.
     * @param len the maximum number of bytes to read.
     * @return the total number of bytes read into the buffer, or -1 if there is
     * no more data because the end of the stream has been reached.
     * @throws IOException If the first byte cannot be read for any reason other
     * than end of file, or if the stream has been closed, or if some other I/O
     * error occurs.
     * @see java.io.InputStream#read(byte[], int, int)
     **************************************************************************/
    public int read( byte [] buf, int off, int len ) throws IOException;

    /***************************************************************************
     * Writes the given bytes to this stream at the current offset, increasing
     * the length of the stream if necessary.
     * @param buf the bytes to be written.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    public default void write( byte [] buf ) throws IOException
    {
        write( buf, 0, buf.length );
    }

    /***************************************************************************
     * Writes the given byte to this stream at the current offset, increasing
     * the length of the stream if necessary.
     * @param buf the buffer containing the bytes to be written.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    public void write( byte [] buf, int off, int len ) throws IOException;
}
