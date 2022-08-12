package org.jutils.core.net;

import java.net.InetAddress;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ip4Address extends IpAddress
{

    /***************************************************************************
     * Creates a new IP v4 address initialized to INADDR_ANY.
     **************************************************************************/
    public Ip4Address()
    {
        super( IpVersion.IPV4 );
    }

    /***************************************************************************
     * @param f1
     * @param f2
     * @param f3
     * @param f4
     **************************************************************************/
    public Ip4Address( int f1, int f2, int f3, int f4 )
    {
        this();

        setOctets( f1, f2, f3, f4 );
    }

    /***************************************************************************
     * @param address
     **************************************************************************/
    public Ip4Address( Ip4Address address )
    {
        this();

        if( address != null && address.address != null )
        {
            set( address );
        }
    }

    /***************************************************************************
     * @param address
     **************************************************************************/
    public Ip4Address( InetAddress address )
    {
        this();

        setInetAddress( address );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getValue()
    {
        int addr = ( address[0] << 24 ) | ( address[1] << 16 ) |
            ( address[2] << 8 ) | ( address[3] << 0 );

        return addr;
    }

    /***************************************************************************
     * @param address
     **************************************************************************/
    public void setValue( int address )
    {
        this.address[0] = ( byte )( address >>> 24 );
        this.address[1] = ( byte )( address >>> 16 );
        this.address[2] = ( byte )( address >>> 8 );
        this.address[3] = ( byte )( address >>> 0 );
    }

    /***************************************************************************
     * @param f1
     * @param f2
     * @param f3
     * @param f4
     **************************************************************************/
    public void setOctets( int f1, int f2, int f3, int f4 )
    {
        address[0] = ( byte )f1;
        address[1] = ( byte )f2;
        address[2] = ( byte )f3;
        address[3] = ( byte )f4;
    }

    /***************************************************************************
     * @param address
     **************************************************************************/
    public void set( Ip4Address address )
    {
        set( address.address );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        String str = "";

        for( byte b : address )
        {
            if( !str.isEmpty() )
            {
                str += ".";
            }

            str += Integer.toString( Byte.toUnsignedInt( b ) );
        }

        return str;
    }
}
