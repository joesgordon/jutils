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
    public final IpAddress nic;
    /**  */
    public boolean broadcast;
    /**  */
    public final Usable<IpAddress> multicast;
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
        this.nic = new IpAddress();
        this.broadcast = false;
        this.multicast = new Usable<>( false, new IpAddress( 224, 0, 0, 1 ) );
        this.timeout = 500;
        this.reuse = false;
        this.loopback = false;
        this.ttl = 2;
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public UdpConfig( UdpConfig config )
    {
        this();

        set( config );
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public void set( UdpConfig config )
    {
        this.localPort = config.localPort;
        if( config.nic != null )
        {
            this.nic.set( config.nic );
        }
        this.broadcast = config.broadcast;
        if( config.multicast != null )
        {
            this.multicast.set( config.multicast );
        }
        this.timeout = config.timeout;
        this.reuse = config.reuse;
        this.loopback = config.loopback;
        this.ttl = config.ttl;
    }
}
