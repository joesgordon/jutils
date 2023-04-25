package org.jutils.core.io;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/*******************************************************************************
 * Defines a stream that performs I/O with a file.
 ******************************************************************************/
public class FileStream implements IStream
{
    /** The file to be accessed. */
    private final File file;
    /** The underlying I/O accessor. */
    private final RandomAccessFile raf;

    /***************************************************************************
     * Creates a new stream to read/write to the provided file.
     * @param file the file to be accessed.
     * @throws FileNotFoundException if the given file does not exist and cannot
     * be created or if some other error occurs while opening or creating the
     * file.
     **************************************************************************/
    public FileStream( File file ) throws FileNotFoundException
    {
        this( file, false );
    }

    /***************************************************************************
     * Creates a new stream to read/write to the provided file.
     * @param file the file to be accessed.
     * @param readOnly opens the file as read-only if {@code true}.
     * @throws FileNotFoundException if the given file does not exist, or if the
     * read only flag is false and the given file object does not denote an
     * existing, writable file and a new file of that name cannot be created, or
     * if some other error occurs while opening or creating the file.
     **************************************************************************/
    public FileStream( File file, boolean readOnly )
        throws FileNotFoundException
    {
        this.file = file;
        this.raf = new RandomAccessFile( file, readOnly ? "r" : "rw" );
    }

    /***************************************************************************
     * Sets the length of the file to the provided value.
     * @param length the new length of the file.
     * @throws IOException if any I/O exception occurs.
     * @see RandomAccessFile#setLength(long)
     **************************************************************************/
    public void setLength( long length ) throws IOException
    {
        raf.setLength( length );
    }

    /***************************************************************************
     * Returns the path to the file being accessed.
     * @return the path to the file being accessed.
     **************************************************************************/
    public File getFile()
    {
        return file;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte read() throws IOException
    {
        byte b = raf.readByte();
        return b;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int read( byte [] buf ) throws IOException
    {
        return raf.read( buf );
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
    public int read( byte [] buf, int off, int len ) throws IOException
    {
        return raf.read( buf, off, len );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void readFully( byte [] buf, int off, int len ) throws IOException
    {
        int bytesRead = 0;

        while( bytesRead < len )
        {
            int count = read( buf, off + bytesRead, len - bytesRead );

            if( count < 0 )
            {
                String err = String.format(
                    "End of file reached @ %d of %d; attempted to read past by %d bytes",
                    getPosition(), getLength(), len - bytesRead );
                throw new EOFException( err );
            }

            bytesRead += count;
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
            pos = getPosition() + pos;
        }

        raf.seek( pos );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        raf.close();
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
        return raf.getFilePointer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getLength() throws IOException
    {
        return raf.length();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( byte b ) throws IOException
    {
        raf.write( b & 0xFF );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( byte [] buf ) throws IOException
    {
        raf.write( buf );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( byte [] buf, int off, int len ) throws IOException
    {
        raf.write( buf, off, len );
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public String readLine() throws IOException
    {
        return raf.readLine();
    }
}
