package jutils.hexulator;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import jutils.core.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class HexulatorIcons
{
    /**  */
    public static final IconLoader loader = new IconLoader(
        HexulatorIcons.class, "icons" );

    /**  */
    public static final String APP_024 = "hexulator_024.png";
    /**  */
    public static final String APP_032 = "hexulator_032.png";

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private HexulatorIcons()
    {
    }

    /***************************************************************************
     * @return
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
