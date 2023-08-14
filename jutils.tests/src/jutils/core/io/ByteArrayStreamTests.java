package jutils.core.io;

import jutils.core.io.ByteArrayStream;
import jutils.core.io.IStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ByteArrayStreamTests extends IStreamTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected IStream createStream()
    {
        return new ByteArrayStream();
    }
}
