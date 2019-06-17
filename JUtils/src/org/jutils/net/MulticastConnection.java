package org.jutils.net;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastConnection implements IConnection
{
    /**  */
    private final InetAddress address;
    /**  */
    private final MulticastSocket socket;
    /**  */
    private final int port;
    /**  */
    private final byte [] rxBuffer;
    /**  */
    private final DatagramPacket rxPacket;
    /**  */
    private final String localAddress;

    /***************************************************************************
     * @param inputs the configuration values for this connection.
     * @throws IOException if any error occurs binding to the socket using the
     * provided configuration values.
     **************************************************************************/
    public MulticastConnection( MulticastInputs inputs ) throws IOException
    {
        if( inputs.port < 1 || inputs.port > 65535 )
        {
            throw new IOException( "Invalid port: " + inputs.port );
        }

        if( inputs.ttl < 0 || inputs.ttl > 255 )
        {
            throw new IOException( "Invalid Time to Live: " + inputs.ttl );
        }

        this.address = inputs.group.getInetAddress();
        this.rxBuffer = new byte[65536];
        this.socket = new MulticastSocket( inputs.port );
        this.rxPacket = new DatagramPacket( rxBuffer, rxBuffer.length, address,
            inputs.port );
        this.port = inputs.port;

        this.socket.setLoopbackMode( !inputs.loopback );
        this.socket.setTimeToLive( inputs.ttl );
        this.socket.setSoTimeout( inputs.timeout );

        InetAddress any = InetAddress.getByAddress( new byte[] { 0, 0, 0, 0 } );
        InetAddress nicAddr = null;

        if( inputs.nic != null )
        {
            nicAddr = NetUtils.lookupNic( inputs.nic );

            if( nicAddr == null )
            {
                throw new IOException( "Nic not found: " + inputs.nic );
            }

            if( !nicAddr.equals( any ) )
            {
                // this.socket.setNetworkInterface( nic );
                this.socket.setInterface( nicAddr );
            }
        }
        else
        {
            nicAddr = any;
        }

        this.localAddress = nicAddr.getHostAddress();

        try
        {
            this.socket.joinGroup( address );
        }
        catch( SocketException ex )
        {
            String msg = String.format( "Unable to join %s:%d", inputs.group,
                inputs.port );
            throw new IOException( msg, ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NetMessage sendMessage( byte [] buf ) throws IOException
    {
        // LogUtils.printDebug( "Sending message..." );

        DatagramPacket pack = new DatagramPacket( buf, buf.length, address,
            port );

        socket.send( pack );

        int localPort = socket.getLocalPort();

        String remoteAddress = address.getHostAddress();
        int remotePort = port;

        NetMessage msg = new NetMessage( false, localAddress, localPort,
            remoteAddress, remotePort, buf );

        return msg;
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

        return new NetMessage( false, localAddress, socket.getLocalPort(),
            toAddr.getHostAddress(), toPort, contents );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NetMessage receiveMessage() throws IOException
    {
        // LogUtils.printDebug( "Receiving message..." );

        socket.receive( rxPacket );

        byte [] contents = Arrays.copyOf( rxBuffer, rxPacket.getLength() );
        InetAddress address = rxPacket.getAddress();
        int port = rxPacket.getPort();

        int localPort = socket.getLocalPort();

        String remoteAddress = address.getHostAddress();
        int remotePort = port;

        NetMessage msg = new NetMessage( true, localAddress, localPort,
            remoteAddress, remotePort, contents );

        return msg;
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
    public void addDisconnectedListener( Runnable listener )
    {
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getNic()
    {
        try
        {
            return NetUtils.getIpv4( socket.getNetworkInterface() );
        }
        catch( SocketException e )
        {
            return null;
        }
    }
}
