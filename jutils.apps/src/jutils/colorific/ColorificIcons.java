package jutils.colorific;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import jutils.core.io.IconLoader;

/*******************************************************************************
 * Defines functions to access icons specific to the Colorific application.
 ******************************************************************************/
public final class ColorificIcons
{
    /** The icon loader to be used to access icons in this project. */
    public static final IconLoader loader = new IconLoader(
        ColorificIcons.class, "icons" );

    /** The name of the icon for picking a color that is 24 x 24 pixels. */
    public static final String COLOR_PICKER_024 = "colorPicker024.png";
    /** The name of the icon for the application that is 24 x 24 pixels. */
    public static final String COLOR_024 = "color024.png";

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private ColorificIcons()
    {
    }

    /***************************************************************************
     * Provides the list of images for the Colorific Application.
     * @return list of application images.
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( "color" ) );
    }

    /***************************************************************************
     * Returns the Colorific icon with the provided name.
     * @param name the filename of the icon to be returned.
     * @return the icon of the provided name; {@code null} if no icon of the
     * provided name exists.
     **************************************************************************/
    public static Icon getIcon( String name )
    {
        return loader.getIcon( name );
    }
}
