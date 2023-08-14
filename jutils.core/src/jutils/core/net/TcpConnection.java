package jutils.core.net;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jutils.core.io.IOUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpConnection implements IConnection, Closeable
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
        this( inputs, null );
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
     * @param inputs
     * @param socket
     * @throws IOException
     **************************************************************************/
    private TcpConnection( TcpInputs inputs, Socket socket ) throws IOException
    {
        if( inputs == null && socket == null )
        {
            throw new IllegalArgumentException(
                "Cannot create a TcpConnection with no socket or configuration" );
        }

        if( socket == null )
        {
            socket = createSocket( inputs );
        }

        this.inputs = inputs;
        this.socket = socket;

        InputStream inStream = socket.getInputStream();

        this.rxBuffer = new byte[65535];
        this.disconnetListeners = new ArrayList<>();

        this.remoteAddress = socket.getInetAddress();
        this.remotePort = socket.getPort();
        this.input = new BufferedInputStream( inStream,
            IOUtils.DEFAULT_BUF_SIZE );
        this.output = socket.getOutputStream();

        socket.setSoTimeout( inputs.timeout );
    }

    /***************************************************************************
     * @param socket
     * @return
     **************************************************************************/
    private static TcpInputs createInputs( Socket socket )
    {
        TcpInputs inputs = new TcpInputs();

        inputs.localPort = socket.getLocalPort();
        inputs.nic = socket.getLocalAddress().getHostAddress();
        inputs.remoteAddress.setInetAddress(
            ( ( InetSocketAddress )socket.getRemoteSocketAddress() ).getAddress() );
        inputs.remotePort = socket.getPort();
        try
        {
            inputs.timeout = socket.getSoTimeout();
        }
        catch( SocketException e )
        {
            inputs.timeout = -1;
        }

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

        InetAddress nicAddr = NetUtils.lookupNetAddress( inputs.nic );

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
            @SuppressWarnings( "resource")
            OutputStream outStream = socket.getOutputStream();
            outStream.write( contents );
        }
        catch( SocketTimeoutException ex )
        {
            fireDisconnected();
            return null;
        }

        NetMessage msg = new NetMessage( false, getLocal(), getRemote(),
            contents );

        msg.local.address.setInetAddress( socket.getLocalAddress() );
        msg.local.port = socket.getLocalPort();

        msg.remote.address.setInetAddress( remoteAddress );
        msg.remote.port = remotePort;

        return msg;
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

        NetMessage msg = new NetMessage( true, getLocal(), getRemote(),
            contents );

        return msg;
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
        EndPoint ep = new EndPoint();

        ep.address.setInetAddress( socket.getLocalAddress() );
        ep.port = socket.getLocalPort();

        return ep;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IpAddress getRemoteAddress()
    {
        IpAddress addr = new IpAddress();

        addr.setInetAddress( remoteAddress );

        return addr;
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

    /***************************************************************************
     * @param on
     * @throws SocketException
     **************************************************************************/
    public void setTcpNoDelay( boolean on ) throws SocketException
    {
        socket.setTcpNoDelay( on );
    }
}
