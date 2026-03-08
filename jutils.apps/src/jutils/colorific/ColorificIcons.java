package jutils.colorific;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import jutils.core.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class ColorificIcons
{
    /** The icon loader to be used to access icons in this project. */
    public static final IconLoader loader = new IconLoader(
        ColorificIcons.class, "icons" );

    /**  */
    public static final String COLOR_PICKER_024 = "colorPicker024.png";
    /**  */
    public static final String COLOR_024 = "color024.png";

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private ColorificIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getColorImages()
    {
        return loader.getImages( IconLoader.buildNameList( "color" ) );
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    public static ImageIcon getIcon( String name )
    {
        return loader.getIcon( name );
    }
}
