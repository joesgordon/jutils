package jutils.telemetry;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner;
import jutils.telemetry.ch09.ui.TmatsEditorFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TmatsEditorMain
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private TmatsEditorMain()
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
        TmatsEditorFrame view = new TmatsEditorFrame();
        JFrame frame = view.getView();

        return frame;
    }
}
