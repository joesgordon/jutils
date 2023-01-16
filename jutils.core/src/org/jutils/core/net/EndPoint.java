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
     * Creates an end point at the provided address and port.
     * @param address the IP4 address of the end point.
     * @param port the port of the end point.
     **************************************************************************/
    public EndPoint( IpAddress address, int port )
    {
        this();

        this.address.set( address );
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
     * Sets this end point to the same values as the provided end point.
     * @param that the values to copy to this end point.
     **************************************************************************/
    public void set( EndPoint that )
    {
        this.address.set( that.address );
        this.port = that.port;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        else if( obj == this )
        {
            return true;
        }
        else if( obj instanceof EndPoint )
        {
            EndPoint that = ( EndPoint )obj;
            return this.address.equals( that.address ) &&
                this.port == that.port;
        }

        return false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return 31 * address.hashCode() + port;
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
