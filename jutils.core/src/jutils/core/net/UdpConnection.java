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
     * @param config
     * @param remote
     * @throws IOException
     * @throws SocketException
     **************************************************************************/
    public UdpConnection( UdpConfig config, EndPoint remote )
        throws IOException, SocketException
    {
        this.socket = NetUtils.openUdpSocket( config );

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
}
