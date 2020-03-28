package org.jutils.apps.summer;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import org.jutils.core.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SummerIcons
{
    /**  */
    public static final IconLoader loader = new IconLoader( SummerIcons.class,
        "icons" );
    /**  */
    public static final String SUMMER_016 = "summer016.png";
    /**  */
    public static final String SUMMER_024 = "summer024.png";

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getSummerImages()
    {
        return loader.getImages( IconLoader.buildNameList( "summer" ) );
    }

    /***************************************************************************
     * @param str
     * @return
     **************************************************************************/
    public static Icon getIcon( String str )
    {
        return loader.getIcon( str );
    }
}
