package org.jutils.apps.jhex;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.core.ui.IToolView;
import org.jutils.core.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JHexTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        return JHexIcons.loader.getIcon( JHexIcons.APP_024 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "JHex";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        IFrameApp frameApp = new org.jutils.apps.jhex.JHexApp();

        return frameApp.createFrame();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "View binary files";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        return JHexIcons.getAppImages();
    }
}
