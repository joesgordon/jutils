package org.jutils.io;

import java.io.IOException;

/*******************************************************************************
 * Writes an object of the provided type to a data stream.
 * @param <T> the type of object to be written.
 ******************************************************************************/
public interface IDataWriter<T> extends IWriter<T, IDataStream>
{
    /***************************************************************************
     * Writes the object to the current position of the provided stream.
     * @param data the object to be written.
     * @param stream the stream to be written to.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    @Override
    public void write( T data, IDataStream stream ) throws IOException;
}
