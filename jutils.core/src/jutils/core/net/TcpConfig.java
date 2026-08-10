package jutils.core.net;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpConfig
{
    /**  */
    public final EndPoint local;
    /**  */
    public final EndPoint remote;
    /**
     * The number of milliseconds to block for communications. Must be > -1. 0
     * is interpreted as an infinite timeout.
     */
    public int timeout;
    /**  */
    public boolean keepAlive;
    /**  */
    public boolean reuseAddress;
    /**  */
    public int linger;
    /**  */
    public boolean noDelay;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpConfig()
    {
        this.local = new EndPoint();
        this.remote = new EndPoint( new IpAddress( 127, 0, 0, 1 ), 0 );
        this.timeout = 500;
        this.keepAlive = true;
        this.reuseAddress = true;
        this.linger = -1;
        this.noDelay = false;
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public TcpConfig( TcpConfig config )
    {
        this();

        this.set( config );
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public void set( TcpConfig config )
    {
        this.local.set( config.local );
        this.remote.set( config.remote );
        this.timeout = config.timeout;
        this.keepAlive = config.keepAlive;
        this.reuseAddress = config.reuseAddress;
        this.linger = config.linger;
        this.noDelay = config.noDelay;
    }
}
