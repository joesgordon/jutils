package jutils.multicon.links;

import jutils.multicon.data.LinkType;
import jutils.platform.data.SerialConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialLink implements ILink
{
    public final SerialConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public SerialLink()
    {
        this.config = new SerialConfig();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        // TODO Auto-generated method stub
        return String.format( "%s:", config.comPort );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LinkType getType()
    {
        return LinkType.SERIAL;
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
        // TODO Auto-generated method stub
        return false;
    }
}
