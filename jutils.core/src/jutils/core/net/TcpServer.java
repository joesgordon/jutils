package jutils.core.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpServer implements Closeable
{
    /**  */
    private final ServerSocket server;

    /***************************************************************************
     * @param inputs
     * @throws IOException
     **************************************************************************/
    public TcpServer( TcpInputs inputs ) throws IOException
    {
        InetAddress nicAddr = NetUtils.lookupNetAddress( inputs.nic );

        if( nicAddr == null )
        {
            throw new IOException( "Nic not found: " + inputs.nic );
        }

        this.server = new ServerSocket( inputs.localPort, 64, nicAddr );

        server.setSoTimeout( inputs.timeout );
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
        Socket socket = server.accept();

        TcpConnection connection = new TcpConnection( socket );

        return connection;
    }
}
