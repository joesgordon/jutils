package org.jutils.core.net;

import org.jutils.core.utils.Usable;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpInputs
{
    /**  */
    public int localPort;
    /**  */
    public String nic;
    /**  */
    public boolean broadcast;
    /**  */
    public Usable<IpAddress> multicast;
    /**  */
    public int timeout;
    /**  */
    public boolean reuse;
    /**  */
    public boolean loopback;
    /**  */
    public int ttl;
    /**  */
    public final IpAddress remoteAddress;
    /**  */
    public int remotePort;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpInputs()
    {
        this.localPort = 0;
        this.nic = null;
        this.broadcast = false;
        this.multicast = new Usable<>( false, new IpAddress( 224, 0, 0, 1 ) );
        this.timeout = 500;
        this.reuse = true;
        this.loopback = false;
        this.ttl = 2;

        this.remoteAddress = new IpAddress( 127, 0, 0, 1 );
        this.remotePort = 5000;
    }

    /***************************************************************************
     * @param inputs
     **************************************************************************/
    public UdpInputs( UdpInputs inputs )
    {
        this.localPort = inputs.localPort;
        this.nic = inputs.nic;
        this.broadcast = inputs.broadcast;
        this.multicast = new Usable<>( inputs.multicast );
        this.timeout = inputs.timeout;
        this.reuse = inputs.reuse;
        this.loopback = inputs.loopback;
        this.ttl = inputs.ttl;

        this.remoteAddress = new IpAddress( inputs.remoteAddress );
        this.remotePort = inputs.remotePort;
    }
}
