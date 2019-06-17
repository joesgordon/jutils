package org.jutils.io;

import java.io.IOException;

import org.jutils.ValidationException;

/*******************************************************************************
 * Deep clones objects, retaining the intermediate buffer from call to call.
 * @param <T> the type of object to be cloned.
 ******************************************************************************/
public class ItemCloner<T>
{
    /** The underlying memory to serialize items to/from. */
    private final ByteArrayStream stream;
    /** The output stream wrapping the underlying memory. */
    private final StreamOutput outputStream;
    /** The input stream wrapping the underlying memory. */
    private final StreamInput inputStream;

    /***************************************************************************
     * Creates a new cloner.
     **************************************************************************/
    public ItemCloner()
    {
        this.stream = new ByteArrayStream( 1024 );
        this.outputStream = new StreamOutput( stream );
        this.inputStream = new StreamInput( stream );
    }

    /***************************************************************************
     * Deep clones the provided item.
     * @param item the item to be cloned.
     * @return the clone item.
     * @throws IllegalStateException any error that occurs during serialization.
     **************************************************************************/
    public T cloneItem( T item ) throws IllegalStateException
    {
        T clone = null;

        try
        {
            stream.setLength( 0 );

            stream.seek( 0 );
            XStreamUtils.writeObjectXStream( item, outputStream );

            stream.seek( 0 );
            clone = XStreamUtils.readObjectXStream( inputStream );
        }
        catch( IOException | ValidationException ex )
        {
            throw new IllegalStateException(
                "Could not clone item: " + ex.getMessage(), ex );
        }

        return clone;
    }
}
