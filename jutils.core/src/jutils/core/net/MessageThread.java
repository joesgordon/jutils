package jutils.core.net;

import java.io.IOException;
import java.net.SocketTimeoutException;

import jutils.core.ValidationException;
import jutils.core.io.ByteArrayStream;
import jutils.core.io.DataStream;
import jutils.core.io.IDataSerializer;
import jutils.core.ui.event.updater.UpdaterList;

/*******************************************************************************
 * @param <T> The base type of messages to be sent/received.
 ******************************************************************************/
public class MessageThread<T>
{
    /**  */
    private final IDataSerializer<T> msgSerializer;
    /**  */
    private final ConnectionListener listener;

    /**  */
    public final UpdaterList<NetMessage> messageHandlers;
    /**  */
    public final UpdaterList<String> errorListeners;
    /**  */
    public final UpdaterList<SocketTimeoutException> timeoutListeners;

    /***************************************************************************
     * @param msgSerializer
     **************************************************************************/
    public MessageThread( IDataSerializer<T> msgSerializer )
    {
        this.msgSerializer = msgSerializer;
        this.listener = new ConnectionListener();
        this.messageHandlers = new UpdaterList<>();
        this.errorListeners = new UpdaterList<>();
        this.timeoutListeners = listener.timeoutListeners;

        listener.addErrorListener( ( e ) -> handleError( e ) );
        listener.addMessageListener( ( m ) -> handleMessage( m ) );
    }

    /***************************************************************************
     * @param msg
     * @return
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    public NetMessage sendMessage( T msg ) throws IOException
    {
        NetMessage netMsg = listener.sendMessage( write( msg ) );

        netMsg.message = msg;

        messageHandlers.fire( netMsg );

        return netMsg;
    }

    /***************************************************************************
     * @param connection
     **************************************************************************/
    public void start( IConnection connection )
    {
        listener.start( connection );
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void stop() throws IOException
    {
        listener.close();
    }

    /***************************************************************************
     * @param error
     **************************************************************************/
    private void handleError( String error )
    {
        errorListeners.fire( error );
    }

    /***************************************************************************
     * @param netMsg
     **************************************************************************/
    private void handleMessage( NetMessage netMsg )
    {
        try
        {
            netMsg.message = read( netMsg.contents );
        }
        catch( IOException ex )
        {
            handleError( ex.getMessage() );
        }
        finally
        {
            messageHandlers.fire( netMsg );
        }
    }

    /***************************************************************************
     * @param contents
     * @return
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    private T read( byte [] contents ) throws IOException
    {
        T msg = null;

        try( ByteArrayStream bas = new ByteArrayStream( contents );
             DataStream stream = new DataStream( bas ) )
        {
            try
            {
                msg = msgSerializer.read( stream );
            }
            catch( ValidationException ex )
            {
                // TODO add parsing error listeners.
            }
        }

        return msg;
    }

    /***************************************************************************
     * @param msg
     * @return
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    private byte [] write( T msg ) throws IOException
    {
        try( ByteArrayStream bas = new ByteArrayStream( 1024 );
             DataStream stream = new DataStream( bas ) )
        {
            msgSerializer.write( msg, stream );

            return bas.toByteArray();
        }
    }
}
