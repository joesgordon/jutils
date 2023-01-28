package org.jutils.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/*******************************************************************************
 * Defines an object to read lines from a file and keep up with the number of
 * the line read.
 ******************************************************************************/
public class LineReader implements AutoCloseable
{
    /** The underlying reader. */
    private final FileStream stream;

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
        this.stream = new FileStream( file, true );
        this.lineNumber = -1;
    }

    /***************************************************************************
     * Creates a new reader of the provided file.
     * @param stream
     **************************************************************************/
    public LineReader( FileStream stream )
    {
        this.stream = stream;
        this.lineNumber = -1;
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
     * Reads the next line of text.
     * @return the line read.
     * @throws IOException any I/O error that occurs.
     **************************************************************************/
    public String readLine() throws IOException
    {
        lineNumber++;
        return stream.readLine();
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
        return stream.getPosition();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IStream getStream()
    {
        return stream;
    }
}
