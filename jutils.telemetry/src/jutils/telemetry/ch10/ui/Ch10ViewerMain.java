package jutils.telemetry.ch10.ui;

import java.io.File;

import javax.swing.JFrame;

import jutils.core.io.IOUtils;
import jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ch10ViewerMain
{
    /**  */
    public static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "ch10", "options.xml" );;

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
        Ch10ViewerFrame view = new Ch10ViewerFrame();
        JFrame frame = view.getView();

        return frame;
    }
}
