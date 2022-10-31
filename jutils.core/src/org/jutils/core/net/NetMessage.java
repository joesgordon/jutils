package org.jutils.core.net;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.jutils.core.ui.hex.HexUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetMessage
{
    // TODO Consider using java.time.Instant instead of LocalDateTime.

    /** {@code true} indicates the message was received; {@code false}, sent */
    public final boolean received;
    /** Time of transmission or reception. */
    public final LocalDateTime time;
    /** The local NIC address/port on which the message was Tx/Rx. */
    public final EndPoint local;
    /** The address/port of the remote system the message was Tx/Rx. */
    public final EndPoint remote;
    /** The raw message contents. */
    public final byte [] contents;
    /**  */
    public Object message;

    /***************************************************************************
     * @param received
     * @param local
     * @param remote
     * @param contents
     **************************************************************************/
    public NetMessage( boolean received, EndPoint local, EndPoint remote,
        byte [] contents )
    {
        this( received, LocalDateTime.now( ZoneOffset.UTC ), local, remote,
            contents );
    }

    /***************************************************************************
     * @param received
     * @param time
     * @param local
     * @param remote
     * @param contents
     **************************************************************************/
    public NetMessage( boolean received, LocalDateTime time, EndPoint local,
        EndPoint remote, byte [] contents )
    {
        this.received = received;
        this.time = time;
        this.local = local;
        this.remote = remote;
        this.contents = contents;
        this.message = null;
    }

    /***************************************************************************
     * @param msg
     **************************************************************************/
    public NetMessage( NetMessage msg )
    {
        this.received = msg.received;
        this.time = msg.time;
        this.local = msg.local;
        this.remote = msg.remote;
        this.contents = msg.contents;
        this.message = msg.message;
    }

    /***************************************************************************
     * @param <T>
     * @return
     **************************************************************************/
    public <T> T getParsedMessage()
    {
        @SuppressWarnings( "unchecked")
        T msg = ( T )message;
        return msg;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return String.format( "%s, %s, %s, %s, %s, %s", received, time, local,
            remote, HexUtils.toHexString( contents, " " ), message );
    }
}
