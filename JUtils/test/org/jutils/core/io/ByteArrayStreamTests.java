package org.jutils.core.io;

import org.jutils.core.io.ByteArrayStream;
import org.jutils.core.io.IStream;

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
