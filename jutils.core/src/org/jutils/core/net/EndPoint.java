package org.jutils.core.net;

import java.net.InetAddress;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EndPoint
{
    /**  */
    public final IpAddress address;
    /**  */
    public int port;

    /***************************************************************************
     * 
     **************************************************************************/
    public EndPoint()
    {
        this.address = new IpAddress();
        this.port = 0;
    }

    /***************************************************************************
     * @param port
     **************************************************************************/
    public EndPoint( int port )
    {
        this();

        this.address.setOctets( 127, 0, 0, 1 );
        this.port = port;
    }

    /***************************************************************************
     * @param address
     * @param port
     **************************************************************************/
    public EndPoint( InetAddress address, int port )
    {
        this();

        this.address.setInetAddress( address );
        this.port = port;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return String.format( "%s:%d", address, port );
    }
}
