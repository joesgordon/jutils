package org.jutils.chart.app;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.options.OptionsSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JChartAppConstants
{
    /**  */
    public static final String APP_NAME = "JChart";
    /**  */
    public static final File USER_APP_DIR = IOUtils.getUsersFile( ".jchart" );
    /**  */
    private static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "jchart", "options.xml" );

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
    private JChartAppConstants()
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
