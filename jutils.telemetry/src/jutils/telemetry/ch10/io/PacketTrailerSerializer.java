package jutils.telemetry.ch10.io;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataStream;
import jutils.core.io.IKeyedSerializer;
import jutils.telemetry.ch10.PacketHeader;
import jutils.telemetry.ch10.PacketTrailer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketTrailerSerializer
    implements IKeyedSerializer<PacketHeader, PacketTrailer>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public PacketTrailer read( IDataStream stream, PacketHeader key )
        throws IOException, ValidationException
    {
        PacketTrailer trailer = new PacketTrailer();

        read( trailer, stream, key );

        return trailer;
    }

    /***************************************************************************
     * @param trailer
     * @param stream
     * @param header
     * @throws IOException
     **************************************************************************/
    public void read( PacketTrailer trailer, IDataStream stream,
        PacketHeader header ) throws IOException
    {
        int trailerLen = header.getTrailerLength();

        if( trailerLen > 0 )
        {
            int fillerLen = trailerLen - header.checksumPresent.size;

            trailer.filler = new byte[fillerLen];
            stream.readFully( trailer.filler );

            switch( header.checksumPresent )
            {
                case CS_8:
                    trailer.checksum = 0xFF & stream.read();
                    break;

                case CS_16:
                    trailer.checksum = 0xFFFF & stream.readShort();
                    break;

                case CS_32:
                    trailer.checksum = 0xFFFFFFFFL & stream.readInt();
                    break;

                case NO_CHECKSUM:
                    break;
            }
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( PacketTrailer data, IDataStream stream,
        PacketHeader key ) throws IOException
    {
        // TODO Auto-generated method stub
    }
}
