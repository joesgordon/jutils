package jutils.core.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

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
     * 
     **************************************************************************/
    public UdpConnection()
    {
        this.socket = new UdpSocket();
        this.remoteAddress = null;
        this.remotePort = -1;
    }

    /***************************************************************************
     * @param config
     * @param remote
     * @throws IOException
     * @throws SocketException
     **************************************************************************/
    public void open( UdpConfig config, EndPoint remote )
        throws IOException, SocketException
    {
        NetUtils.openUdpSocket( this.socket, config );

        setRemote( remote );
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
    public EndPoint getLocal()
    {
        return socket.getLocal();
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
        if( port < 0 || port > 65535 )
        {
            throw new IllegalArgumentException(
                "Port is out of range [1-65535]: " + port );
        }

        this.remotePort = port;
    }

    /***************************************************************************
     * @param remote
     **************************************************************************/
    public void setRemote( EndPoint remote )
    {
        setRemote( remote.address.getInetAddress() );
        setRemote( remote.port );
    }

    /***************************************************************************
     * @param milliseconds
     * @return
     **************************************************************************/
    public boolean setReceiveTimeout( int milliseconds )
    {
        return socket.setReceiveTimeout( milliseconds );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isBound()
    {
        return socket.isBound();
    }
}
