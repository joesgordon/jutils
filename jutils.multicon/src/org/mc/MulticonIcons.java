package org.mc;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import org.jutils.core.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticonIcons
{
    /**  */
    public static final String MULTICON_016 = "multicon016.png";
    /**  */
    public static final String MULTICON_024 = "multicon024.png";

    /** The loader used to access the icons. */
    public final static IconLoader loader = new IconLoader( MulticonIcons.class,
        "icons" );

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private MulticonIcons()
    {
    }

    /***************************************************************************
     * Builds and returns the list of application images.
     * @return the list of application images or an empty list if a programmatic
     * error occurs.
     **************************************************************************/
    public static List<Image> getMulticonImages()
    {
        return loader.getImages( IconLoader.buildNameList( "multicon" ) );
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
