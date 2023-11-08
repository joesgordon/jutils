package jutils.platform.ui;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner;
import jutils.platform.IPlatform;
import jutils.platform.PlatformUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialConsoleMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.SIMPLE_LAF;

        IPlatform platform = PlatformUtils.getPlatform();

        platform.initialize();

        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        SerialConsoleFrame frameView = new SerialConsoleFrame();
        JFrame frame = frameView.getView();

        return frame;
    }
}
