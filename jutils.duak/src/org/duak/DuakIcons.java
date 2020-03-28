package org.duak;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import org.jutils.core.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class DuakIcons
{
    /**  */
    public static final IconLoader loader = new IconLoader( DuakIcons.class,
        "icons" );

    /**  */
    public static final String APP_024 = "duak_024.png";
    /**  */
    public static final String APP_032 = "duak_032.png";

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private DuakIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( "duak_" ) );
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
