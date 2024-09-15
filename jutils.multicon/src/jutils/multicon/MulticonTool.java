package jutils.multicon;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;

import jutils.core.ui.IToolView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticonTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        return MulticonIcons.loader.getIcon( MulticonIcons.MULTICON_024 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Multicon";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        MulticonApp app = new MulticonApp();
        JFrame frame = app.createFrame();

        return frame;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "Multiple net connection manager";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        return MulticonIcons.getMulticonImages();
    }
}
