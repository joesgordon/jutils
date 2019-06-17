package org.jutils.io;

import java.io.IOException;
import java.io.OutputStream;

/******************************************************************************
 * Wraps an {@link IStream} as an {@link OutputStream}.
 ******************************************************************************/
public class StreamOutput extends OutputStream
{
    /** The underlying stream. */
    private final IStream stream;

    /***************************************************************************
     * Creates a new output stream with the provided stream.
     * @param stream the stream to be wrapped.
     **************************************************************************/
    public StreamOutput( IStream stream )
    {
        this.stream = stream;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( int b ) throws IOException
    {
        stream.write( ( byte )b );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte b[], int off, int len ) throws IOException
    {
        stream.write( b, off, len );
    }
}
