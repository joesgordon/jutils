package jutils.core.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpServer implements Closeable
{
    /**  */
    private final TcpServerConfig config;
    /**  */
    private ServerSocketChannel server;
    /**  */
    private Selector selector;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpServer()
    {
        this.config = new TcpServerConfig();
    }

    /***************************************************************************
     * @param config
     * @throws IOException
     **************************************************************************/
    public void open( TcpServerConfig config ) throws IOException
    {
        this.config.set( config );

        this.server = ServerSocketChannel.open();

        InetSocketAddress local = config.local.getInetSocketAddress();

        server.bind( local, config.backlog );

        server.register( selector, SelectionKey.OP_ACCEPT );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        server.close();
    }

    /***************************************************************************
     * @return
     * @throws IOException
     * @throws SocketTimeoutException
     **************************************************************************/
    @SuppressWarnings( "resource")
    public TcpConnection accept() throws IOException, SocketTimeoutException
    {
        TcpConnection connection = null;

        int count = selector.select( config.timeout );

        if( count > 0 )
        {
            // Iterate through selector.selectedKeys().iterator() and use
            // ServerSocketChannel channel = (ServerSocketChannel)
            // key.channel() if this is ever changed to listen to multiple
            // ports.

            SocketChannel socket = server.accept();
            TcpSocket tcp = TcpSocket.createFrom( socket );

            connection = new TcpConnection( tcp );
        }

        return connection;
    }
}
