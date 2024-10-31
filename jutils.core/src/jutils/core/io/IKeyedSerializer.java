package jutils.core.io;

import java.io.IOException;

import jutils.core.ValidationException;

/*******************************************************************************
 * @param <K>
 * @param <T>
 ******************************************************************************/
public interface IKeyedSerializer<K, T>
{
    /***************************************************************************
     * @param stream
     * @param key
     * @return
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    public T read( IDataStream stream, K key )
        throws IOException, ValidationException;

    /***************************************************************************
     * @param data
     * @param stream
     * @param key
     * @throws IOException
     **************************************************************************/
    public void write( T data, IDataStream stream, K key ) throws IOException;
}
