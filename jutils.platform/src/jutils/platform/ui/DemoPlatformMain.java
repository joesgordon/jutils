package jutils.platform.ui;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DemoPlatformMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.SIMPLE_LAF;

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
