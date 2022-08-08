package org.jutils.core.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/*******************************************************************************
 * Wraps an {@link IStream} as an {@link InputStream}.
 ******************************************************************************/
public class StreamInput extends InputStream
{
    /** The underlying stream. */
    private final IStream stream;

    /***************************************************************************
     * Creates a new input stream with the provided stream.
     * @param stream the stream to be wrapped.
     **************************************************************************/
    public StreamInput( IStream stream )
    {
        this.stream = stream;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read() throws IOException
    {
        int b = -1;

        try
        {
            b = stream.read() & 0xFF;
        }
        catch( EOFException ex )
        {
            b = -1;
        }

        return b;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int read( byte b[], int off, int len ) throws IOException
    {
        int count = 0;

        try
        {
            count = stream.read( b, off, len );
        }
        catch( EOFException ex )
        {
            count = 0;
        }

        return count;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int available() throws IOException
    {
        return ( int )stream.getAvailable();
    }
}
