package jutils.telemetry;

import java.io.File;

import javax.swing.JFrame;

import jutils.core.io.IOUtils;
import jutils.core.ui.app.AppRunner;
import jutils.telemetry.ui.TelemetryFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TelemetryMain
{
    /**  */
    public static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "tm", "options.xml" );;

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private TelemetryMain()
    {
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.JGOODIES_LAF;

        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        TelemetryFrame view = new TelemetryFrame();
        JFrame frame = view.getView();

        return frame;
    }
}
