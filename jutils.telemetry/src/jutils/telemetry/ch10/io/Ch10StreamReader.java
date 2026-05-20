package jutils.telemetry.ch10.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jutils.core.ValidationException;
import jutils.core.io.IDataReader;
import jutils.core.io.IDataStream;
import jutils.telemetry.ch10.Ch10Channel;
import jutils.telemetry.ch10.Ch10File;
import jutils.telemetry.ch10.PacketHeader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ch10StreamReader implements IDataReader<Ch10File>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Ch10File read( IDataStream stream )
        throws IOException, ValidationException
    {
        Ch10File c10 = new Ch10File( stream );
        Map<Short, Ch10Channel> channels = new HashMap<>();

        PacketHeaderSerializer serializer = new PacketHeaderSerializer();

        // FieldPrinter fp = new FieldPrinter();
        // int i = 0;
        while( PacketHeader.SIZE < stream.getAvailable() )
        {
            long position = stream.getPosition();
            PacketHeader header = serializer.read( stream );
            Ch10Channel channel = channels.get( header.channelId );

            if( channel == null )
            {
                channel = new Ch10Channel( header.channelId, header.dataType );

                channel.startTime = header.relativeTimeCounter;

                channels.put( header.channelId, channel );
            }
            else
            {
                channel.endTime = header.relativeTimeCounter;
                channel.packetCount++;
            }

            if( header.channelId != channel.id )
            {
                String err = String.format(
                    " Data type (%s) of packet at %d does not match data " +
                        "type (%s) of channel ID (%d) ",
                    header.dataType.getDescription(), position,
                    channel.dataType.getDescription(), header.channelId );
                throw new ValidationException( err );
            }

            c10.packets.add( position );

            if( c10.initRelTime == 0 )
            {
                if( header.channelId != 0 )
                {
                    c10.initRelTime = header.relativeTimeCounter;
                }
            }
            // else
            // {
            // channel.
            // }

            // LogUtils.printDebug( "%d) %s %d %d", i, header.dataType.name,
            // header.relativeTimeCounter, c10.initRelTime );

            // fp.printTier( "Packet " + i, header );

            long skipSize = header.packetLength - PacketHeader.SIZE;

            stream.skip( skipSize );
            // i++;
        }

        for( Ch10Channel channel : channels.values() )
        {
            c10.channels.add( channel );
        }

        // LogUtils.print( fp.toString() );

        return c10;
    }
}
