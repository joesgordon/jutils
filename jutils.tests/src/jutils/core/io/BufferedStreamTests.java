package jutils.core.io;

import jutils.core.io.BufferedStream;
import jutils.core.io.ByteArrayStream;
import jutils.core.io.IStream;

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
