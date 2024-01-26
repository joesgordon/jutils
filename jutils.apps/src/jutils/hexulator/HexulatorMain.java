package jutils.hexulator;

import javax.swing.JFrame;

import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.hexulator.ui.HexulatorView;

/*******************************************************************************
 *
 ******************************************************************************/
public class HexulatorMain
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private HexulatorMain()
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
    private static JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();
        HexulatorView view = new HexulatorView();

        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setTitle( "Hexulator" );
        frameView.setSize( 800, 800 );
        frameView.setContent( view.getView() );
        frame.setIconImages( HexulatorIcons.getAppImages() );

        return frame;
    }
}
