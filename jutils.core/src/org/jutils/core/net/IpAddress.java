package org.jutils.core.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.jutils.core.INamedItem;
import org.jutils.core.Utils;

/*******************************************************************************
 * Represents an IPv4 or IPv6 address.
 ******************************************************************************/
public class IpAddress
{
    /** The number of bytes in an IPv4 address. */
    public static final int IPV4_SIZE = 4;
    /** The number of bytes in an IPv6 address. */
    public static final int IPV6_SIZE = 16;

    /** IP address values (always IPv6 size). */
    public final byte [] address;
    /**  */
    private IpVersion version;

    /***************************************************************************
     * Creates an IPv4 address initialized to ANY (0.0.0.0).
     **************************************************************************/
    public IpAddress()
    {
        this( IpVersion.IPV4 );
    }

    /***************************************************************************
     * Creates an IP address of the specified version initialized to ANY
     * (0.0.0.0 or ::0).
     * @param version the version of IP address to create.
     **************************************************************************/
    public IpAddress( IpVersion version )
    {
        this.address = new byte[IPV6_SIZE];
        this.version = version;
    }

    /***************************************************************************
     * Creates an IPv4 address with the specified octets.
     * @param o1 octet 1
     * @param o2 octet 2
     * @param o3 octet 3
     * @param o4 octet 4
     **************************************************************************/
    public IpAddress( int o1, int o2, int o3, int o4 )
    {
        this( IpVersion.IPV4 );

        setOctets( o1, o2, o3, o4 );
    }

    /***************************************************************************
     * Creates a copy of the specified IP address.
     * @param address IP to be copied.
     **************************************************************************/
    public IpAddress( IpAddress address )
    {
        this.address = new byte[IPV6_SIZE];
        this.version = address.version;

        Utils.byteArrayCopy( address.address, 0, this.address, 0, IPV6_SIZE );
    }

    /***************************************************************************
     * Returns a copy of the address of length {@link #IPV4_SIZE} or
     * {@link #IPV6_SIZE} depending on the IP version.
     * @return a copy of the address.
     **************************************************************************/
    public byte [] get()
    {
        byte [] address = new byte[version.byteCount];

        Utils.byteArrayCopy( this.address, 0, address, 0, address.length );

        return address;
    }

    /***************************************************************************
     * Sets this address to the specified values using the length of values to
     * determine the IP version.
     * @param address the address of length {@link #IPV4_SIZE} or
     * {@link #IPV6_SIZE}.
     * @throws IllegalArgumentException if the provided address is not of length
     * {@link #IPV4_SIZE} or {@link #IPV6_SIZE}.
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
            Arrays.fill( this.address, ( byte )0 );
            Utils.byteArrayCopy( address, 0, this.address, 0, address.length );
            version = v;
        }
        catch( ArrayIndexOutOfBoundsException ex )
        {
            throw new IllegalStateException( ex );
        }
    }

    /***************************************************************************
     * Sets this address to the provided address.
     * @param address the IP to set this address to.
     **************************************************************************/
    public void set( IpAddress address )
    {
        try
        {
            set( address.get() );
        }
        catch( IllegalArgumentException ex )
        {
            throw new IllegalStateException(
                "Programming error as length has already been checked", ex );
        }
    }

    /***************************************************************************
     * Sets this address to the provided address.
     * @param address the IP to set this address to.
     **************************************************************************/
    public void setInetAddress( InetAddress address )
    {
        byte [] octets = address.getAddress();

        try
        {
            set( octets );
        }
        catch( IllegalArgumentException ex )
        {
            throw new IllegalStateException(
                "Programming error as length has already been checked", ex );
        }
    }

    /***************************************************************************
     * Creates an {@link InetAddress} using the byte values of this address.
     * @return the {@link InetAddress} representing this IP.
     **************************************************************************/
    public InetAddress getInetAddress()
    {
        try
        {
            return InetAddress.getByAddress( get() );
        }
        catch( UnknownHostException ex )
        {
            throw new IllegalStateException(
                "Programming error as length has already been checked", ex );
        }
    }

    /***************************************************************************
     * Sets this as an IPv4 address with the specified octets.
     * @param o1 octet 1
     * @param o2 octet 2
     * @param o3 octet 3
     * @param o4 octet 4
     **************************************************************************/
    public void setOctets( int o1, int o2, int o3, int o4 )
    {
        version = IpVersion.IPV4;

        Arrays.fill( this.address, ( byte )0 );

        address[0] = ( byte )o1;
        address[1] = ( byte )o2;
        address[2] = ( byte )o3;
        address[3] = ( byte )o4;
    }

    /***************************************************************************
     * Returns the integer representation of an IPv4 address in network byte
     * order.
     * @return the integer representation of an IPv4 address in network byte
     * order.
     * @throws IllegalStateException if this address is not an IPv4 address.
     **************************************************************************/
    public int getValue() throws IllegalStateException
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
     * Sets this address to the provided IPv4 address.
     * @param address an IPv4 address to set this address to.
     **************************************************************************/
    public void setValue( int address )
    {
        version = IpVersion.IPV4;

        Arrays.fill( this.address, ( byte )0 );

        this.address[0] = ( byte )( address >>> 24 );
        this.address[1] = ( byte )( address >>> 16 );
        this.address[2] = ( byte )( address >>> 8 );
        this.address[3] = ( byte )( address >>> 0 );
    }

    /***************************************************************************
     * Provides the IP version of this address.
     * @return the IP version of this address.
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
            IpAddress that = ( IpAddress )obj;

            return this.version == that.version &&
                Arrays.equals( this.address, that.address );
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

    /***************************************************************************
     * @return
     **************************************************************************/
    private String toIpv4String()
    {
        return String.format( "%d.%d.%d.%d", address[0] & 0xFF,
            address[1] & 0xFF, address[2] & 0xFF, address[3] & 0xFF );
    }

    /***************************************************************************
     * https://www.geeksforgeeks.org/compression-of-ipv6-address/
     * https://techhub.hpe.com/eginfolib/networking/docs/switches/5950/5200-2220a_l3-ip-svcs_cg/content/472592411.htm
     * @return
     **************************************************************************/
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
