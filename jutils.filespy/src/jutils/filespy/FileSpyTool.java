package jutils.filespy;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;

import jutils.core.IconConstants;
import jutils.core.ui.IToolView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileSpyTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        return IconConstants.getIcon( IconConstants.PAGEMAG_24 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "FileSpy";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return FileSpyMain.createFrame();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "Searches for and through files";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        return IconConstants.getPageMagImages();
    }
}
