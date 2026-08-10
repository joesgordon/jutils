package jutils.multicon.links;

import jutils.core.net.UdpConnection;
import jutils.multicon.data.LinkType;
import jutils.multicon.data.UdpInputs;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpLink implements ILink
{
    /**  */
    public final UdpInputs inputs;
    /**  */
    public final UdpConnection connection;

    /***************************************************************************
     * 
     **************************************************************************/
    @SuppressWarnings( "resource")
    public UdpLink()
    {
        this( new UdpConnection() );
    }

    /***************************************************************************
     * @param connection
     **************************************************************************/
    public UdpLink( UdpConnection connection )
    {
        this.inputs = new UdpInputs();
        this.connection = connection;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        String txt = String.format( "%s:%d to %s", inputs.config.nic,
            inputs.config.localPort, inputs.remote );
        return txt;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LinkType getType()
    {
        return LinkType.UDP;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getRxMsgCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getRxByteCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getTxMsgCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getTxByteCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isConnected()
    {
        return connection.isBound();
    }
}
