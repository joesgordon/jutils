package org.jutils.core.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.jutils.core.INamedItem;
import org.jutils.core.Utils;

/*******************************************************************************
 *
 ******************************************************************************/
public class IpAddress
{
    /**  */
    public static final int IPV4_SIZE = 4;
    /**  */
    public static final int IPV6_SIZE = 16;

    /**  */
    public final byte [] address;
    /**  */
    private IpVersion version;

    /***************************************************************************
     * 
     **************************************************************************/
    public IpAddress()
    {
        this( IpVersion.IPV4 );
    }

    /***************************************************************************
     * @param version
     **************************************************************************/
    public IpAddress( IpVersion version )
    {
        this.address = new byte[IPV6_SIZE];
        this.version = IpVersion.IPV4;
    }

    /***************************************************************************
     * @param f1
     * @param f2
     * @param f3
     * @param f4
     **************************************************************************/
    public IpAddress( int f1, int f2, int f3, int f4 )
    {
        this( IpVersion.IPV4 );

        setOctets( f1, f2, f3, f4 );
    }

    /***************************************************************************
     * @param address
     **************************************************************************/
    public IpAddress( IpAddress address )
    {
        this.address = new byte[IPV6_SIZE];
        this.version = address.version;

        Utils.byteArrayCopy( this.address, 0, address.address, 0,
            address.address.length );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte [] get()
    {
        byte [] address = new byte[version.byteCount];

        Utils.byteArrayCopy( this.address, 0, address, 0, address.length );

        return address;
    }

    /***************************************************************************
     * @param address
     * @throws ArrayIndexOutOfBoundsException
     * @throws IllegalArgumentException
     **************************************************************************/
    public void set( byte [] address ) throws IllegalArgumentException
    {
        IpVersion v = null;

        switch( address.length )
        {
            case IPV4_SIZE:
                v = IpVersion.IPV4;
                break;

            case IPV6_SIZE:
                v = IpVersion.IPV6;
                break;

            default:
                throw new IllegalArgumentException(
                    "Invalid address length: " + address.length );
        }

        try
        {
            Utils.byteArrayCopy( address, 0, this.address, 0, address.length );
            version = v;
        }
        catch( ArrayIndexOutOfBoundsException ex )
        {
            throw new IllegalStateException( ex );
        }
    }

    /***************************************************************************
     * @param address
     **************************************************************************/
    public void set( IpAddress address )
    {
        set( address.get() );
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
        return InetAddress.getByAddress( get() );
    }

    /***************************************************************************
     * @param f1
     * @param f2
     * @param f3
     * @param f4
     **************************************************************************/
    public void setOctets( int f1, int f2, int f3, int f4 )
    {
        version = IpVersion.IPV4;

        address[0] = ( byte )f1;
        address[1] = ( byte )f2;
        address[2] = ( byte )f3;
        address[3] = ( byte )f4;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getValue()
    {
        if( version != IpVersion.IPV4 )
        {
            throw new IllegalStateException(
                "Cannot represent IPv6 as an int" );
        }

        int addr = ( address[0] << 24 ) | ( address[1] << 16 ) |
            ( address[2] << 8 ) | ( address[3] << 0 );

        return addr;
    }

    /***************************************************************************
     * @param address
     **************************************************************************/
    public void setValue( int address )
    {
        version = IpVersion.IPV4;

        this.address[0] = ( byte )( address >>> 24 );
        this.address[1] = ( byte )( address >>> 16 );
        this.address[2] = ( byte )( address >>> 8 );
        this.address[3] = ( byte )( address >>> 0 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IpVersion getVersion()
    {
        return version;
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
        else if( obj instanceof IpAddress )
        {
            IpAddress address = ( IpAddress )obj;

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
    public String toString()
    {
        switch( version )
        {
            case IPV4:
                return toIpv4String();

            case IPV6:
                return toIpv6String();
        }

        return "";
    }

    /**
     * @return
     */
    private String toIpv4String()
    {
        return String.format( "%d.%d.%d.%d", address[0] & 0xFF,
            address[1] & 0xFF, address[2] & 0xFF, address[3] & 0xFF );
    }

    /**
     * https://www.geeksforgeeks.org/compression-of-ipv6-address/
     * https://techhub.hpe.com/eginfolib/networking/docs/switches/5950/5200-2220a_l3-ip-svcs_cg/content/472592411.htm
     * @return
     */
    private String toIpv6String()
    {
        // TODO Auto-generated method stub
        return String.format(
            "%02X%02X:%02X%02X:%02X%02X:%02X%02X:%02X%02X:%02X%02X:%02X%02X:%02X%02X",
            address[0] & 0xFF, address[1] & 0xFF, address[2] & 0xFF,
            address[3] & 0xFF, address[4] & 0xFF, address[5] & 0xFF,
            address[6] & 0xFF, address[7] & 0xFF, address[8] & 0xFF,
            address[9] & 0xFF, address[10] & 0xFF, address[11] & 0xFF,
            address[12] & 0xFF, address[13] & 0xFF, address[14] & 0xFF,
            address[15] & 0xFF );
    }

    /***************************************************************************
     *
     **************************************************************************/
    public enum IpVersion implements INamedItem
    {
        /**  */
        IPV4( IPV4_SIZE, "IPv4" ),
        /**  */
        IPV6( IPV6_SIZE, "IPv6" );

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
