package jutils.core.io;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.utils.ByteOrdering;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ItemBytesSerializer<T> implements IStdSerializer<T, byte []>
{
    /**  */
    private final IDataSerializer<T> serializer;
    /**  */
    private final ByteArrayStream byteStream;
    /**  */
    private final IDataStream dataStream;

    /***************************************************************************
     * @param serializer
     **************************************************************************/
    public ItemBytesSerializer( IDataSerializer<T> serializer )
    {
        this( serializer, ByteOrdering.BIG_ENDIAN );
    }

    /***************************************************************************
     * @param serializer
     * @param order
     **************************************************************************/
    public ItemBytesSerializer( IDataSerializer<T> serializer,
        ByteOrdering order )
    {
        this.serializer = serializer;
        this.byteStream = new ByteArrayStream( 1024 );
        this.dataStream = new DataStream( byteStream, order );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T read( byte [] bytes ) throws IOException, ValidationException
    {
        byte [] buf = byteStream.getBuffer();

        byteStream.setBuffer( bytes );

        try
        {
            return serializer.read( dataStream );
        }
        finally
        {
            byteStream.setBuffer( buf );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( T item, byte [] bytes ) throws IOException
    {
        byte [] buf = byteStream.getBuffer();

        byteStream.setBuffer( bytes );

        try
        {
            serializer.write( item, dataStream );
        }
        finally
        {
            byteStream.setBuffer( buf );
        }
    }
}
