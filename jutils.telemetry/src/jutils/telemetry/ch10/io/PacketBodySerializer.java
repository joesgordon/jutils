package jutils.telemetry.ch10.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jutils.core.ValidationException;
import jutils.core.io.IDataStream;
import jutils.telemetry.ch10.DataType;
import jutils.telemetry.ch10.IPacketBody;
import jutils.telemetry.ch10.PacketHeader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketBodySerializer implements IBodySerializer<IPacketBody>
{
    /**  */
    private final Map<DataType, IBodySerializer<?>> serializers;
    /**  */
    private final IBodySerializer<?> defaultSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketBodySerializer()
    {
        this.serializers = new HashMap<>();
        this.defaultSerializer = new DataBodySerializer();

        serializers.put( DataType.COMPUTER_GENERATED_1,
            new CompGen1BodySerializer() );
        serializers.put( DataType.TIME_1, new Time1BodySerializer() );
        serializers.put( DataType.PCM_1, new Pcm1BodySerializer() );
    }

    /***************************************************************************
     * @param key
     * @param serializer
     **************************************************************************/
    public void put( DataType key, IBodySerializer<?> serializer )
    {
        serializers.put( key, serializer );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IPacketBody read( IDataStream stream, PacketHeader header )
        throws IOException, ValidationException
    {
        IBodySerializer<?> serializer = getSerializer( header.dataType );
        IPacketBody body = serializer.read( stream, header );

        return body;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( IPacketBody data, IDataStream stream,
        PacketHeader header ) throws IOException
    {
        IBodySerializer<?> serializer = getSerializer( header.dataType );
        @SuppressWarnings( "unchecked")
        IBodySerializer<IPacketBody> bodySerializer = ( IBodySerializer<
            IPacketBody> )serializer;

        bodySerializer.write( data, stream, header );
    }

    /***************************************************************************
     * @param key
     * @return
     **************************************************************************/
    private IBodySerializer<?> getSerializer( DataType key )
    {
        IBodySerializer<?> serializer = serializers.get( key );

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
