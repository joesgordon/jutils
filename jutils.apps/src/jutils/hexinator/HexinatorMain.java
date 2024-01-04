package jutils.hexinator;

import javax.swing.JFrame;

import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.hexinator.ui.HexinatorView;

/*******************************************************************************
 *
 ******************************************************************************/
public class HexinatorMain
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private HexinatorMain()
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
        HexinatorView view = new HexinatorView();

        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setTitle( "Hexinator" );
        frameView.setSize( 800, 800 );
        frameView.setContent( view.getView() );

        return frame;
    }
}
