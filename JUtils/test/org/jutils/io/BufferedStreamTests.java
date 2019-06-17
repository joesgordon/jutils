package org.jutils.io;

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
