package jutils.multicon.data;

import jutils.core.net.EndPoint;
import jutils.core.net.UdpConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpInputs
{
    /**  */
    public final UdpConfig config;
    /**  */
    public final EndPoint remote;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpInputs()
    {
        this.config = new UdpConfig();
        this.remote = new EndPoint();
    }

    /***************************************************************************
     * @param inputs
     **************************************************************************/
    public UdpInputs( UdpInputs inputs )
    {
        this();

        set( inputs );
    }

    /***************************************************************************
     * @param inputs
     **************************************************************************/
    public void set( UdpInputs inputs )
    {
        config.set( inputs.config );
        remote.set( inputs.remote );
    }
}
