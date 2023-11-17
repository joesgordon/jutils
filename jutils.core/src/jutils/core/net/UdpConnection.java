package jutils.core.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import jutils.core.net.NetUtils.NicInfo;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpConnection implements IConnection
{
    /**  */
    private final UdpSocket socket;

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
        this.socket = new UdpSocket();

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

        if( inputs.remoteAddress != null )
        {
            this.remoteAddress = inputs.remoteAddress.getInetAddress();
        }
        this.remotePort = inputs.remotePort;
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
    public NetMessage receiveMessage() throws IOException
    {
        return socket.receive();
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
        return socket.getLocal().address.toString();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public EndPoint getRemote()
    {
        EndPoint ep = new EndPoint();

        ep.address.setInetAddress( remoteAddress );
        ep.port = remotePort;

        return ep;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public EndPoint getLocal()
    {
        return socket.getLocal();
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
        return socket.send( contents, toAddr, toPort );
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

    /***************************************************************************
     * @param port
     * @throws IllegalArgumentException
     **************************************************************************/
    public void setRemote( int port ) throws IllegalArgumentException
    {
        if( port < 1 || port > 65535 )
        {
            throw new IllegalArgumentException(
                "Remote address may not be null" );
        }

        this.remotePort = port;
    }

    /***************************************************************************
     * @param milliseconds
     * @return
     **************************************************************************/
    public boolean setReceiveTimeout( int milliseconds )
    {
        return socket.setReceiveTimeout( milliseconds );
    }
}
