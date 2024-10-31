package jutils.core.pcap.ui;

import java.awt.Container;
import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import jutils.core.ui.IToolView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcapTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Container getView()
    {
        return PcapMain.createFrame();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "View pcapng files";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "PCAP Viewer";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
