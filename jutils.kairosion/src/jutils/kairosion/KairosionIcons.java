package jutils.kairosion;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import jutils.core.io.IconLoader;

/*******************************************************************************
 * Defines the class to access icons for this application.
 ******************************************************************************/
public class KairosionIcons
{
    /** The icon loader to be used to access icons in this project. */
    public static final IconLoader loader = new IconLoader(
        KairosionIcons.class, "icons" );

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private KairosionIcons()
    {
    }

    /***************************************************************************
     * @return a list of the icons (as images) for this application.
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( "kairosion" ) );
    }

    /***************************************************************************
     * @return the application icon that is 24 x 24 pixels.
     **************************************************************************/
    public static Icon getApp24()
    {
        return loader.getIcon( "kairosion024.png" );
    }
}
