package org.jutils.minesweeper;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import org.jutils.core.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MsIcons
{
    /**  */
    public static final String APP_PREFIX = "bomb";

    /** The loader used to access the icons. */
    public final static IconLoader loader = new IconLoader( MsIcons.class,
        "icons" );

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private MsIcons()
    {
    }

    /***************************************************************************
     * Builds and returns the list of application images.
     * @return the list of application images or an empty list if a programmatic
     * error occurs.
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( APP_PREFIX ) );
    }

    /***************************************************************************
     * Returns the icon with the provided name.
     * @param name the name/relative path to the icon.
     * @return the icon loaded or null if none found.
     **************************************************************************/
    public static ImageIcon getIcon( String name )
    {
        return loader.getIcon( name );
    }
}
