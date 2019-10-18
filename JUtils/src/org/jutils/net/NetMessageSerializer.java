package org.jutils.net;

import java.io.IOException;
import java.time.LocalDateTime;

import org.jutils.ValidationException;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;
import org.jutils.io.LengthStringSerializer;
import org.jutils.io.LocalDateTimeSerializer;

/*******************************************************************************
 *
 ******************************************************************************/
public class NetMessageSerializer implements IDataSerializer<NetMessage>
{
    /**  */
    private final LengthStringSerializer stringSerializer = new LengthStringSerializer();
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

        String localAddress = stringSerializer.read( stream );
        int localPort = stream.readInt();

        String remoteAddress = stringSerializer.read( stream );
        int remotePort = stream.readInt();

        int contentsLen = stream.readInt();
        byte [] contents = new byte[contentsLen];

        stream.read( contents );

        return new NetMessage( received, time, localAddress, localPort,
            remoteAddress, remotePort, contents );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( NetMessage msg, IDataStream stream ) throws IOException
    {
        stream.writeBoolean( msg.received );

        timeSerializer.write( msg.time, stream );

        stringSerializer.write( msg.localAddress, stream );
        stream.writeInt( msg.localPort );

        stringSerializer.write( msg.remoteAddress, stream );
        stream.writeInt( msg.remotePort );

        stream.writeInt( msg.contents.length );
        stream.write( msg.contents );
    }
}
