package jutils.filespy;

import java.awt.Dimension;

import javax.swing.JFrame;

import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IFrameApp;
import jutils.filespy.ui.RegexPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RegexMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        IFrameApp app = new IFrameApp()
        {
            @Override
            public void finalizeGui()
            {
            }

            @Override
            public JFrame createFrame()
            {
                RegexPanel view = new RegexPanel();
                StandardFrameView frameView = new StandardFrameView();
                JFrame frame = frameView.getView();

                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setTitle( "Regex Friend" );
                frame.setContentPane( view.getView() );
                frame.setSize( new Dimension( 500, 500 ) );

                return frame;
            }
        };

        AppRunner.invokeLater( app );
    }
}
