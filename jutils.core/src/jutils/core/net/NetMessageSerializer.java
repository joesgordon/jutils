package jutils.core.net;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.io.LocalDateTimeSerializer;
import jutils.core.net.EndPoint.EndPointSerializer;

/*******************************************************************************
 *
 ******************************************************************************/
public class NetMessageSerializer implements IDataSerializer<NetMessage>
{
    /**  */
    public static byte [] VERSION_MAGIC_NUM = "\0netmsg01\0".getBytes(
        Charset.forName( "US-ASCII" ) );
    /**  */
    public static final String NETMSGS_EXT = "netmsgs";
    /**  */
    public static final String MSGS_EXT = "msgs";

    /**  */
    private final LocalDateTimeSerializer timeSerializer;
    /**  */
    private final EndPointSerializer pointSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public NetMessageSerializer()
    {
        this.timeSerializer = new LocalDateTimeSerializer();
        this.pointSerializer = new EndPointSerializer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NetMessage read( IDataStream stream )
        throws IOException, ValidationException
    {
        boolean received = stream.readBoolean();
        LocalDateTime time = timeSerializer.read( stream );
        EndPoint local = pointSerializer.read( stream );
        EndPoint remote = pointSerializer.read( stream );

        int contentsLen = stream.readInt();
        byte [] contents = new byte[contentsLen];

        stream.readFully( contents );

        return new NetMessage( received, time, local, remote, contents );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( NetMessage msg, IDataStream stream ) throws IOException
    {
        stream.writeBoolean( msg.received );
        timeSerializer.write( msg.time, stream );
        pointSerializer.write( msg.local, stream );
        pointSerializer.write( msg.remote, stream );

        stream.writeInt( msg.contents.length );
        stream.write( msg.contents );
    }
}
