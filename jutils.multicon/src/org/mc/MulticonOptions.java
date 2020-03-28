package org.mc;

import org.jutils.core.net.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticonOptions
{
    /**  */
    public TcpInputs tcpServerInputs;
    /**  */
    public TcpInputs tcpClientInputs;
    /**  */
    public UdpInputs udpInputs;
    /**  */
    public MulticastInputs multicastInputs;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticonOptions()
    {
        this.tcpServerInputs = new TcpInputs();
        this.tcpClientInputs = new TcpInputs();
        this.udpInputs = new UdpInputs();
        this.multicastInputs = new MulticastInputs();
    }

    /***************************************************************************
     * @param options the options to be copied.
     **************************************************************************/
    public MulticonOptions( MulticonOptions options )
    {
        this.tcpServerInputs = options.tcpServerInputs == null ? new TcpInputs()
            : new TcpInputs( options.tcpServerInputs );
        this.tcpClientInputs = options.tcpClientInputs == null ? new TcpInputs()
            : new TcpInputs( options.tcpClientInputs );
        this.udpInputs = options.udpInputs == null ? new UdpInputs()
            : new UdpInputs( options.udpInputs );
        this.multicastInputs = options.multicastInputs == null
            ? new MulticastInputs()
            : new MulticastInputs( options.multicastInputs );
    }
}
