package jutils.core.net;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpServerConfig
{
    /**  */
    public static final int DEFAULT_TIMEOUT = 0;
    /**  */
    public static final int DEFAULT_BACKLOG = 1;

    /**  */
    public final EndPoint local;
    /**
     * The requested maximum length of the queue of incoming connections.
     */
    public int backlog;
    /**
     * The number of milliseconds to block for communications. Must be > -1. 0
     * (default) is interpreted as an infinite timeout.
     */
    public int timeout;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpServerConfig()
    {
        this.local = new EndPoint();
        this.backlog = DEFAULT_BACKLOG;
        this.timeout = DEFAULT_TIMEOUT;
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public TcpServerConfig( TcpServerConfig config )
    {
        this();

        if( config != null )
        {
            set( config );
        }
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public void set( TcpServerConfig config )
    {
        local.set( config.local );
        backlog = config.backlog;
        timeout = config.timeout;
    }
}
