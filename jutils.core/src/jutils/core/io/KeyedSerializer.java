package jutils.core.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jutils.core.ValidationException;

/*******************************************************************************
 * @param <K>
 * @param <T>
 ******************************************************************************/
public class KeyedSerializer<K, T> implements IKeyedSerializer<K, T>
{
    /**  */
    private final Map<K, IDataSerializer<? extends T>> serializers;
    /**  */
    private final IDataSerializer<? extends T> defaultSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public KeyedSerializer()
    {
        this( null );
    }

    /***************************************************************************
     * @param defaultSerializer
     **************************************************************************/
    public KeyedSerializer( IDataSerializer<? extends T> defaultSerializer )
    {
        this.serializers = new HashMap<>();
        this.defaultSerializer = defaultSerializer;
    }

    /***************************************************************************
     * @param key
     * @param serializer
     **************************************************************************/
    public void put( K key, IDataSerializer<? extends T> serializer )
    {
        serializers.put( key, serializer );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T read( IDataStream stream, K key )
        throws IOException, ValidationException
    {
        IDataSerializer<? extends T> serializer = getSerializer( key );

        return serializer.read( stream );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( T data, IDataStream stream, K key ) throws IOException
    {
        IDataSerializer<? extends T> serializer = getSerializer( key );
        @SuppressWarnings( "unchecked")
        IDataSerializer<T> itmeSerializer = ( IDataSerializer<T> )serializer;

        itmeSerializer.write( data, stream );
    }

    /***************************************************************************
     * @param key
     * @return
     **************************************************************************/
    private IDataSerializer<? extends T> getSerializer( K key )
    {
        IDataSerializer<? extends T> serializer = serializers.get( key );

        if( serializer == null )
        {
            if( defaultSerializer == null )
            {
                String err = String.format(
                    "No serializer set for %s and no default serializer exists",
                    key );
                throw new IllegalStateException( err );
            }

            serializer = defaultSerializer;
        }

        return serializer;
    }
}
