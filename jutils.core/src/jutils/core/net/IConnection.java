package jutils.core.net;

import java.io.Closeable;
import java.io.IOException;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IConnection extends Closeable
{
    /***************************************************************************
     * Sends the message provided using this connection.
     * @param buf the bytes to send
     * @return the message sent or {@code null} on error.
     * @throws IOException any exception generated.
     **************************************************************************/
    public NetMessage sendMessage( byte [] buf ) throws IOException;

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public NetMessage receiveMessage() throws IOException;

    /***************************************************************************
     * @param listener
     **************************************************************************/
    public void addDisconnectedListener( Runnable listener );

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getNic();

    /***************************************************************************
     * @return
     **************************************************************************/
    public EndPoint getRemote();
}
