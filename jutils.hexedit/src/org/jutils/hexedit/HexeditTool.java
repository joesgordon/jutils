package org.jutils.hexedit;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.core.ui.IToolView;
import org.jutils.core.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexeditTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        return HexeditIcons.loader.getIcon( HexeditIcons.APP_024 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Hexedit";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        IFrameApp frameApp = new org.jutils.hexedit.HexeditApp();

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
        return HexeditIcons.getAppImages();
    }
}
