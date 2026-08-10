package jutils.multicon.ui.links;

import java.io.IOException;

import javax.swing.JComponent;

import jutils.multicon.links.ILink;
import jutils.multicon.links.TcpServerLink;
import jutils.multicon.ui.ILinkView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpServerLinkView implements ILinkView
{
    private final TcpServerLink link;

    public TcpServerLinkView()
    {
        this.link = new TcpServerLink();
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
