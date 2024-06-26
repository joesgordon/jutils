package jutils.plot.app;

import java.io.File;

import jutils.core.io.IOUtils;
import jutils.core.io.options.OptionsSerializer;
import jutils.core.io.xs.XsOptions;

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
        OPTIONS = XsOptions.getOptions( UserData.class,
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
