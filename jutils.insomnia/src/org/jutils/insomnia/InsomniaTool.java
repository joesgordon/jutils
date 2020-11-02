package org.jutils.insomnia;

import java.awt.Container;
import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import org.jutils.core.ui.IToolView;
import org.jutils.insomnia.ui.InsomniaFrameView;

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
        return "Keeps the system awake";
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
        // TODO make icons
        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        return null;
    }
}
