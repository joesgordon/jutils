package jutils.telemetry.io.ch10;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataStream;
import jutils.telemetry.data.ch10.PacketHeader;
import jutils.telemetry.data.ch10.Pcm1Body;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Pcm1BodySerializer implements IBodySerializer<Pcm1Body>
{
    /**  */
    private final Pcm1SpecificDataSerializer specificDataSerializer;
    /**  */
    private final PcmDataSerializer dataSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public Pcm1BodySerializer()
    {
        this.specificDataSerializer = new Pcm1SpecificDataSerializer();
        this.dataSerializer = new PcmDataSerializer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Pcm1Body read( IDataStream stream, PacketHeader header )
        throws IOException, ValidationException
    {
        Pcm1Body body = new Pcm1Body();

        specificDataSerializer.read( body.specificData, stream );
        body.data = dataSerializer.read( stream, header, body.specificData );

        return body;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( Pcm1Body body, IDataStream stream, PacketHeader header )
        throws IOException
    {
        specificDataSerializer.write( body.specificData, stream );
        dataSerializer.write( body.data, stream, header, body.specificData );
    }
}
