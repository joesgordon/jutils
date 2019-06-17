package org.jutils.net;

import java.io.*;
import java.net.*;
import java.util.*;

import org.jutils.io.IOUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpConnection implements IConnection
{
    /**  */
    private final TcpInputs inputs;
    /**  */
    private final Socket socket;
    /**  */
    private final byte [] rxBuffer;
    /**  */
    private final List<Runnable> disconnetListeners;

    /**  */
    private final InetAddress remoteAddress;
    /**  */
    private final int remotePort;
    /**  */
    private final BufferedInputStream input;
    /**  */
    private final OutputStream output;

    /***************************************************************************
     * @param inputs
     * @param disconnetCallback
     * @throws IOException
     **************************************************************************/
    public TcpConnection( TcpInputs inputs ) throws IOException
    {
        this( inputs, createSocket( inputs ) );
    }

    /***************************************************************************
     * @param socket
     * @param disconnetCallback
     * @throws IOException
     **************************************************************************/
    TcpConnection( Socket socket ) throws IOException
    {
        this( createInputs( socket ), socket );
    }

    /***************************************************************************
     * @param socket
     * @param timeout
     * @throws IOException
     **************************************************************************/
    private TcpConnection( TcpInputs inputs, Socket socket ) throws IOException
    {
        this.inputs = inputs;
        this.socket = socket;

        this.rxBuffer = new byte[65535];
        this.disconnetListeners = new ArrayList<>();

        this.remoteAddress = socket.getInetAddress();
        this.remotePort = socket.getPort();
        this.input = new BufferedInputStream( socket.getInputStream(),
            IOUtils.DEFAULT_BUF_SIZE );
        this.output = socket.getOutputStream();

        socket.setSoTimeout( inputs.timeout );
    }

    /***************************************************************************
     * @param socket2
     * @return
     **************************************************************************/
    private static TcpInputs createInputs( Socket socket )
    {
        TcpInputs inputs = new TcpInputs();

        inputs.localPort = socket.getLocalPort();
        inputs.nic = socket.getLocalAddress().getHostAddress();
        inputs.remoteAddress.set(
            ( ( InetSocketAddress )socket.getRemoteSocketAddress() ).getAddress() );
        inputs.remotePort = socket.getPort();
        inputs.timeout = 1000;

        return inputs;
    }

    /***************************************************************************
     * @param inputs
     * @return
     * @throws UnknownHostException
     * @throws IOException
     **************************************************************************/
    private static Socket createSocket( TcpInputs inputs )
        throws UnknownHostException, IOException
    {
        Socket socket = null;

        InetAddress nicAddr = NetUtils.lookupNic( inputs.nic );

        if( nicAddr == null )
        {
            throw new IOException( "Nic not found: " + inputs.nic );
        }

        // socket = new Socket( inputs.remoteAddress.getInetAddress(),
        // inputs.remotePort, nicAddr, inputs.localPort );

        socket = new Socket();

        InetSocketAddress local = new InetSocketAddress( nicAddr,
            inputs.localPort );
        InetSocketAddress remote = new InetSocketAddress(
            inputs.remoteAddress.getInetAddress(), inputs.remotePort );

        socket.bind( local );
        socket.setSoTimeout( inputs.timeout );
        socket.connect( remote, inputs.timeout );

        return socket;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void fireDisconnected()
    {
        for( Runnable listener : disconnetListeners )
        {
            listener.run();
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        if( socket != null )
        {
            socket.shutdownInput();
            socket.shutdownOutput();

            input.close();
            output.flush();
            output.close();
            socket.close();
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NetMessage sendMessage( byte [] contents ) throws IOException
    {
        try
        {
            socket.getOutputStream().write( contents );
        }
        catch( SocketTimeoutException ex )
        {
            fireDisconnected();
            return null;
        }

        return new NetMessage( false, socket.getLocalAddress().getHostAddress(),
            socket.getLocalPort(), remoteAddress.getHostAddress(), remotePort,
            contents );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NetMessage receiveMessage() throws IOException
    {
        int len = input.read( rxBuffer );

        if( len == -1 )
        {
            // connection closed?
            fireDisconnected();
            return null;
            // throw new SocketTimeoutException();
        }
        else if( len == 0 )
        {
            throw new SocketTimeoutException();
        }

        byte [] contents = Arrays.copyOf( rxBuffer, len );

        return new NetMessage( true, socket.getLocalAddress().getHostAddress(),
            socket.getLocalPort(), remoteAddress.getHostAddress(), remotePort,
            contents );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addDisconnectedListener( Runnable listener )
    {
        disconnetListeners.add( listener );
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
     * @return
     **************************************************************************/
    public Ip4Address getRemoteAddress()
    {
        return new Ip4Address( remoteAddress );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getRemotePort()
    {
        return remotePort;
    }

    /***************************************************************************
     * @param millis
     * @throws SocketException
     **************************************************************************/
    public void setTimeout( int millis ) throws SocketException
    {
        socket.setSoTimeout( millis );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public TcpInputs getInputs()
    {
        return new TcpInputs( inputs );
    }
}
