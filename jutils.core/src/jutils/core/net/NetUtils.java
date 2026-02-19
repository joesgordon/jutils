package jutils.core.net;

import java.awt.Component;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import jutils.core.OptionUtils;
import jutils.core.net.NicInfo.AddressInfo;

/*******************************************************************************
 * Utility class for general network functions.
 ******************************************************************************/
public final class NetUtils
{
    /** The number of hextets (16-bit values) in an IPv6 address. */
    public static final int HEXTET_COUNT = 8;
    /** The number of bytes in an IPv4 address. */
    public static final int IPV4_SIZE = 4;
    /** The number of bytes in an IPv6 address. */
    public static final int IPV6_SIZE = 2 * HEXTET_COUNT;

    /** Integer value for IPv4 loopback, 127.0.0.1. */
    public static final int IPV4_LOOPBACK = 0x7F000001;
    /** IPv6 bytes for {@code ANY}. */
    public static final byte [] IPV6_ANY = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0 };
    /** Bytes for IPv6 loopback ::1 */
    public static final byte [] IPV6_LOOPBACK = new byte[] { 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 1 };

    /** The lowest integer value for IPv4 multicast groups. */
    public static final int IP4_MIN_GROUP = 0xE0000000;
    /** The highest integer value for IPv4 multicast groups. */
    public static final int IP4_MAX_GROUP = 0xEFFFFFFF;

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private NetUtils()
    {
    }

    /***************************************************************************
     * Creates a list of all network interfaces on this system.
     * @return the list of all network interfaces found.
     * @throws RuntimeException if any {@link SocketException} is thrown. See
     * {@link NetworkInterface#getNetworkInterfaces()}.
     **************************************************************************/
    public static List<NetworkInterface> listAllNicInterfaces()
        throws RuntimeException
    {
        List<NetworkInterface> nics = new ArrayList<>();
        Enumeration<NetworkInterface> nets;

        try
        {
            nets = NetworkInterface.getNetworkInterfaces();

            for( NetworkInterface nic : Collections.list( nets ) )
            {
                nics.add( nic );
            }
        }
        catch( SocketException ex )
        {
            throw new RuntimeException( ex.getMessage(), ex );
        }

        return nics;
    }

    /***************************************************************************
     * List all interfaces that are up or have addresses.
     * @return the list of connected interfaces.
     **************************************************************************/
    public static List<NicInfo> listNics()
    {
        List<NicInfo> nics = new ArrayList<>();

        for( NetworkInterface nic : listAllNicInterfaces() )
        {
            NicInfo info = new NicInfo( nic );

            if( info.isUp || !info.ipAddresses.isEmpty() )
            {
                nics.add( info );
            }
        }

        return nics;
    }

    /***************************************************************************
     * Lists all local addresses found.
     * @return the list of all local addresses.
     **************************************************************************/
    public static List<IpAddress> listLocalAddresses()
    {
        return listLocalAddresses( null );
    }

    /***************************************************************************
     * Lists all local addresses of the provided version found.
     * @param version either IPv4, IPv6, or {@code null} for both.
     * @return the list of local addresses.
     **************************************************************************/
    public static List<IpAddress> listLocalAddresses( IpVersion version )
    {
        List<IpAddress> addresses = new ArrayList<>();
        List<NicInfo> nics = NetUtils.listNics();

        for( NicInfo nic : nics )
        {
            for( AddressInfo ai : nic.ipAddresses )
            {
                IpAddress ip = ai.ip;

                if( version == null || version == ip.getVersion() )
                {
                    addresses.add( ip );
                }
            }
        }

        return addresses;
    }

    /***************************************************************************
     * Tests if the provided addresses are in the same subnet.
     * @param addr1 the first address to be checked.
     * @param addr2 the second address to be checked.
     * @param maskLen the length of the subnet bits.
     * @return {@code true} if the addresses are on the same subnet.
     **************************************************************************/
    public static boolean isSubnet( IpAddress addr1, IpAddress addr2,
        short maskLen )
    {
        byte [] bytes1 = addr1.get();
        byte [] bytes2 = addr2.get();

        if( bytes1.length != bytes2.length )
        {
            return false;
        }

        int maskBytes = ( maskLen + 7 ) / 8;

        if( maskBytes > bytes1.length )
        {
            return false;
        }

        for( int i = 0; i < maskBytes; i++ )
        {
            byte b1 = bytes1[i];
            byte b2 = bytes2[i];
            int remainingLen = maskLen - ( 8 * i );
            int mask = remainingLen > 7 ? 0xFF : ~( ( 1 << remainingLen ) - 1 );

            b1 &= mask;
            b2 &= mask;

            if( b1 != b2 )
            {
                return false;
            }
        }

        return true;
    }

    /***************************************************************************
     * Pings the provided address and displays a message saying whether it can
     * or cannot be reached OR the error that occurred by trying
     * @param ip the address to ping.
     * @param timeout the timeout to be used.
     * @param parent the parent component of the dialog to be displayed.
     **************************************************************************/
    public static void ping( IpAddress ip, int timeout, Component parent )
    {
        InetAddress addr = ip.getInetAddress();

        try
        {
            if( addr.isReachable( timeout ) )
            {
                OptionUtils.showInfoMessage( parent,
                    ip.toString() + " is reachable", "Success" );
            }
            else
            {
                OptionUtils.showWarningMessage( parent,
                    ip.toString() + " cannot be reached", "Failed" );
            }
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( parent, ex.getMessage(),
                "Unable to Ping " + ip.toString() );
        }
    }

    /***************************************************************************
     * Opens a UDP socket with the provided inputs.
     * @param inputs the configuration of the socket.
     * @return the created, open socket.
     * @throws IOException any error that occurs.
     **************************************************************************/
    public static UdpSocket openUdpSocket( UdpConfig inputs ) throws IOException
    {
        UdpSocket socket = new UdpSocket();

        if( inputs.multicast.isUsed && inputs.multicast.data == null )
        {
            throw new IOException( "Multicast group not specified" );
        }

        // LogUtils.printDebug( "NIC: " + inputs.nic );
        // LogUtils.printDebug( "Address: " + nicAddr );
        // LogUtils.printDebug( "Local Port: " + inputs.localPort );
        // LogUtils.printDebug( "Remote Port: " + inputs.remotePort );
        // LogUtils.printDebug( "" );

        InetAddress nicAddr = inputs.nic.getInetAddress();
        IpAddress localIp = new IpAddress();

        localIp.setInetAddress( nicAddr );

        EndPoint localPoint = new EndPoint( localIp, inputs.localPort );

        if( inputs.multicast.isUsed )
        {
            IpAddress group = inputs.multicast.data;

            socket.open();
            socket.setReuseAddress( true );
            socket.setTimeToLive( inputs.ttl );
            socket.bind( localPoint );
            socket.setLoopback( inputs.loopback );
            socket.joinGroup( group, localIp );
        }
        else
        {
            socket.open();
            socket.setReuseAddress( inputs.reuse );
            socket.setBroadcast( inputs.broadcast );
            socket.bind( localPoint );
        }

        socket.setReceiveTimeout( inputs.timeout );

        return socket;
    }
}
