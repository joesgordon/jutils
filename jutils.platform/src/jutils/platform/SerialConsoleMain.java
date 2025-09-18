package jutils.platform;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner;
import jutils.platform.ui.SerialConsoleFrame;

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
        AppRunner.DEFAULT_LAF = AppRunner.JGOODIES_LAF;

        IPlatform platform = PlatformUtils.getPlatform();

        platform.initialize();

        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static JFrame createFrame()
    {
        SerialConsoleFrame frameView = new SerialConsoleFrame();
        JFrame frame = frameView.getView();

        return frame;
    }
}
