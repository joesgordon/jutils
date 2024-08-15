package jutils.telemetry;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner;
import jutils.telemetry.ui.ch09.TedeFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TedeMain
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private TedeMain()
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
    public static JFrame createFrame()
    {
        TedeFrame view = new TedeFrame();
        JFrame frame = view.getView();

        return frame;
    }
}
