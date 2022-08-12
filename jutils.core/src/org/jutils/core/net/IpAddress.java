package org.jutils.core.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.jutils.core.INamedItem;
import org.jutils.core.Utils;

/*******************************************************************************
 *
 ******************************************************************************/
public abstract class IpAddress
{
    /**  */
    public final byte [] address;

    /***************************************************************************
     * @param version
     **************************************************************************/
    protected IpAddress( IpVersion version )
    {
        this.address = new byte[version.byteCount];
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte [] get()
    {
        byte [] address = new byte[this.address.length];

        Utils.byteArrayCopy( this.address, 0, address, 0, address.length );

        return address;
    }

    /***************************************************************************
     * @param address
     * @throws ArrayIndexOutOfBoundsException
     **************************************************************************/
    public void set( byte [] address ) throws ArrayIndexOutOfBoundsException
    {
        Utils.byteArrayCopy( address, 0, this.address, 0, address.length );
    }

    /***************************************************************************
     * @param address
     **************************************************************************/
    public void set( IpAddress address )
    {
        set( address.address );
    }

    /***************************************************************************
     * @param address
     * @throws ArrayIndexOutOfBoundsException
     **************************************************************************/
    public void setInetAddress( InetAddress address )
        throws ArrayIndexOutOfBoundsException
    {
        byte [] octets = address.getAddress();

        set( octets );
    }

    /***************************************************************************
     * @return
     * @throws UnknownHostException
     **************************************************************************/
    public InetAddress getInetAddress() throws UnknownHostException
    {
        return InetAddress.getByAddress( address );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IpVersion getVersion()
    {
        return address.length == 4 ? IpVersion.IPV4
            : ( address.length == 6 ? IpVersion.IPV6 : null );
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
        else if( obj instanceof Ip4Address )
        {
            Ip4Address address = ( Ip4Address )obj;

            return Arrays.equals( this.address, address.address );
        }

        return false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return Arrays.hashCode( address );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public abstract String toString();

    /***************************************************************************
     *
     **************************************************************************/
    public enum IpVersion implements INamedItem
    {
        /**  */
        IPV4( 4, "IPv4" ),
        /**  */
        IPV6( 8, "IPv6" );

        /**  */
        public final int byteCount;
        /**  */
        public final String name;

        /**
         * @param byteCount
         * @param name
         */
        private IpVersion( int byteCount, String name )
        {
            this.byteCount = byteCount;
            this.name = name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return name;
        }
    }
}
