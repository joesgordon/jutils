package jutils.platform;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner;
import jutils.platform.ui.DemoPlatformFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DemoPlatformMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main2( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.SIMPLE_LAF;

        PlatformUtils.checkResources();

        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        DemoPlatformFrame frameView = new DemoPlatformFrame();
        JFrame frame = frameView.getView();

        return frame;
    }
}
