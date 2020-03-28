package org.duak;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.core.ui.IToolView;
import org.jutils.core.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DuakTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        return DuakIcons.loader.getIcon( DuakIcons.APP_024 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Duak";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        IFrameApp r = new org.duak.DuakApp();

        return r.createFrame();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "Finds the largest sets of data in a directory.";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        return DuakIcons.getAppImages();
    }
}
