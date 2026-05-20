package jutils.core.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jutils.core.data.Pair;
import jutils.core.io.LogUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpSocket
{
    /**  */
    private final byte [] rxBuffer;
    /**  */
    private final List<Pair<SocketAddress, NetworkInterface>> groups;

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
        this.groups = new ArrayList<>();
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
        for( Pair<SocketAddress, NetworkInterface> group : groups )
        {
            try
            {
                socket.leaveGroup( group.a, group.b );
            }
            catch( IOException ex )
            {
                ;
            }
        }

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
        // TODO create receive with buffer passed in.

        socket.receive( packet );
        byte [] contents = Arrays.copyOf( rxBuffer, packet.getLength() );
        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        return new NetMessage( true, getLocal(), new EndPoint( address, port ),
            contents );
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
             ( ( MulticastSocket )socket ).setTimeToLive( ttl );
//            socket = socket.setOption( StandardSocketOptions.IP_MULTICAST_TTL,
//                ttl );
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
        	//( ( MulticastSocket )socket ).setLoopbackMode( !loopback );
            socket = socket.setOption( StandardSocketOptions.IP_MULTICAST_LOOP,
                loopback );
            return true;
        }
        catch( IOException ex )
        {
            return false;
        }
    }

    /***************************************************************************
     * @param nic
     * @return
     **************************************************************************/
    public boolean setInterface( IpAddress nic )
    {
        InetAddress addr = nic.getInetAddress();

        try
        {
            NetworkInterface nif = NetworkInterface.getByInetAddress( addr );

            if( nif != null )
            {
                socket = socket.setOption(
                    StandardSocketOptions.IP_MULTICAST_IF, nif );
                return true;
            }

            String err = String.format(
                "Unable to set interface to %s: Interface not found", nic );
            LogUtils.printWarning( err );
        }
        catch( IOException ex )
        {
            String err = String.format( "Unable to set interface to %s: %s",
                nic, ex.getMessage() );
            LogUtils.printWarning( err );
            return false;
        }

        return false;
    }

    /***************************************************************************
     * @param group
     * @return
     **************************************************************************/
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
        try
        {
            InetAddress nicAddress = nic.getInetAddress();
            InetAddress mcAddress = group.getInetAddress();
            SocketAddress mcSockAddress = new InetSocketAddress( mcAddress, 0 );
            NetworkInterface nicNi = NetworkInterface.getByInetAddress(
                nicAddress );

            ( ( MulticastSocket )socket ).joinGroup( mcSockAddress, nicNi );

            groups.add( new Pair<>( mcSockAddress, nicNi ) );

            return true;
        }
        catch( IOException ex )
        {
            LogUtils.printWarning( "Unable to join %s on %s", group, nic );
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
}
