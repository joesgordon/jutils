package jutils.multicon.ui.links;

import java.io.IOException;

import javax.swing.JComponent;

import jutils.multicon.links.ILink;
import jutils.multicon.links.SerialLink;
import jutils.multicon.ui.ILinkView;
import jutils.platform.ui.SerialConsoleView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialLinkView implements ILinkView
{
    /**  */
    private final SerialLink link;
    /**  */
    private final SerialConsoleView view;

    /***************************************************************************
     * 
     **************************************************************************/
    public SerialLinkView()
    {
        this.link = new SerialLink();
        this.view = new SerialConsoleView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view.getView();
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
