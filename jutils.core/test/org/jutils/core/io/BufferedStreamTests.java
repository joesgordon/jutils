package org.jutils.core.io;

import org.jutils.core.io.BufferedStream;
import org.jutils.core.io.ByteArrayStream;
import org.jutils.core.io.IStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BufferedStreamTests extends IStreamTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @SuppressWarnings( "resource")
    @Override
    protected IStream createStream()
    {
        ByteArrayStream byteStream = new ByteArrayStream();

        return new BufferedStream( byteStream, 7 );
    }
}
