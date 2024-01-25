package jutils.iris;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import jutils.core.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class IrisIcons
{
    /**  */
    public static final IconLoader loader = new IconLoader( IrisIcons.class,
        "icons" );

    /**  */
    public static final String APP_024 = "iris_024.png";
    /**  */
    public static final String APP_032 = "iris_032.png";

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private IrisIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( "iris_" ) );
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
