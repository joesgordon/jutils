package jutils.multicon.ui.links;

import java.io.IOException;

import javax.swing.JComponent;

import jutils.multicon.links.ILink;
import jutils.multicon.links.NtpLink;
import jutils.multicon.ui.ILinkView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NtpLinkView implements ILinkView
{
    private final NtpLink link;

    public NtpLinkView()
    {
        this.link = new NtpLink();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public ILink getLink()
    {
        return link;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void bind() throws IOException
    {
        // TODO Auto-generated method stub

    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void unbind() throws IOException
    {
        // TODO Auto-generated method stub

    }
}
