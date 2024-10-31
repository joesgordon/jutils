package jutils.insomnia;

import java.awt.Container;
import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import jutils.core.ui.IToolView;
import jutils.insomnia.ui.InsomniaFrameView;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class InsomniaTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Container getView()
    {
        InsomniaFrameView view = new InsomniaFrameView();

        // view.start();

        return view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "Measures when the user is awake";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Insomnia";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        return InsomniaIcons.getAppImages();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        return InsomniaIcons.getIcon( InsomniaIcons.APP_024 );
    }
}
