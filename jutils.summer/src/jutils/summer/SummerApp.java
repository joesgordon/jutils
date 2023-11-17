package jutils.summer;

import javax.swing.JFrame;

import jutils.core.ui.app.IFrameApp;
import jutils.summer.ui.SummerView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SummerApp implements IFrameApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        SummerView view = new SummerView();
        JFrame frame = view.getView();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }
}
