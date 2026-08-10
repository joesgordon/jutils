package jutils.multicon.links;

import jutils.multicon.data.LinkType;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NtpLink implements ILink
{
    @Override
    public String getDescription()
    {
        // TODO Auto-generated method stub
        return "NTP";
    }

    @Override
    public LinkType getType()
    {
        return LinkType.NTP;
    }

    @Override
    public int getRxMsgCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getRxByteCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getTxMsgCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getTxByteCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isConnected()
    {
        // TODO Auto-generated method stub
        return false;
    }
}
