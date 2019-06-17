package org.jutils.net;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Objects;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastInputs
{
    /** The multicast group used to send/receive. */
    public final Ip4Address group;
    /** The port to send/receive. */
    public int port;
    /** The nic to be bound to. */
    public String nic;
    /**
     * Indicates that the socket will receive the messages it sends or not. This
     * flag may be ignored by the underlying subsystem because standards are
     * great everyone should have one.
     */
    public boolean loopback;
    /** Time-to-Live */
    public int ttl;
    /** The timeout to use for send and receive. */
    public int timeout;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticastInputs()
    {
        this.group = new Ip4Address( 238, 0, 0, 1 );
        this.port = 2048;
        this.nic = null;
        this.ttl = 10;
        this.loopback = true;
        this.timeout = 500;
    }

    /***************************************************************************
     * @param inputs
     **************************************************************************/
    public MulticastInputs( MulticastInputs inputs )
    {
        this.group = new Ip4Address( inputs.group );
        this.port = inputs.port;
        this.nic = inputs.nic;
        this.ttl = inputs.ttl;
        this.loopback = inputs.loopback;
        this.timeout = inputs.timeout;
    }

    /***************************************************************************
     * @return
     * @throws SocketException
     **************************************************************************/
    public NetworkInterface getSystemNic() throws SocketException
    {
        return nic == null ? null : NetworkInterface.getByName( nic );
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        else if( obj instanceof MulticastInputs )
        {
            MulticastInputs inputs = ( MulticastInputs )obj;
            return group.equals( inputs.group ) && port == inputs.port &&
                nic == inputs.nic && ttl == inputs.ttl;
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( group, port, nic, ttl );
    }
}
