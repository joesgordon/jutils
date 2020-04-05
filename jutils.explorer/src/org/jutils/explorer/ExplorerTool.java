package org.jutils.explorer;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.core.ui.IToolView;

/*******************************************************************************
 *
 ******************************************************************************/
public class ExplorerTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        return ExplorerIcons.getApp24Icon();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Explorer";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return ExplorerMain.createFrame();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "File explorer";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        return ExplorerIcons.getAppImages();
    }
}
