package jutils.colorific;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import jutils.core.ui.IToolView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ColorificTool implements IToolView
{
    /***************************************************************************
     * 
     **************************************************************************/
    public ColorificTool()
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        ColorificView view = new ColorificView();

        return view.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "Color Chooser";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Color Chooser";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ImageIcon getIcon24()
    {
        return ColorificIcons.loader.getIcon( ColorificIcons.COLOR_024 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        return ColorificIcons.getAppImages();
    }
}
