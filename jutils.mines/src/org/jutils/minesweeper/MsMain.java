package org.jutils.minesweeper;

import javax.swing.JFrame;

import org.jutils.core.ui.app.FrameRunner;
import org.jutils.minesweeper.ui.MsFrameView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MsMain
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
        MsFrameView frameView = new MsFrameView();
        JFrame frame = frameView.getView();

        return frame;
    }
}
