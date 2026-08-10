package jutils.multicon.ui.links;

import java.io.IOException;

import javax.swing.JComponent;

import jutils.multicon.links.BridgeLink;
import jutils.multicon.links.ILink;
import jutils.multicon.ui.ILinkView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BridgeLinkView implements ILinkView
{
    private final BridgeLink link;

    public BridgeLinkView()
    {
        this.link = new BridgeLink();
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
