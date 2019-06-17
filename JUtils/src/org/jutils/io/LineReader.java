package org.jutils.io;

import java.io.*;

/*******************************************************************************
 * Defines an object to read lines from a file and keep up with the number of
 * the line read.
 ******************************************************************************/
public class LineReader implements Closeable
{
    /** The underlying reader. */
    private final RandomAccessFile reader;

    /** The number of the last line read. */
    private long lineNumber;

    /***************************************************************************
     * Creates a new reader of the provided file.
     * @param file the file to be read.
     * @throws FileNotFoundException if the given file does not exist or cannot
     * be read.
     **************************************************************************/
    public LineReader( File file ) throws FileNotFoundException
    {
        this.reader = new RandomAccessFile( file, "r" );
        this.lineNumber = -1;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        reader.close();
    }

    /***************************************************************************
     * Reads the next line of text.
     * @return the line read.
     * @throws IOException any I/O error that occurs.
     **************************************************************************/
    public String readLine() throws IOException
    {
        lineNumber++;
        return reader.readLine();
    }

    /***************************************************************************
     * Returns the number of the last line read.
     * @return the number of the last line read or {@code -1} if none have been
     * read.
     **************************************************************************/
    public long getLastLineNumberRead()
    {
        return lineNumber;
    }

    /***************************************************************************
     * Returns the current position of the reader.
     * @return the position of pointer to the next read operation.
     * @throws IOException any I/O error that occurs.
     **************************************************************************/
    public long getPosition() throws IOException
    {
        return reader.getFilePointer();
    }
}
