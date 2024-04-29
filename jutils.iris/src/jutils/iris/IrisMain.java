package jutils.iris;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner;
import jutils.iris.ui.IrisFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IrisMain
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private IrisMain()
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
        IrisFrame frame = new IrisFrame();

        return frame.getView();
    }
}
