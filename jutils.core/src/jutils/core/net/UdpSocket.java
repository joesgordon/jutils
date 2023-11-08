package jutils.core.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.StandardSocketOptions;
import java.util.Arrays;

import jutils.core.io.LogUtils;

/*******************************************************************************
 * Defines a socket for UDP communications.
 ******************************************************************************/
public class UdpSocket
{
    /** The default time to live as */
    public static final int DEFAULT_TTL = 5;

    /**  */
    private final byte [] rxBuffer;

    /**  */
    private DatagramSocket socket;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpSocket()
    {
        this( null );
    }

    /***************************************************************************
     * @param socket
     **************************************************************************/
    public UdpSocket( DatagramSocket socket )
    {
        this.socket = socket;
        this.rxBuffer = new byte[65536];
    }

    /***************************************************************************
     * @param local
     * @throws SocketException
     **************************************************************************/
    public void open() throws SocketException
    {
        socket = new DatagramSocket( null );
    }

    /***************************************************************************
     * @param local
     * @throws SocketException
     **************************************************************************/
    public void bind( EndPoint local ) throws SocketException
    {
        socket.bind( local.getInetSocketAddress() );
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void close() throws IOException
    {
        socket.close();
    }

    /***************************************************************************
     * @param contents
     * @param remote
     * @return
     * @throws IOException
     **************************************************************************/
    public NetMessage send( byte [] contents, EndPoint remote )
        throws IOException
    {
        return send( contents, remote.address, remote.port );
    }

    /***************************************************************************
     * @param contents
     * @param address
     * @param port
     * @return
     * @throws IOException
     **************************************************************************/
    public NetMessage send( byte [] contents, IpAddress address, int port )
        throws IOException
    {
        return send( contents, address.getInetAddress(), port );
    }

    /***************************************************************************
     * @param contents
     * @param toAddr
     * @param toPort
     * @return
     * @throws IOException
     **************************************************************************/
    public NetMessage send( byte [] contents, InetAddress toAddr, int toPort )
        throws IOException
    {
        DatagramPacket packet = new DatagramPacket( contents, contents.length,
            toAddr, toPort );
        EndPoint remote = new EndPoint( toAddr, toPort );

        socket.send( packet );

        return new NetMessage( false, getLocal(), remote, contents );
    }

    /***************************************************************************
     * @return
     * @throws IOException
     * @throws SocketTimeoutException
     **************************************************************************/
    public NetMessage receive() throws IOException, SocketTimeoutException
    {
        DatagramPacket packet = new DatagramPacket( rxBuffer, rxBuffer.length );

        socket.receive( packet );

        byte [] contents = Arrays.copyOf( rxBuffer, packet.getLength() );
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        EndPoint local = getLocal();
        EndPoint remote = new EndPoint( address, port );
        NetMessage msg = new NetMessage( true, local, remote, contents );

        return msg;
    }

    /***************************************************************************
     * @param milliseconds
     * @return
     **************************************************************************/
    public boolean setReceiveTimeout( int milliseconds )
    {
        try
        {
            socket.setSoTimeout( milliseconds );
            return true;
        }
        catch( SocketException ex )
        {
            return false;
        }
    }

    /***************************************************************************
     * @param reuse
     * @return
     **************************************************************************/
    public boolean setReuseAddress( boolean reuse )
    {
        try
        {
            socket.setReuseAddress( reuse );
            return true;
        }
        catch( SocketException ex )
        {
            return false;
        }
    }

    /***************************************************************************
     * @param broadcast
     * @return
     **************************************************************************/
    public boolean setBroadcast( boolean broadcast )
    {
        try
        {
            socket.setBroadcast( broadcast );
            return true;
        }
        catch( SocketException ex )
        {
            return false;
        }
    }

    /***************************************************************************
     * @param ttl
     * @return
     **************************************************************************/
    public boolean setTimeToLive( int ttl )
    {
        try
        {
            // ( ( MulticastSocket )socket ).setTimeToLive( ttl );
            socket = socket.setOption( StandardSocketOptions.IP_MULTICAST_TTL,
                ttl );
            return true;
        }
        catch( IOException ex )
        {
            return false;
        }
        catch( ClassCastException ex )
        {
            return false;
        }
    }

    /***************************************************************************
     * @param loopback
     * @return
     **************************************************************************/
    public boolean setLoopback( boolean loopback )
    {
        try
        {
            // socket.setLoopbackMode( !loopback );
            socket = socket.setOption( StandardSocketOptions.IP_MULTICAST_LOOP,
                loopback );
            return true;
        }
        catch( IOException ex )
        {
            return false;
        }
    }

    public boolean joinGroup( IpAddress group )
    {
        return joinGroup( group, new IpAddress() );
    }

    /***************************************************************************
     * @param group
     * @param nic
     * @return
     **************************************************************************/
    public boolean joinGroup( IpAddress group, IpAddress nic )
    {
        int port = socket.getLocalPort();

        try
        {
            InetAddress nicAddress = nic.getInetAddress();
            InetAddress mcAddress = group.getInetAddress();
            SocketAddress mcSockAddress = new InetSocketAddress( mcAddress,
                port );
            NetworkInterface nicNi = NetworkInterface.getByInetAddress(
                nicAddress );

            socket.joinGroup( mcSockAddress, nicNi );
            return true;
        }
        catch( IOException ex )
        {
            LogUtils.printWarning( "Unable to join %s:%d on %s", group, port,
                nic );
            return false;
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public EndPoint getLocal()
    {
        EndPoint ep = new EndPoint();

        ep.address.setInetAddress( socket.getLocalAddress() );
        ep.port = socket.getLocalPort();

        return ep;
    }

    /***************************************************************************
     * @throws SocketException
     **************************************************************************/
    public void openUnicast() throws SocketException
    {
        openUnicast( 0 );
    }

    /***************************************************************************
     * @param port
     * @throws SocketException
     **************************************************************************/
    public void openUnicast( int port ) throws SocketException
    {
        openUnicast( new EndPoint( port ) );
    }

    /***************************************************************************
     * @param endPoint
     * @throws SocketException
     **************************************************************************/
    public void openUnicast( EndPoint endPoint ) throws SocketException
    {
        open();
        bind( endPoint );
    }

    /***************************************************************************
     * @param port
     * @throws SocketException
     **************************************************************************/
    public void openMulticast( int port ) throws SocketException
    {
        openMulticast( port, false );
    }

    /***************************************************************************
     * @param port
     * @param loopback
     * @throws SocketException
     **************************************************************************/
    private void openMulticast( int port, boolean loopback )
        throws SocketException
    {
        openMulticast( port, loopback, DEFAULT_TTL );
    }

    /***************************************************************************
     * @param port
     * @param loopback
     * @param ttl
     * @throws SocketException
     **************************************************************************/
    private void openMulticast( int port, boolean loopback, int ttl )
        throws SocketException
    {
        EndPoint localPoint = new EndPoint( port );

        open();
        setReuseAddress( true );
        setTimeToLive( ttl );
        bind( localPoint );
        setLoopback( loopback );
    }

    /***************************************************************************
     * @param group
     * @param port
     * @throws SocketException
     **************************************************************************/
    public void openMulticast( IpAddress group, int port )
        throws SocketException
    {
        openMulticast( group, port, new IpAddress() );
    }

    /***************************************************************************
     * @param group
     * @param port
     * @param nic
     * @throws SocketException
     **************************************************************************/
    public void openMulticast( IpAddress group, int port, IpAddress nic )
        throws SocketException
    {
        openMulticast( group, port, nic, nic.isLoopback() );
    }

    /***************************************************************************
     * @param group
     * @param port
     * @param nic
     * @param loopback
     * @throws SocketException
     **************************************************************************/
    private void openMulticast( IpAddress group, int port, IpAddress nic,
        boolean loopback ) throws SocketException
    {
        openMulticast( group, port, nic, loopback, DEFAULT_TTL );
    }

    /***************************************************************************
     * @param local
     * @param group
     * @param port
     * @param ttl
     * @param loopback
     * @param group
     * @param nic
     * @throws SocketException
     **************************************************************************/
    public void openMulticast( IpAddress group, int port, IpAddress nic,
        boolean loopback, int ttl ) throws SocketException
    {
        openMulticast( port, loopback, ttl );
        joinGroup( group, nic );
    }
}
