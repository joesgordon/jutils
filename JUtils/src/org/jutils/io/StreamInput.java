package org.jutils.io;

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
        return stream.read() & 0xFF;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte b[], int off, int len ) throws IOException
    {
        return stream.read( b, off, len );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int available() throws IOException
    {
        return ( int )stream.getAvailable();
    }
}
