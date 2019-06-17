package org.jutils.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.jutils.concurrent.ITaskHandler;
import org.jutils.concurrent.TaskThread;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.UpdaterList;

/*******************************************************************************
 * Creates a thread to listen for messages received with the provided
 * connection.
 ******************************************************************************/
public class ConnectionListener implements Closeable
{
    /** The connection used to send/receive messages. */
    private IConnection connection;
    /** The receive thread. */
    private TaskThread rxThread;

    /**  */
    public final UpdaterList<NetMessage> messageListeners;
    /**  */
    public final UpdaterList<String> errorListeners;
    /**  */
    public final UpdaterList<SocketTimeoutException> timeoutListeners;

    /***************************************************************************
     * @param connection
     * @param msgListener
     * @param errListener
     * @throws IOException
     **************************************************************************/
    public ConnectionListener()
    {
        this.connection = null;
        this.messageListeners = new UpdaterList<>();
        this.errorListeners = new UpdaterList<>();
        this.timeoutListeners = new UpdaterList<>();
    }

    /***************************************************************************
     * @param connection
     **************************************************************************/
    public void start( IConnection connection )
    {
        this.connection = connection;
        this.rxThread = new TaskThread( ( h ) -> run( h ),
            "Connection Receiver" );

        rxThread.start();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        IConnection connection = this.connection;
        TaskThread rxThread = this.rxThread;

        this.connection = null;
        this.rxThread = null;

        if( connection != null )
        {
            rxThread.interrupt();
            rxThread.stopAndWait();
            connection.close();
        }
    }

    /***************************************************************************
     * Removes all listeners added to this connection.
     **************************************************************************/
    public void clear()
    {
        this.messageListeners.removeAll();
        this.errorListeners.removeAll();
        this.timeoutListeners.removeAll();
    }

    /***************************************************************************
     * @param handler
     **************************************************************************/
    public void run( ITaskHandler handler )
    {
        while( handler.canContinue() )
        {
            try
            {
                // LogUtils.printDebug( "Receiving message..." );
                NetMessage msg = connection.receiveMessage();
                if( msg == null )
                {
                    break;
                }
                messageListeners.fire( msg );
            }
            catch( SocketTimeoutException ex )
            {
                // LogUtils.printDebug( "Receive timed out..." );
                timeoutListeners.fire( ex );
            }
            catch( SocketException ex )
            {
                // LogUtils.printDebug( "Receive had an exception:" +
                // ex.getMessage() );
            }
            catch( IOException ex )
            {
                ex.printStackTrace();
                errorListeners.fire(
                    "Error receiving packet: " + ex.getMessage() );
            }
            catch( Exception ex )
            {
                ex.printStackTrace();
                break;
            }
        }
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addMessageListener( IUpdater<NetMessage> l )
    {
        messageListeners.add( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addErrorListener( IUpdater<String> l )
    {
        errorListeners.add( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addTimeoutListener( IUpdater<SocketTimeoutException> l )
    {
        timeoutListeners.add( l );
    }

    /***************************************************************************
     * @param buf
     * @return
     * @throws IOException
     **************************************************************************/
    public NetMessage sendMessage( byte [] buf ) throws IOException
    {
        if( connection == null )
        {
            throw new IOException( "No connection established" );
        }

        return connection.sendMessage( buf );
    }
}
