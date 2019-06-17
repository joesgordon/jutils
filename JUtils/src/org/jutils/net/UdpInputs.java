package org.jutils.net;

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
    public int timeout;
    /**  */
    public boolean reuse;
    /**  */
    public final Ip4Address remoteAddress;
    /**  */
    public int remotePort;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpInputs()
    {
        this.localPort = 0;
        this.nic = null;
        this.timeout = 500;
        this.reuse = true;
        this.remoteAddress = new Ip4Address( 127, 0, 0, 1 );
        this.remotePort = 5000;
    }

    /***************************************************************************
     * @param inputs
     **************************************************************************/
    public UdpInputs( UdpInputs inputs )
    {
        this.localPort = inputs.localPort;
        this.nic = inputs.nic;
        this.timeout = inputs.timeout;
        this.reuse = inputs.reuse;
        this.remoteAddress = inputs.remoteAddress;
        this.remotePort = inputs.remotePort;
    }
}
