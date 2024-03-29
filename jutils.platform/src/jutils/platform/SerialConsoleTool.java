package jutils.platform;

import java.awt.Container;
import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import jutils.core.ui.IToolView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialConsoleTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Container getView()
    {
        return SerialConsoleMain.createFrame();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "Send and receive messages over serial ports";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Serial Console";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        return PlatformIcons.getAppImages();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        return PlatformIcons.getIcon( PlatformIcons.APP_024 );
    }
}
