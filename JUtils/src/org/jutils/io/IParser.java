package org.jutils.io;

import org.jutils.ValidationException;

/*******************************************************************************
 * Parses objects of the provided type from strings.
 * @param <T> the type of object to be parsed.
 ******************************************************************************/
public interface IParser<T>
{
    /***************************************************************************
     * Parses the object from the provided string.
     * @param str the string to be parsed.
     * @return the object read from the string.
     * @throws ValidationException if the string does not specify a valid object
     * or is improperly formatted.
     **************************************************************************/
    public T parse( String str ) throws ValidationException;
}
