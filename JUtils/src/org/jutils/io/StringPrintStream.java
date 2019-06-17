package org.jutils.io;

import org.jutils.Utils;

/*******************************************************************************
 * Defines a print stream that writes to a string.
 ******************************************************************************/
public class StringPrintStream implements IPrintStream
{
    /** The buffer to hold the strings written. */
    private final StringBuilder buffer;

    /***************************************************************************
     * Creates a new print stream with an empty buffer.
     **************************************************************************/
    public StringPrintStream()
    {
        this.buffer = new StringBuilder();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void print( String str )
    {
        buffer.append( str );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void println( String str )
    {
        buffer.append( str );
        println();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void println()
    {
        buffer.append( Utils.NEW_LINE );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return buffer.toString();
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
        buffer.append( chars );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close()
    {
        buffer.setLength( 0 );
    }

    /***************************************************************************
     * Returns {@code true} if the stream has never been written to.
     * @return {@code true} if the stream has never been written to;
     * {@code false} otherwise.
     **************************************************************************/
    public boolean isEmpty()
    {
        return buffer.length() > 0;
    }
}
