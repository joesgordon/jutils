package jutils.core.net;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpInputs
{
    /**  */
    public final IpAddress nic;
    /**  */
    public int localPort;
    /**  */
    public final IpAddress remoteAddress;
    /**  */
    public int remotePort;
    /**
     * The number of milliseconds to block for communications. Must be > -1. 0
     * is interpreted as an infinite timeout.
     */
    public int timeout;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpInputs()
    {
        this.nic = new IpAddress();
        this.localPort = 0;
        this.remoteAddress = new IpAddress( 127, 0, 0, 1 );
        this.remotePort = 5000;
        this.timeout = 500;
    }

    /***************************************************************************
     * @param inputs
     **************************************************************************/
    public TcpInputs( TcpInputs inputs )
    {
        this.nic = inputs.nic;
        this.localPort = inputs.localPort;
        this.remoteAddress = new IpAddress( inputs.remoteAddress );
        this.remotePort = inputs.remotePort;
        this.timeout = inputs.timeout;
    }
}
