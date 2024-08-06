package jutils.telemetry.io.ch10;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.telemetry.data.ch10.Packet;

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

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketSerializer()
    {
        this.headerSerializer = new PacketHeaderSerializer();
        this.secondarySerializer = new SecondaryHeaderSerializer();
        this.bodySerializer = new PacketBodySerializer();
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

        int trailerLen = p.header.getTrailerLength();

        if( p.header.secHdrPresent )
        {
            secondarySerializer.read( p.secondary, stream );
        }

        p.body = bodySerializer.read( stream, p.header );

        if( trailerLen > 0 )
        {
            int fillerLen = trailerLen - p.header.checksumPresent.size;

            stream.skip( fillerLen );

            switch( p.header.checksumPresent )
            {
                case CS_8:
                    p.trailer.checksum = 0xFF & stream.read();
                    break;

                case CS_16:
                    p.trailer.checksum = 0xFFFF & stream.readShort();
                    break;

                case CS_32:
                    p.trailer.checksum = 0xFFFFFF & stream.readInt();
                    break;

                case NO_CHECKSUM:
                    break;
            }
        }

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
