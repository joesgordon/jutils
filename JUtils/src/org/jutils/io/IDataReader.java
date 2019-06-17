package org.jutils.io;

import java.io.IOException;

import org.jutils.ValidationException;

/*******************************************************************************
 * Reads an object of the provided type from a data stream.
 * @param <T> the type of object to be read.
 ******************************************************************************/
public interface IDataReader<T> extends IReader<T, IDataStream>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T read( IDataStream stream ) throws IOException, ValidationException;
}
