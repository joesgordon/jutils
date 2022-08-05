package org.jutils.core.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.StandardSocketOptions;
import java.util.Arrays;

import org.jutils.core.net.NetUtils.NicInfo;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpConnection implements IConnection
{
    /**  */
    private final DatagramSocket socket;
    /**  */
    private final byte [] rxBuffer;

    /**  */
    private InetAddress remoteAddress;
    /**  */
    private int remotePort;

    /***************************************************************************
     * @param inputs
     * @throws IOException
     * @throws SocketException
     **************************************************************************/
    public UdpConnection( UdpInputs inputs ) throws IOException, SocketException
    {
        DatagramSocket socket;

        if( inputs.multicast.isUsed && inputs.multicast.data == null )
        {
            throw new IOException( "Multicast group not specified" );
        }

        // LogUtils.printDebug( "NIC: " + inputs.nic );
        // LogUtils.printDebug( "Address: " + nicAddr );
        // LogUtils.printDebug( "Local Port: " + inputs.localPort );
        // LogUtils.printDebug( "Remote Port: " + inputs.remotePort );
        // LogUtils.printDebug( "" );

        NicInfo info = NetUtils.lookupInfo( inputs.nic );

        if( info == null )
        {
            throw new IOException( "NIC not found: " + inputs.nic );
        }

        InetAddress nicAddr = info.address;

        if( inputs.multicast.isUsed )
        {
            socket = openMulticast(inputs, info.nic);
        }
        else
        {
            socket = new DatagramSocket( null );
            socket.setReuseAddress( inputs.reuse );
            socket.setBroadcast( inputs.broadcast );
            socket.bind( new InetSocketAddress( nicAddr, inputs.localPort ) );
        }

        socket.setSoTimeout( inputs.timeout );

        this.socket = socket;
        this.rxBuffer = new byte[65536];

        if( inputs.remoteAddress != null )
        {
            this.remoteAddress = inputs.remoteAddress.getInetAddress();
        }
        this.remotePort = inputs.remotePort;
    }
    
    @SuppressWarnings( "resource")
    private static MulticastSocket openMulticast(UdpInputs inputs, 
        NetworkInterface nic) throws IOException
    {
        MulticastSocket mcs = new MulticastSocket( inputs.localPort );

        // mcs.setLoopbackMode( !inputs.loopback );
        mcs.setOption( StandardSocketOptions.IP_MULTICAST_LOOP,
            inputs.loopback );
        mcs.setTimeToLive( inputs.ttl );
        mcs.setSoTimeout( inputs.timeout );

        try
        {
            InetAddress address = inputs.multicast.data.getInetAddress();
            SocketAddress maddr = new InetSocketAddress( address,
                inputs.localPort );
            mcs.joinGroup( maddr, nic );
        }
        catch( SocketException ex )
        {
            String msg = String.format( "Unable to join %s:%d",
                inputs.multicast.data, inputs.localPort );
            throw new IOException( msg, ex );
        }
        
        return mcs;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        socket.close();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NetMessage receiveMessage()
        throws IOException, SocketTimeoutException
    {
        DatagramPacket packet = new DatagramPacket( rxBuffer, rxBuffer.length );
        socket.receive( packet );
        byte [] contents = Arrays.copyOf( rxBuffer, packet.getLength() );
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        return new NetMessage( true, socket.getLocalAddress().getHostAddress(),
            socket.getLocalPort(), address.getHostAddress(), port, contents );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NetMessage sendMessage( byte [] contents ) throws IOException
    {
        if( remoteAddress == null )
        {
            throw new RuntimeException( "No remote address specified" );
        }

        return sendMessage( contents, remoteAddress, remotePort );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addDisconnectedListener( Runnable listener )
    {
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getNic()
    {
        return socket.getLocalAddress().getHostAddress();
    }

    /***************************************************************************
     * @param contents
     * @param toAddr
     * @param toPort
     * @return
     * @throws IOException
     **************************************************************************/
    public NetMessage sendMessage( byte [] contents, InetAddress toAddr,
        int toPort ) throws IOException
    {
        DatagramPacket packet = new DatagramPacket( contents, contents.length,
            toAddr, toPort );

        socket.send( packet );

        return new NetMessage( false, socket.getLocalAddress().getHostAddress(),
            socket.getLocalPort(), toAddr.getHostAddress(), toPort, contents );
    }

    /***************************************************************************
     * @param address
     * @throws IllegalArgumentException
     **************************************************************************/
    public void setRemote( InetAddress address ) throws IllegalArgumentException
    {
        if( address == null )
        {
            throw new IllegalArgumentException(
                "Remote address may not be null" );
        }

        this.remoteAddress = address;
    }

    /**
     * @param port
     * @throws IllegalArgumentException
     */
    public void setRemote( int port ) throws IllegalArgumentException
    {
        if( port < 1 || port > 65535 )
        {
            throw new IllegalArgumentException(
                "Remote address may not be null" );
        }

        this.remotePort = port;
    }

    /**
     * @param milliseconds
     * @return
     */
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
}
