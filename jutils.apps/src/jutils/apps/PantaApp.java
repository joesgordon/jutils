package jutils.apps;

import javax.swing.JFrame;

import jutils.apps.ui.PantaView;
import jutils.core.laf.SimpleLookAndFeel;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.AppRunner.IFrameCreator;

/*******************************************************************************
 *
 ******************************************************************************/
public class PantaApp implements IFrameCreator
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        PantaView view = new PantaView();

        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 500, 500 );
        frameView.setTitle( "All the Things" );
        frameView.setContent( view.getView() );

        return frameView.getView();
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new PantaApp(), true,
            SimpleLookAndFeel.class.getName() );
    }
}
