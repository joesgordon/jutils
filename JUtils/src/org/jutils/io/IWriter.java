package org.jutils.io;

import java.io.IOException;

/*******************************************************************************
 * Defines a generic method of writing a type from a resource.
 * @param <T> the type to be written.
 * @param <R> the resource to which the type is written.
 ******************************************************************************/
public interface IWriter<T, R>
{
    /***************************************************************************
     * Writes the item to the provided resource.
     * @param item the item to be written.
     * @param resource the resource to which the item is written.
     * @throws IOException any I/O error that occurs.
     **************************************************************************/
    public void write( T item, R resource ) throws IOException;
}
