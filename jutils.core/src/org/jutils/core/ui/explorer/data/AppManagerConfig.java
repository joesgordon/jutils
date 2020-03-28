package org.jutils.core.ui.explorer.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AppManagerConfig
{
    /**  */
    public boolean useCustom;

    /**  */
    public final List<ExtensionConfig> exts;
    /**  */
    public final List<ApplicationConfig> apps;

    /***************************************************************************
     * 
     **************************************************************************/
    public AppManagerConfig()
    {
        useCustom = true;
        exts = new ArrayList<>();
        apps = new ArrayList<>();
    }

    /***************************************************************************
     * @param ext
     * @return
     **************************************************************************/
    public boolean containsExtension( String ext )
    {
        for( ExtensionConfig ed : exts )
        {
            if( ed.ext.equals( ext ) )
            {
                return true;
            }
        }

        return false;
    }
}
