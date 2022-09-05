package org.jutils.core.net;

import java.io.IOException;
import java.time.LocalDateTime;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IDataSerializer;
import org.jutils.core.io.IDataStream;
import org.jutils.core.io.LocalDateTimeSerializer;
import org.jutils.core.net.IpAddress.IpVersion;

/*******************************************************************************
 *
 ******************************************************************************/
public class NetMessageSerializer implements IDataSerializer<NetMessage>
{
    /**  */
    private final LocalDateTimeSerializer timeSerializer = new LocalDateTimeSerializer();

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NetMessage read( IDataStream stream )
        throws IOException, ValidationException
    {
        boolean received = stream.readBoolean();

        LocalDateTime time = timeSerializer.read( stream );

        EndPoint local = readEndPoint( stream );
        EndPoint remote = readEndPoint( stream );

        int contentsLen = stream.readInt();
        byte [] contents = new byte[contentsLen];

        stream.read( contents );

        return new NetMessage( received, time, local, remote, contents );
    }

    /***************************************************************************
     * @param stream
     * @return
     * @throws IOException
     **************************************************************************/
    private static EndPoint readEndPoint( IDataStream stream )
        throws IOException
    {
        EndPoint ep = new EndPoint();
        boolean isIpv4 = stream.readBoolean();
        IpVersion ver = isIpv4 ? IpVersion.IPV4 : IpVersion.IPV6;
        int len = ver.byteCount;
        byte [] bytes = new byte[len];

        stream.read( bytes );

        ep.address.set( bytes );
        ep.port = stream.readInt();

        return ep;
    }

    /***************************************************************************
     * @param ep
     * @param stream
     * @throws IOException
     **************************************************************************/
    private static void writeEndPoint( EndPoint ep, IDataStream stream )
        throws IOException
    {
        IpVersion ver = ep.address.getVersion();
        boolean isIpv4 = ver == IpVersion.IPV4;
        int len = ver.byteCount;

        stream.writeBoolean( isIpv4 );
        stream.write( ep.address.address, 0, len );
        stream.writeInt( ep.port );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( NetMessage msg, IDataStream stream ) throws IOException
    {
        stream.writeBoolean( msg.received );

        timeSerializer.write( msg.time, stream );

        writeEndPoint( msg.local, stream );
        writeEndPoint( msg.remote, stream );

        stream.writeInt( msg.contents.length );
        stream.write( msg.contents );
    }
}
