package org.jutils.apps.jhex;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import org.jutils.core.io.IconLoader;

/*******************************************************************************
 * Defines the methods for accessing the icons needed in the JHex application.
 ******************************************************************************/
public class JHexIcons
{
    /** The icon loader needed to access the project icons. */
    public static final IconLoader loader = new IconLoader( JHexIcons.class,
        "icons" );

    /** Represents a go-to operation. */
    public static final String GOTO = "goto.png";
    /** Represents the 16 pixel application icon. */
    public static final String APP_016 = "app_016.png";
    /** Represents the 24 pixel application icon. */
    public static final String APP_024 = "app_024.png";

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private JHexIcons()
    {
    }

    /***************************************************************************
     * @return the list of application images.
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( "app_" ) );
    }

    /***************************************************************************
     * The icon specified by the provided name.
     * @param name the name of the icon to load.
     * @return the icon loaded or {@code null} if it does not exist.
     * @see IconLoader#getIcon(String)
     **************************************************************************/
    public static Icon getIcon( String name )
    {
        return loader.getIcon( name );
    }
}
