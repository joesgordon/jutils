package org.jutils.net;

import java.io.IOException;
import java.net.InetAddress;

/*******************************************************************************
 *
 ******************************************************************************/
public class MulticastConnectionListener extends ConnectionListener
{
    /**  */
    private MulticastConnection connection;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void start( IConnection connection )
    {
        this.connection = ( MulticastConnection )connection;
    }

    /***************************************************************************
     * @param buf
     * @param remoteAddress
     * @param remotePort
     * @return
     * @throws IOException
     **************************************************************************/
    public NetMessage sendMessage( byte [] buf, InetAddress remoteAddress,
        int remotePort ) throws IOException
    {
        return connection.sendMessage( buf, remoteAddress, remotePort );
    }
}
