package jutils.platform;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import jutils.core.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class PlatformIcons
{
    /**  */
    public static final IconLoader loader = new IconLoader( PlatformIcons.class,
        "icons" );

    /**  */
    public static final String APP_024 = "serial_024.png";
    /**  */
    public static final String APP_032 = "serial_032.png";

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private PlatformIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( "serial_" ) );
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
