package org.jutils.core.io;

/*******************************************************************************
 * Defines the method by which an object can be written to a string.
 * @param <T> the type of object to be written.
 ******************************************************************************/
public interface IStringWriter<T>
{
    /***************************************************************************
     * Creates a string representation of the provided object.
     * @param item the item to be written.
     * @return the string representation of the item.
     **************************************************************************/
    public String toString( T item );
}
