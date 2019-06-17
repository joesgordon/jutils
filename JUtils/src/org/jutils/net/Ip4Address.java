package org.jutils.net;

import java.net.*;
import java.util.*;

import org.jutils.Utils;
import org.jutils.io.LogUtils;
import org.jutils.utils.EnumerationIteratorAdapter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ip4Address
{
    /**  */
    public final byte [] address;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ip4Address()
    {
        this.address = new byte[4];
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

        set( f1, f2, f3, f4 );
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

        set( address );
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
     * @param f1
     * @param f2
     * @param f3
     * @param f4
     **************************************************************************/
    public void set( int f1, int f2, int f3, int f4 )
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
        Utils.byteArrayCopy( address.address, 0, this.address, 0, 4 );
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
     * @param address
     **************************************************************************/
    public void set( InetAddress address )
    {
        byte [] octects = address.getAddress();

        if( octects.length != this.address.length )
        {
            LogUtils.printError( "Invalid octet count: %d", octects.length );
        }

        for( int i = 0; i < this.address.length; i++ )
        {
            this.address[i] = octects[i];
        }
    }

    /***************************************************************************
     * @param address
     * @return
     **************************************************************************/
    public static Ip4Nic getNic( Ip4Address address )
    {
        Ip4Address mask = new Ip4Address( 255, 255, 255, 0 );

        return getNic( address, mask );
    }

    /***************************************************************************
     * @param address
     * @param mask
     * @return
     **************************************************************************/
    public static Ip4Nic getNic( Ip4Address address, Ip4Address mask )
    {
        String nicName = null;
        String loopbackName = null;
        Enumeration<NetworkInterface> nets;
        Ip4Address nicAddress = new Ip4Address( 127, 0, 0, 1 );

        try
        {
            nets = NetworkInterface.getNetworkInterfaces();

            for( NetworkInterface nic : Collections.list( nets ) )
            {
                if( nic.isLoopback() )
                {
                    loopbackName = nic.getName();
                }

                Enumeration<InetAddress> addresses = nic.getInetAddresses();
                Iterable<InetAddress> addressList = new EnumerationIteratorAdapter<>(
                    addresses );
                for( InetAddress ina : addressList )
                {
                    if( ina instanceof Inet4Address )
                    {
                        Inet4Address ina4 = ( Inet4Address )ina;
                        byte [] abs = ina4.getAddress();
                        boolean matched = true;

                        for( int i = 0; i < abs.length; i++ )
                        {
                            int no = Byte.toUnsignedInt( abs[i] );
                            int mo = Byte.toUnsignedInt( mask.address[i] );
                            int ao = Byte.toUnsignedInt( address.address[i] );

                            if( ( no & mo ) != ( ao & mo ) )
                            {
                                matched = false;
                                break;
                            }
                        }

                        if( matched )
                        {
                            nicName = nic.getName();
                            nicAddress.set( Byte.toUnsignedInt( abs[0] ),
                                Byte.toUnsignedInt( abs[1] ),
                                Byte.toUnsignedInt( abs[2] ),
                                Byte.toUnsignedInt( abs[3] ) );
                        }

                        break;
                    }
                }

                if( nicName != null )
                {
                    break;
                }
            }
        }
        catch( SocketException ex )
        {
            ;
        }

        Ip4Nic nic = nicName == null
            ? new Ip4Nic( new Ip4Address( 127, 0, 0, 1 ), loopbackName )
            : new Ip4Nic( nicAddress, nicName );

        return nic;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class Ip4Nic
    {
        /**  */
        public final Ip4Address address;
        /**  */
        public final String nic;

        /**
         * @param address
         * @param nic
         */
        public Ip4Nic( Ip4Address address, String nic )
        {
            this.address = address;
            this.nic = nic;
        }
    }
}
