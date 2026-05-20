package jutils.core.io;

import java.io.IOException;

import jutils.core.ValidationException;

/*******************************************************************************
 * Defines methods of reading objects from a resource and writing them to a
 * potentially different type of resource.
 * @param <T> the type of object to be serialized.
 * @param <IN> the type of resource to be read from.
 * @param <OUT> the type of resource to be written to.
 ******************************************************************************/
public interface ISerializer<T, IN, OUT> extends IReader<T, IN>, IWriter<T, OUT>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T read( IN resource ) throws IOException, ValidationException;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( T item, OUT resource ) throws IOException;
}
