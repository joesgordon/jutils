package jutils.telemetry;

import java.awt.Image;
import java.util.List;

import jutils.core.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TelemetryIcons
{
    /**  */
    public static final IconLoader loader = new IconLoader(
        TelemetryIcons.class, "resources" );

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( "tm_" ) );
    }

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private TelemetryIcons()
    {
    }
}
