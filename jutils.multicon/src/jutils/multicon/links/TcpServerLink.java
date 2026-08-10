package jutils.multicon.links;

import jutils.multicon.data.LinkType;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpServerLink implements ILink
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        // TODO Auto-generated method stub
        return "TCP Server";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LinkType getType()
    {
        return LinkType.TCP_SERVER;
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
