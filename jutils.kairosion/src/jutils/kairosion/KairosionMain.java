package jutils.kairosion;

import javax.swing.JFrame;

import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.kairosion.ui.KairosionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class KairosionMain
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
    public static JFrame createFrame()
    {
        StandardFrameView view = new StandardFrameView();
        JFrame frame = view.getView();
        KairosionView mainView = new KairosionView();

        view.setTitle( "Kairosion" );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setSize( 500, 800 );
        view.setContent( mainView.getView() );

        frame.setIconImages( KairosionIcons.getAppImages() );

        return frame;
    }
}
