package jutils.telemetry.ch10.io;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.telemetry.ch10.Packet;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketSerializer implements IDataSerializer<Packet>
{
    /**  */
    private final PacketHeaderSerializer headerSerializer;
    /**  */
    private final SecondaryHeaderSerializer secondarySerializer;
    /**  */
    private final PacketBodySerializer bodySerializer;
    /**  */
    private final PacketTrailerSerializer trailerSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketSerializer()
    {
        this.headerSerializer = new PacketHeaderSerializer();
        this.secondarySerializer = new SecondaryHeaderSerializer();
        this.bodySerializer = new PacketBodySerializer();
        this.trailerSerializer = new PacketTrailerSerializer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Packet read( IDataStream stream )
        throws IOException, ValidationException
    {
        Packet p = new Packet();

        headerSerializer.read( p.header, stream );

        if( p.header.secHdrPresent )
        {
            secondarySerializer.read( p.secondary, stream );
        }

        p.body = bodySerializer.read( stream, p.header );

        trailerSerializer.read( p.trailer, stream, p.header );

        return p;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( Packet data, IDataStream stream ) throws IOException
    {
        // TODO Auto-generated method stub
    }
}
