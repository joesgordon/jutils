package org.jutils.mines;

import javax.swing.JFrame;

import org.jutils.core.ui.app.FrameRunner;
import org.jutils.mines.ui.MinesFrameView;

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
        FrameRunner.invokeLater( () -> createFrame(), false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        MinesFrameView frameView = new MinesFrameView();
        JFrame frame = frameView.getView();

        return frame;
    }
}
