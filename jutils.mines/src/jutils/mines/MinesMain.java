package jutils.mines;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner;
import jutils.mines.ui.MinesFrameView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MinesMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( () -> createFrame(), false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static JFrame createFrame()
    {
        MinesFrameView frameView = new MinesFrameView();
        JFrame frame = frameView.getView();

        return frame;
    }
}
