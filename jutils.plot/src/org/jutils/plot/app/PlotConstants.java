package org.jutils.plot.app;

import java.io.File;

import org.jutils.core.io.IOUtils;
import org.jutils.core.io.options.OptionsSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlotConstants
{
    /**  */
    public static final String APP_NAME = "Plot";
    /**  */
    private static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "plot", "options.xml" );

    /**  */
    private static final OptionsSerializer<UserData> OPTIONS;

    static
    {
        OPTIONS = OptionsSerializer.getOptions( UserData.class,
            USER_OPTIONS_FILE );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private PlotConstants()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<UserData> getOptions()
    {
        return OPTIONS;
    }
}