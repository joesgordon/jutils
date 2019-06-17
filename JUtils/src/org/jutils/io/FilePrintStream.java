package org.jutils.io;

import java.io.*;

import org.jutils.Utils;

/*******************************************************************************
 * Defines a print stream that prints to a file in a buffered fashion.
 ******************************************************************************/
public class FilePrintStream implements IPrintStream
{
    /** The buffer size used to create the underlying writer. */
    private static final int BUFFER_SIZE = 64 * 1024;

    /** The output stream that writes to a file. */
    private final FileOutputStream fileStream;
    /** the writer that writes to an output stream. */
    private final OutputStreamWriter outputWriter;
    /** The buffer to use for printing. */
    private final BufferedWriter writer;

    /***************************************************************************
     * Creates a new print string that writes to the provided file that will be
     * overridden.
     * @param file the file to be written to.
     * @throws IOException if the file exists but is a directory rather than a
     * regular file, does not exist but cannot be created, or cannot be opened
     * for any other reason.
     **************************************************************************/
    public FilePrintStream( File file ) throws IOException
    {
        this( file, false );
    }

    /***************************************************************************
     * Creates a new print string that writes to the provided file that will be
     * appended according to the provided parameter.
     * @param file the file to be written to.
     * @param append appends the file if {@code true}; overwrites otherwise.s
     * @throws IOException if the file exists but is a directory rather than a
     * regular file, does not exist but cannot be created, or cannot be opened
     * for any other reason.
     **************************************************************************/
    public FilePrintStream( File file, boolean append ) throws IOException
    {
        this.fileStream = new FileOutputStream( file, append );
        this.outputWriter = new OutputStreamWriter( fileStream,
            IOUtils.US_ASCII );
        this.writer = new BufferedWriter( outputWriter, BUFFER_SIZE );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        writer.close();
        outputWriter.close();
        fileStream.close();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void print( String str )
    {
        write( str );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void println()
    {
        writeNewLine();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void println( String line )
    {
        write( line );
        writeNewLine();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void println( String format, Object... args )
    {
        println( String.format( format, args ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void print( String format, Object... args )
    {
        print( String.format( format, args ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void println( char [] chars )
    {
        write( chars );
        writeNewLine();
    }

    /***************************************************************************
     * Writes the system line separator to the underlying stream.
     **************************************************************************/
    private void writeNewLine()
    {
        write( Utils.NEW_LINE );
    }

    /***************************************************************************
     * Writes the provided string to the underlying stream, suppressing any
     * exception that occurs.
     * @param str the string to be written.
     **************************************************************************/
    private void write( String str )
    {
        try
        {
            writer.write( str );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * Writes the provided characters to the underlying stream, suppressing any
     * exception that occurs.
     * @param chars the characters to be written.
     **************************************************************************/
    private void write( char [] chars )
    {
        try
        {
            writer.write( chars );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }
    }
}
