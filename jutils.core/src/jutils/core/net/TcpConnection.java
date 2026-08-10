package jutils.core.net;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpConnection implements IConnection
{
    /**  */
    private final TcpConfig inputs;
    /**  */
    private final byte [] rxBuffer;
    /**  */
    private final List<Runnable> disconnetListeners;
    /**  */
    private final TcpSocket socket;

    /***************************************************************************
     * @param inputs
     **************************************************************************/
    public TcpConnection()
    {
        this( null );
    }

    /***************************************************************************
     * @param socket
     * @param disconnetCallback
     * @throws IOException
     **************************************************************************/
    TcpConnection( TcpSocket socket )
    {
        this.inputs = new TcpConfig();
        this.rxBuffer = new byte[65535];
        this.disconnetListeners = new ArrayList<>();
        this.socket = socket;

        if( socket != null )
        {
            inputs.set( socket.getConfig() );
        }
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
     * @param config
     * @throws IOException
     **************************************************************************/
    public void open( TcpConfig config ) throws IOException
    {
        socket.open( config );
        this.inputs.set( socket.getConfig() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        if( socket != null )
        {
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
            int len = socket.send( contents );
            byte [] bytes = Arrays.copyOf( contents, len );
            NetMessage msg = new NetMessage( false, inputs.local, inputs.remote,
                bytes );

            return msg;
        }
        catch( SocketTimeoutException ex )
        {
            fireDisconnected();
        }

        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NetMessage receiveMessage() throws IOException
    {
        int len = socket.receive( rxBuffer );

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
    public EndPoint getLocal()
    {
        return new EndPoint( inputs.local );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public EndPoint getRemote()
    {
        return new EndPoint( inputs.remote );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IpAddress getRemoteAddress()
    {
        return new IpAddress( inputs.remote.address );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getRemotePort()
    {
        return inputs.remote.port;
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
    public TcpConfig getInputs()
    {
        return new TcpConfig( inputs );
    }

    /***************************************************************************
     * @param on
     * @throws SocketException
     **************************************************************************/
    public void setTcpNoDelay( boolean on ) throws IOException
    {
        socket.setNoDelay( on );
    }
}
