package jutils.telemetry.ch10;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jutils.core.ValidationException;
import jutils.core.io.DataStream;
import jutils.core.io.FieldPrinter;
import jutils.core.io.FileStream;
import jutils.core.io.IDataReader;
import jutils.core.io.IDataStream;
import jutils.core.io.IReader;
import jutils.core.io.LogUtils;
import jutils.core.utils.ByteOrdering;
import jutils.telemetry.ch10.PacketHeader.PacketHeaderSerializer;

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
    public final List<Long> positions;

    /***************************************************************************
     * @param stream
     **************************************************************************/
    public Ch10File( IDataStream stream )
    {
        this.stream = stream;
        this.channels = new ArrayList<>();
        this.positions = new ArrayList<>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class Ch10FileReader implements IReader<Ch10File, File>
    {
        /**  */
        private final Ch10StreamReader reader;

        /**
         * 
         */
        public Ch10FileReader()
        {
            this.reader = new Ch10StreamReader();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Ch10File read( File file )
            throws IOException, ValidationException
        {
            try( FileStream fs = new FileStream( file, true );
                 IDataStream stream = new DataStream( fs,
                     ByteOrdering.INTEL_ORDER ) )
            {
                return reader.read( stream );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class Ch10StreamReader implements IDataReader<Ch10File>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public Ch10File read( IDataStream stream )
            throws IOException, ValidationException
        {
            Ch10File c10 = new Ch10File( stream );
            Map<Short, Ch10Channel> channels = new HashMap<>();

            PacketHeaderSerializer serializer = new PacketHeaderSerializer();

            FieldPrinter fp = new FieldPrinter();
            int i = 0;
            while( PacketHeader.SIZE < stream.getAvailable() )
            {
                long position = stream.getPosition();
                PacketHeader header = serializer.read( stream );
                Ch10Channel channel = channels.get( header.channelId );

                if( channel == null )
                {
                    channel = new Ch10Channel( header.channelId,
                        header.dataType );

                    channels.put( header.channelId, channel );
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

                c10.positions.add( position );

                // LogUtils.print( "%d, %d, %d, %d, %s, %d", i,
                // header.channelId,
                // header.sequenceNumber & 0xFF, header.packetLength,
                // header.dataType.name, header.relativeTimeCounter );
                fp.printTier( "Packet " + i, header );

                long skipSize = header.packetLength - PacketHeader.SIZE;

                stream.skip( skipSize );
                i++;
            }

            for( Ch10Channel channel : channels.values() )
            {
                c10.channels.add( channel );
            }

            LogUtils.print( fp.toString() );

            return c10;
        }
    }
}
