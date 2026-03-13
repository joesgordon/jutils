package jutils.hexulator;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import jutils.core.io.IconLoader;

/*******************************************************************************
 * Defines the functions to access icons specific to the Hexulator application.
 ******************************************************************************/
public final class HexulatorIcons
{
    /** The icon loader to be used to access icons in this project. */
    public static final IconLoader loader = new IconLoader(
        HexulatorIcons.class, "icons" );

    /** The name of the icon for the application that is 24 x 24 pixels. */
    public static final String APP_024 = "hexulator_024.png";
    /** The name of the icon for the application that is 32 x 32 pixels. */
    public static final String APP_032 = "hexulator_032.png";

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private HexulatorIcons()
    {
    }

    /***************************************************************************
     * Provides the list of images for the Hexulator Application.
     * @return list of application images.
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( "hexulator_" ) );
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
