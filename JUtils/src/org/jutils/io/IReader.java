package org.jutils.io;

import java.io.IOException;

import org.jutils.ValidationException;

/*******************************************************************************
 * Defines a generic method of reading an item from a resource.
 * @param <T> the type of the item to be read.
 * @param <R> the type of the resource from which the item is read.
 ******************************************************************************/
public interface IReader<T, R>
{
    /***************************************************************************
     * Reads the item from the provided resource.
     * @param resource the resource from which the item is read.
     * @return the item read from the provided resource.
     * @throws IOException any I/O error that occurs.
     * @throws ValidationException any error due to incorrect format or other
     * non-I/O errors that might arise
     **************************************************************************/
    public T read( R resource ) throws IOException, ValidationException;
}
