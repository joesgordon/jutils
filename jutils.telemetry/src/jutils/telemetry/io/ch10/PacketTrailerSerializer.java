package jutils.telemetry.io.ch10;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.telemetry.data.ch10.PacketTrailer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketTrailerSerializer implements IDataSerializer<PacketTrailer>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public PacketTrailer read( IDataStream stream )
        throws IOException, ValidationException
    {
        PacketTrailer trailer = new PacketTrailer();

        read( trailer, stream );

        return trailer;
    }

    /***************************************************************************
     * @param trailer
     * @param stream
     * @throws IOException
     **************************************************************************/
    public void read( PacketTrailer trailer, IDataStream stream )
        throws IOException
    {
        trailer.checksum = stream.readLong();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( PacketTrailer trailer, IDataStream stream )
        throws IOException
    {
        // TODO Auto-generated method stub
    }
}
