package org.jutils.core.net;

import java.io.Closeable;
import java.io.IOException;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IConnection extends Closeable
{
    /***************************************************************************
     * @param buf
     * @return
     * @throws IOException
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
}