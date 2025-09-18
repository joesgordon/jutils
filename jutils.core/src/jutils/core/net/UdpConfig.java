package jutils.core.net;

import jutils.core.utils.Usable;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpConfig
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

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpConfig()
    {
        this.localPort = 0;
        this.nic = null;
        this.broadcast = false;
        this.multicast = new Usable<>( false, new IpAddress( 224, 0, 0, 1 ) );
        this.timeout = 500;
        this.reuse = false;
        this.loopback = false;
        this.ttl = 2;
    }

    /***************************************************************************
     * @param inputs
     **************************************************************************/
    public UdpConfig( UdpConfig inputs )
    {
        this.localPort = inputs.localPort;
        this.nic = inputs.nic;
        this.broadcast = inputs.broadcast;
        this.multicast = new Usable<>( inputs.multicast );
        this.timeout = inputs.timeout;
        this.reuse = inputs.reuse;
        this.loopback = inputs.loopback;
        this.ttl = inputs.ttl;
    }
}
