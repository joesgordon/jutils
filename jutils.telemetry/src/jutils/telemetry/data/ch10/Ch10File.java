package jutils.telemetry.data.ch10;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jutils.core.ValidationException;
import jutils.core.io.IDataStream;
import jutils.core.io.LogUtils;
import jutils.telemetry.io.ch10.PacketSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ch10File
{
    /**  */
    public final IDataStream stream;
    /**  */
    public final List<Ch10Channel> channels;
    /**  */
    public final List<Long> packets;
    /**  */
    private final PacketSerializer packetSerializer;

    /**  */
    public String name;
    /**  */
    public long initRelTime;

    /***************************************************************************
     * @param stream
     **************************************************************************/
    public Ch10File( IDataStream stream )
    {
        this.stream = stream;
        this.channels = new ArrayList<>();
        this.packets = new ArrayList<>();
        this.packetSerializer = new PacketSerializer();

        this.name = "";
        this.initRelTime = 0;
    }

    /***************************************************************************
     * @param index
     * @return
     * @throws IllegalStateException
     **************************************************************************/
    public PacketInfo readPacket( int index ) throws IllegalStateException
    {
        long position = packets.get( index );

        return readPacketAt( position );
    }

    /***************************************************************************
     * @param position
     * @return
     * @throws IllegalStateException
     **************************************************************************/
    private PacketInfo readPacketAt( long position )
        throws IllegalStateException
    {

        try
        {
            stream.seek( position );

            Packet p = packetSerializer.read( stream );
            PacketInfo info = new PacketInfo( position, p );

            stream.seek( position );
            stream.readFully( info.data );

            return info;
        }
        catch( IOException ex )
        {
            throw new IllegalStateException( ex );
        }
        catch( ValidationException ex )
        {
            throw new IllegalStateException( ex );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void printInfo()
    {
        for( Ch10Channel channel : channels )
        {
            if( channel.dataType == DataType.PCM_1 )
            {
                LogUtils.print( "PCM Time\tPCM Delta\tTime\tTime Delta" );

                long startPcmTime = -1;
                long lastPcmTime = -1;
                long timeTime = -1;
                long lastTime = -1;

                for( long position : packets )
                {
                    PacketInfo info = readPacketAt( position );

                    DataType packetType = info.packet.header.dataType;
                    long packetTime = info.packet.header.relativeTimeCounter;

                    if( packetType == DataType.TIME_1 )
                    {
                        timeTime = packetTime;
                    }
                    else if( info.packet.header.channelId == channel.id )
                    {
                        if( startPcmTime < 1 )
                        {
                            startPcmTime = packetTime;
                        }
                        else
                        {
                            long pcmTime = packetTime - startPcmTime;
                            long pcmDelta = packetTime - lastPcmTime;
                            long time = packetTime - timeTime;
                            long timeDelta = time - lastTime;

                            LogUtils.print( "%d\t%d\t%d\t%d", pcmTime, pcmDelta,
                                time, timeDelta );

                            lastTime = time;
                        }

                        lastPcmTime = packetTime;
                    }
                }
            }
        }
    }
}
