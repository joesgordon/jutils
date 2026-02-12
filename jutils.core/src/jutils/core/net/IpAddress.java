package jutils.core.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import jutils.core.Utils;
import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.ui.hex.HexUtils;

/*******************************************************************************
 * Represents an IPv4 or IPv6 address.
 ******************************************************************************/
public class IpAddress
{
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
     * @param ipv4Value
     **************************************************************************/
    public IpAddress( int ipv4Value )
    {
        this( IpVersion.IPV4 );

        this.setValue( ipv4Value );
    }

    /***************************************************************************
     * Creates an IP address of the specified version initialized to ANY
     * (0.0.0.0 or ::0).
     * @param version the version of IP address to create.
     **************************************************************************/
    public IpAddress( IpVersion version )
    {
        this.address = new byte[NetUtils.IPV6_SIZE];
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
     * Creates an IPv4 address with the specified value.
     * @param value the address of length {@link #IPV4_SIZE} or
     * {@link #IPV6_SIZE}.
     * @throws IllegalArgumentException if the provided address is not of length
     * {@link #IPV4_SIZE} or {@link #IPV6_SIZE}.
     **************************************************************************/
    public IpAddress( byte [] value ) throws IllegalArgumentException
    {
        this( IpVersion.IPV4 );

        this.set( value );
    }

    /***************************************************************************
     * Creates a copy of the specified IP address.
     * @param address IP to be copied.
     **************************************************************************/
    public IpAddress( IpAddress address )
    {
        IpVersion vers = null;
        byte [] addr = null;

        if( address.address == null && address.version == null )
        {
            vers = IpVersion.IPV4;
            addr = new byte[vers.byteCount];
        }
        else if( address.address == null )
        {
            vers = address.version;
            addr = new byte[vers.byteCount];
        }
        else if( address.version == null )
        {
            addr = address.address;

            switch( addr.length )
            {
                case NetUtils.IPV4_SIZE:
                    vers = IpVersion.IPV4;
                    break;

                case NetUtils.IPV6_SIZE:
                    vers = IpVersion.IPV6;
                    break;

                default:
                    throw new IllegalArgumentException(
                        "Cannot have an address of length " + addr.length +
                            ": " + HexUtils.toHexString( addr ) );
            }
        }
        else
        {
            addr = address.address;
            vers = address.version;
        }

        this.address = new byte[vers.byteCount];
        this.version = vers;

        Utils.byteArrayCopy( addr, 0, this.address, 0, vers.byteCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setAny()
    {
        Arrays.fill( address, ( byte )0 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setLoopback()
    {
        switch( version )
        {
            case IPV4:
                setValue( NetUtils.IPV4_LOOPBACK );
                break;

            case IPV6:
                set( NetUtils.IPV6_LOOPBACK );
                break;
        }
    }

    /***************************************************************************
     * Returns {@code true} if the address is the ANY address; {@code false}
     * otherwise.
     * @return if this address is the ANY address.
     **************************************************************************/
    public boolean isAny()
    {
        switch( version )
        {
            case IPV4:
                return 0 == getValue();

            case IPV6:
                return Arrays.equals( NetUtils.IPV6_ANY, address );
        }
        return false;
    }

    /***************************************************************************
     * Returns {@code true} if the address is a loopback address; {@code false}
     * otherwise.
     * @return if this address is a loopback address.
     **************************************************************************/
    public boolean isLoopback()
    {
        switch( version )
        {
            case IPV4:
                return NetUtils.IPV4_LOOPBACK == getValue();

            case IPV6:
                return Arrays.equals( NetUtils.IPV6_LOOPBACK, address );

            default:
                return false;
        }
    }

    /***************************************************************************
     * Returns {@code true} if the address is a multicast address; {@code false}
     * otherwise.
     * @return if this address is a multicast address.
     **************************************************************************/
    public boolean isMulticast()
    {
        switch( version )
        {
            case IPV4:
            {
                int value = this.getValue();

                return NetUtils.IP4_MIN_GROUP <= value &&
                    value <= NetUtils.IP4_MAX_GROUP;
            }

            case IPV6:
                return getHextet( 0 ) == 0xFF00;

            default:
                throw new IllegalStateException(
                    "Unknown IP version: " + version );
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
     * Returns the octet at the provided index.
     * @param index the 0-relative index of the octet to be returned.
     * @return the unsigned integer representation of the octet.
     **************************************************************************/
    public int getOctet( int index )
    {
        return Byte.toUnsignedInt( address[index] );
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
     * This IPv6 address as hextets.
     * @return an array of {@link #HEXTET_COUNT} hextets.
     * @throws IllegalStateException if this is not an IPv6 address.
     **************************************************************************/
    public short [] getHextets() throws IllegalStateException
    {
        if( version != IpVersion.IPV6 )
        {
            throw new IllegalStateException(
                "Cannot represent IPv4 as hextets" );
        }

        short [] value = new short[NetUtils.HEXTET_COUNT];

        for( int i = 0; i < NetUtils.HEXTET_COUNT; i++ )
        {
            value[i] = getHextet( i );
        }

        return value;
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public short getHextet( int index )
    {
        int ai = index * 2;

        int a1 = Byte.toUnsignedInt( address[ai] );
        int a2 = Byte.toUnsignedInt( address[ai + 1] );

        return ( short )( ( a1 << 8 ) | a2 );
    }

    /***************************************************************************
     * Sets this address to the provided hextets.
     * @param value the hextets to be set. The high 2 bytes of each integer are
     * unchecked and have no effect.
     * @throws IllegalArgumentException if the provided value is not of length
     * {@link #HEXTET_COUNT}.
     **************************************************************************/
    public void setHextets( short [] value ) throws IllegalArgumentException
    {
        byte [] addr = toOctets( value );

        set( addr );
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
            case NetUtils.IPV4_SIZE:
                v = IpVersion.IPV4;
                break;

            case NetUtils.IPV6_SIZE:
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
     * Sets this address to the provided address.
     * @param address the IP to set this address to.
     * @throws IllegalStateException if {@link InetAddress#getAddress()} returns
     * an array that is not of length {@link #IPV4_SIZE} or {@link #IPV6_SIZE}.
     **************************************************************************/
    public void setInetAddress( InetAddress address )
        throws IllegalStateException
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

        int value = 0;

        value |= ( address[0] & 0xFF ) << 24;
        value |= ( address[1] & 0xFF ) << 16;
        value |= ( address[2] & 0xFF ) << 8;
        value |= address[3] & 0xFF;

        return value;
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
        else if( this == obj )
        {
            return true;
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
     * @return the string representing this IPv4 address.
     **************************************************************************/
    private String toIpv4String()
    {
        return String.format( "%d.%d.%d.%d", address[0] & 0xFF,
            address[1] & 0xFF, address[2] & 0xFF, address[3] & 0xFF );
    }

    /***************************************************************************
     * https://www.geeksforgeeks.org/compression-of-ipv6-address/
     * https://techhub.hpe.com/eginfolib/networking/docs/switches/5950/5200-2220a_l3-ip-svcs_cg/content/472592411.htm
     * @return the string representing this IPv6 address.
     **************************************************************************/
    private String toIpv6String()
    {
        short [] hextets = getHextets();
        StringBuilder str = new StringBuilder();

        // winners
        int zeroCnt = 0;
        int zeroIdx = -1;

        // current
        int zc = 0;
        int zi = -1;

        for( int i = 0; i < hextets.length; i++ )
        {
            if( hextets[i] == 0 )
            {
                if( ( zi + zc ) != i )
                {
                    zi = i;
                    zc = 1;
                }
                else
                {
                    zc++;
                }
            }
            else
            {
                zc = 0;
                zi = -1;
            }

            if( zc > zeroCnt )
            {
                zeroCnt = zc;
                zeroIdx = zi;
            }
        }

        int fi = zeroIdx + zeroCnt;

        for( int i = 0; i < hextets.length; i++ )
        {
            if( i == zeroIdx )
            {
                if( i == 0 )
                {
                    str.append( "::" );
                }
                else
                {
                    str.append( ":" );
                }
            }
            else if( i > zeroIdx && i < fi )
            {
                // TODO finish function
            }
            else
            {
                String delim = i == 7 ? "" : ":";

                str.append( String.format( "%X%s", hextets[i], delim ) );
            }
        }

        return str.toString();
    }

    /***************************************************************************
     * @param value
     * @return
     * @throws IllegalArgumentException
     **************************************************************************/
    private static byte [] toOctets( short [] value )
        throws IllegalArgumentException
    {
        byte [] addr = new byte[NetUtils.IPV6_SIZE];

        if( value.length != NetUtils.HEXTET_COUNT )
        {
            throw new IllegalArgumentException(
                "Cannot set an IPv6 address with " + value.length +
                    " hextets" );
        }

        for( int i = 0; i < value.length; i++ )
        {
            int a = i * 2;

            addr[a] = ( byte )( ( value[i] >>> 8 ) & 0xFF );
            addr[a + 1] = ( byte )( ( value[i] >>> 0 ) & 0xFF );
        }

        return addr;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class IpAddressSerializer
        implements IDataSerializer<IpAddress>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public IpAddress read( IDataStream stream )
            throws IOException, ValidationException
        {
            IpAddress address = new IpAddress();

            read( address, stream );

            return address;
        }

        /**
         * @param address
         * @param stream
         * @throws IOException
         * @throws ValidationException
         */
        public void read( IpAddress address, IDataStream stream )
            throws IOException, ValidationException
        {
            byte ver = stream.read();
            IpVersion version = IpVersion.fromId( ver );

            if( version == null )
            {
                throw new ValidationException(
                    "Invalid IP version read: " + ver );
            }

            byte [] bytes = new byte[version.byteCount];
            stream.readFully( bytes );

            address.set( bytes );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( IpAddress address, IDataStream stream )
            throws IOException
        {
            stream.write( ( byte )address.version.byteCount );
            stream.write( address.address, 0, address.version.byteCount );
        }
    }
}
