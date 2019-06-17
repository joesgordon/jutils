package org.jutils.io;

import java.io.*;

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
     * 
     **************************************************************************/
    @Override
    public byte read() throws IOException
    {
        byte b = raf.readByte();
        return b;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte [] buf ) throws IOException
    {
        return raf.read( buf );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte [] buf ) throws IOException
    {
        readFully( buf, 0, buf.length );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte [] buf, int off, int len ) throws IOException
    {
        return raf.read( buf, off, len );
    }

    /***************************************************************************
     * 
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
                throw new EOFException(
                    "End of file reached; attempted to read past by " +
                        ( len - bytesRead ) + " bytes" );
            }

            bytesRead += count;
        }
    }

    /***************************************************************************
     * 
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
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        raf.close();
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
        return raf.getFilePointer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getLength() throws IOException
    {
        return raf.length();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte b ) throws IOException
    {
        raf.write( b & 0xFF );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte [] buf ) throws IOException
    {
        raf.write( buf );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte [] buf, int off, int len ) throws IOException
    {
        raf.write( buf, off, len );
    }
}
