package jutils.multicon.ui.links;

import java.io.IOException;

import javax.swing.JComponent;

import jutils.multicon.data.UdpInputs;
import jutils.multicon.links.ILink;
import jutils.multicon.links.UdpLink;
import jutils.multicon.ui.ConnectionBindableView;
import jutils.multicon.ui.ILinkView;
import jutils.multicon.ui.net.UdpView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpLinkView implements ILinkView
{
    /**  */
    private final ConnectionBindableView<UdpInputs> connectionView;
    /**  */
    private final UdpView udp;
    /**  */
    private final UdpLink link;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpLinkView()
    {
        this.link = new UdpLink();
        this.udp = new UdpView( link.connection );
        this.connectionView = new ConnectionBindableView<>( udp );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return connectionView.getView();
    }

    /***************************************************************************
     * {@inhersitDoc}
     **************************************************************************/
    @Override
    public void bind() throws IOException
    {
        connectionView.bind();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void unbind() throws IOException
    {
        connectionView.unbind();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public ILink getLink()
    {
        return link;
    }
}
