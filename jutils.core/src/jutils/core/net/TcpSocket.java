package jutils.core.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import jutils.core.utils.RunnableList;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpSocket implements Closeable
{
    /**  */
    private final TcpConfig config;
    /**  */
    private final RunnableList disconnectedListeners;

    /**  */
    private SocketChannel channel;
    /**  */
    private Socket socket;
    /**  */
    private InputStream input;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpSocket()
    {
        this.config = new TcpConfig();
        this.disconnectedListeners = new RunnableList();

        this.channel = null;
        this.socket = null;
        this.input = null;
    }

    /***************************************************************************
     * @param channel
     * @return
     * @throws IOException
     **************************************************************************/
    public static TcpSocket createFrom( SocketChannel channel )
        throws IOException
    {
        TcpSocket tcp = new TcpSocket();

        tcp.set( null, channel );

        return tcp;
    }

    /***************************************************************************
     * @param config
     * @param channel
     * @throws IOException
     **************************************************************************/
    private void set( TcpConfig config, SocketChannel channel )
        throws IOException
    {
        @SuppressWarnings( "resource")
        Socket socket = channel.socket();

        if( config == null )
        {
            config = generateConfig( channel, socket );
        }

        this.config.set( config );
        this.channel = channel;
        this.socket = channel.socket();
        this.input = socket.getInputStream();
    }

    /***************************************************************************
     * @param channel
     * @param socket
     * @return
     * @throws IOException
     **************************************************************************/
    private static TcpConfig generateConfig( SocketChannel channel,
        Socket socket ) throws IOException
    {
        TcpConfig config = new TcpConfig();

        config.local.address.setInetAddress( socket.getLocalAddress() );
        config.local.port = socket.getLocalPort();
        config.remote.address.setInetAddress( socket.getLocalAddress() );
        config.remote.port = socket.getLocalPort();
        config.timeout = socket.getSoTimeout();
        config.keepAlive = channel.getOption(
            StandardSocketOptions.SO_KEEPALIVE );
        config.reuseAddress = channel.getOption(
            StandardSocketOptions.SO_REUSEADDR );
        config.linger = channel.getOption( StandardSocketOptions.SO_LINGER );
        config.noDelay = channel.getOption( StandardSocketOptions.TCP_NODELAY );

        return config;
    }

    /***************************************************************************
     * @param config
     * @throws IOException
     **************************************************************************/
    public void open( TcpConfig config ) throws IOException
    {
        @SuppressWarnings( "resource")
        SocketChannel channel = SocketChannel.open();

        InetSocketAddress local = config.local.getInetSocketAddress();
        InetSocketAddress remote = config.remote.getInetSocketAddress();

        try
        {
            channel.configureBlocking( true );

            @SuppressWarnings( { "resource", "unused" })
            SocketChannel bindChannel = channel.bind( local );

            bindChannel.connect( remote );
        }
        catch( IOException ex )
        {
            try
            {
                channel.close();
            }
            catch( IOException cex )
            {
            }

            throw new IOException( "Unable to open TCP Client Socket", ex );
        }

        set( config, channel );

        setSoTimeout( config.timeout );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        SocketChannel channel = this.channel;

        if( channel != null )
        {
            @SuppressWarnings( { "unused", "resource" })
            SocketChannel inputChannel = channel.shutdownInput();
            @SuppressWarnings( { "unused", "resource" })
            SocketChannel outputChannel = channel.shutdownOutput();

            socket.shutdownInput();

            input.close();
            channel.close();
            this.channel = null;
            this.socket = null;
        }
    }

    /***************************************************************************
     * @param listener
     **************************************************************************/
    public void addDisconnectedListener( Runnable listener )
    {
        disconnectedListeners.add( listener );
    }

    /***************************************************************************
     * @param contents
     * @return
     * @throws IOException
     **************************************************************************/
    public int send( byte [] contents ) throws IOException
    {
        ByteBuffer buffer = ByteBuffer.wrap( contents );

        try
        {
            return channel.write( buffer );
        }
        catch( ClosedChannelException ex )
        {
            disconnectedListeners.fire();
            return -1;
        }
    }

    /***************************************************************************
     * @param contents
     * @return
     * @throws IOException
     **************************************************************************/
    public int receive( byte [] contents ) throws IOException
    {
        return input.read( contents );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public TcpConfig getConfig()
    {
        return new TcpConfig( config );
    }

    /***************************************************************************
     * @param timeout
     * @return
     * @throws IllegalArgumentException if {@code timeout < 0}
     * @throws SocketException
     **************************************************************************/
    public void setSoTimeout( int timeout )
        throws IllegalArgumentException, SocketException
    {
        socket.setSoTimeout( timeout );
    }

    /***************************************************************************
     * @return
     * @throws SocketException
     **************************************************************************/
    public int getSoTimeout() throws SocketException
    {
        return socket.getSoTimeout();
    }

    /***************************************************************************
     * @param size
     * @throws IOException
     **************************************************************************/
    public void setSendBufferSize( int size ) throws IOException
    {
        @SuppressWarnings( { "resource", "unused" })
        SocketChannel c = channel.setOption( StandardSocketOptions.SO_SNDBUF,
            Integer.valueOf( size ) );
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public int getSendBufferSize() throws IOException
    {
        return channel.getOption( StandardSocketOptions.SO_SNDBUF );
    }

    /***************************************************************************
     * @param size
     * @throws IOException
     **************************************************************************/
    public void setReceiveBufferSize( int size ) throws IOException
    {
        @SuppressWarnings( { "resource", "unused" })
        SocketChannel c = channel.setOption( StandardSocketOptions.SO_RCVBUF,
            Integer.valueOf( size ) );
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public int getReceiveBufferSize() throws IOException
    {
        return channel.getOption( StandardSocketOptions.SO_RCVBUF );
    }

    /***************************************************************************
     * @param keepAlive
     * @throws IOException
     **************************************************************************/
    public void setKeepAlive( boolean keepAlive ) throws IOException
    {
        @SuppressWarnings( { "resource", "unused" })
        SocketChannel c = channel.setOption( StandardSocketOptions.SO_KEEPALIVE,
            keepAlive );
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public boolean getKeepAlive() throws IOException
    {
        return channel.getOption( StandardSocketOptions.SO_KEEPALIVE );
    }

    /***************************************************************************
     * @param reuseAddress
     * @throws IOException
     **************************************************************************/
    public void setReuseAddress( boolean reuseAddress ) throws IOException
    {
        @SuppressWarnings( { "resource", "unused" })
        SocketChannel c = channel.setOption( StandardSocketOptions.SO_REUSEADDR,
            reuseAddress );
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public boolean getReuseAddress() throws IOException
    {
        return channel.getOption( StandardSocketOptions.SO_REUSEADDR );
    }

    /***************************************************************************
     * @param linger
     * @throws IOException
     **************************************************************************/
    public void setLinger( int linger ) throws IOException
    {
        @SuppressWarnings( { "resource", "unused" })
        SocketChannel c = channel.setOption( StandardSocketOptions.SO_LINGER,
            Integer.valueOf( linger ) );
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public int getLinger() throws IOException
    {
        return channel.getOption( StandardSocketOptions.SO_LINGER );
    }

    /***************************************************************************
     * @param noDelay
     * @throws IOException
     **************************************************************************/
    public void setNoDelay( boolean noDelay ) throws IOException
    {
        @SuppressWarnings( { "resource", "unused" })
        SocketChannel c = channel.setOption( StandardSocketOptions.TCP_NODELAY,
            noDelay );
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public boolean getNoDelay() throws IOException
    {
        return channel.getOption( StandardSocketOptions.TCP_NODELAY );
    }
}
