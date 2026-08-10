package jutils.multicon.links;

import jutils.multicon.data.LinkType;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialLink implements ILink
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        // TODO Auto-generated method stub
        return "Serial";
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
