package jutils.math;

import javax.swing.JFrame;

import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.math.ui.MathDemoView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MathDemoMain
{
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
    private static JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        MathDemoView demoView = new MathDemoView();

        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setTitle( "Math Components Demo" );
        frameView.setSize( 500, 500 );
        frameView.setContent( demoView.getView() );

        return frameView.getView();
    }
}
