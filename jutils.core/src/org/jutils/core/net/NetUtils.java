package org.jutils.core.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.jutils.core.Utils;
import org.jutils.core.ValidationException;

/*******************************************************************************
 * Utility class for general network functions.
 ******************************************************************************/
public final class NetUtils
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private NetUtils()
    {
    }

    /***************************************************************************
     * @param nic
     * @return
     **************************************************************************/
    public static String getIpv4( NetworkInterface nic )
    {
        Enumeration<InetAddress> addresses = nic.getInetAddresses();

        while( addresses.hasMoreElements() )
        {
            InetAddress address = addresses.nextElement();

            if( address instanceof Inet4Address )
            {
                return address.getHostAddress();
            }
        }

        return null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<NicInfo> buildNicList()
    {
        List<NicInfo> nics = new ArrayList<>();
        Enumeration<NetworkInterface> nets;

        nics.add( NicInfo.createAny() );

        try
        {
            nets = NetworkInterface.getNetworkInterfaces();

            for( NetworkInterface nic : Collections.list( nets ) )
            {
                if( nic.isUp() )
                {
                    Enumeration<InetAddress> addresses = nic.getInetAddresses();
                    while( addresses.hasMoreElements() )
                    {
                        InetAddress address = addresses.nextElement();
                        nics.add( new NicInfo( nic, address ) );
                    }
                }
            }
        }
        catch( SocketException ex )
        {
            throw new RuntimeException( ex.getMessage(), ex );
        }

        return nics;
    }

    /**
     * @param nicString
     * @return
     */
    public static NicInfo lookupInfo( String nicString )
    {
        NicInfo info = null;
        List<NicInfo> nics = buildNicList();

        if( nicString == null )
        {
            info = getAnyInfo();
        }
        else
        {
            for( NicInfo ni : nics )
            {
                if( ni.name.toLowerCase().equals( nicString.toLowerCase() ) )
                {
                    info = ni;
                    break;
                }
                else if( ni.addressString.equals( nicString ) )
                {
                    info = ni;
                    break;
                }
            }

            if( info == null )
            {
                List<String> octects = Utils.split( nicString, '.' );

                if( octects.size() < 3 )
                {
                    return null;
                }

                String firstThree = octects.get( 0 ) + "." + octects.get( 1 ) +
                    "." + octects.get( 2 ) + ".";

                for( NicInfo ni : nics )
                {
                    if( ni.addressString.startsWith( firstThree ) )
                    {
                        info = ni;
                        break;
                    }
                }
            }
        }

        return info;
    }

    /***************************************************************************
     * @param nicString
     * @return
     **************************************************************************/
    public static InetAddress lookupNetAddress( String nicString )
    {
        InetAddress address = null;
        List<NicInfo> nics = buildNicList();

        if( nicString == null )
        {
            try
            {
                address = InetAddress.getByAddress( new byte[] { 0, 0, 0, 0 } );
            }
            catch( UnknownHostException ex )
            {
                address = null;
            }
        }
        else
        {
            for( NicInfo info : nics )
            {
                if( info.name.toLowerCase().equals( nicString.toLowerCase() ) )
                {
                    address = info.address;
                    break;
                }
                else if( info.addressString.equals( nicString ) )
                {
                    address = info.address;
                    break;
                }
            }

            if( address == null )
            {
                List<String> octects = Utils.split( nicString, '.' );

                if( octects.size() < 3 )
                {
                    return null;
                }

                String firstThree = octects.get( 0 ) + "." + octects.get( 1 ) +
                    "." + octects.get( 2 ) + ".";

                for( NicInfo info : nics )
                {
                    if( info.addressString.startsWith( firstThree ) )
                    {
                        address = info.address;
                    }
                }
            }
        }

        return address;
    }

    /***************************************************************************
     * @param nicString
     * @param name
     * @throws ValidationException
     **************************************************************************/
    public static void validateNicString( String nicString, String name )
        throws ValidationException
    {
        if( lookupNetAddress( nicString ) == null )
        {
            throw new ValidationException(
                "No Network Interface found for string \"" + name + "\"" );
        }
    }

    /***************************************************************************
     * Returns the first {@link InetAddress} of the network interface name
     * provided or dies if the JRE is strange.
     * @param nicName the name of the network interface to be found.
     * @return The first address of the network interface of the provided name
     * or {@code null} if none found.
     * @throws RuntimeException if the implementing JRE is so strange as to not
     * recognize 0.0.0.0 as a valid address or when a {@link SocketException} is
     * thrown on {@link NetworkInterface#getByName(String)}. The former should
     * never happen and it is unclear from the documentation when/why the latter
     * would happen.
     **************************************************************************/
    public static InetAddress getNetAddress( String nicName )
        throws RuntimeException
    {
        InetAddress nicAddr = null;

        if( nicName != null )
        {
            try
            {
                NetworkInterface nic = NetworkInterface.getByName( nicName );

                nicAddr = getNetAddress( nic );
            }
            catch( SocketException ex )
            {
                throw new RuntimeException(
                    "0.0.0.0 not recognized as a valid address", ex );
            }
        }

        if( nicAddr == null )
        {
            nicAddr = getAnyNetAddress();
        }

        return nicAddr;
    }

    /***************************************************************************
     * Gets the first address of the provided network interface.
     * @param nic the network interface to get the address of.
     * @return The first address of the network interface of the provided name
     * or {@code null} if none found or privoded interface is {@code null}.
     **************************************************************************/
    public static InetAddress getNetAddress( NetworkInterface nic )
    {
        if( nic != null )
        {
            Enumeration<InetAddress> addresses = nic.getInetAddresses();

            if( addresses.hasMoreElements() )
            {
                return addresses.nextElement();
            }
        }

        return null;
    }

    /***************************************************************************
     * @return
     * @throws RuntimeException if the implementing JRE is so strange as to not
     * recognize 0.0.0.0 as a valid address.
     **************************************************************************/
    public static InetAddress getAnyNetAddress()
    {
        byte [] inAddrAnyBytes = new byte[] { 0, 0, 0, 0 };

        try
        {
            return InetAddress.getByAddress( inAddrAnyBytes );
        }
        catch( UnknownHostException ex )
        {
            throw new RuntimeException(
                "0.0.0.0 not recognized as a valid address", ex );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static NicInfo getAnyInfo()
    {
        InetAddress address = getAnyNetAddress();
        try
        {
            NetworkInterface nic = NetworkInterface.getByInetAddress( address );
            NicInfo info = new NicInfo( nic, address );

            return info;
        }
        catch( SocketException ex )
        {
            throw new IllegalStateException(
                "ANY address not found: " + ex.getMessage() );
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    public static class NicInfo
    {
        /**  */
        public final NetworkInterface nic;
        /**  */
        public final InetAddress address;

        /**  */
        public final String name;
        /**  */
        public final String addressString;
        /**  */
        public final boolean isIpv4;

        /**
         * @param nic {@code null} indicates Any.
         * @param address the address of the provided NIC may not be null.
         */
        public NicInfo( NetworkInterface nic, InetAddress address )
        {
            this.nic = nic;
            this.name = nic != null ? nic.getDisplayName() : "Any";
            this.address = address;
            this.addressString = address.getHostAddress();
            this.isIpv4 = address instanceof Inet4Address;
        }

        /**
         * @return
         */
        public static NicInfo createAny()
        {
            try
            {
                byte [] bytes = new byte[] { 0, 0, 0, 0 };
                InetAddress address = InetAddress.getByAddress( bytes );

                return new NicInfo( null, address );
            }
            catch( UnknownHostException ex )
            {
                throw new RuntimeException(
                    "0.0.0.0 was identified as a bad address" );
            }
        }
    }
}