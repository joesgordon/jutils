package jutils.demo;

import javax.swing.JFrame;

import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner.IFrameCreator;
import jutils.demo.ui.DemoView;

/*******************************************************************************
 * Defines the application that shows a frame that displays all UI and
 * functionality in JUtils.
 ******************************************************************************/
public class DemoApp implements IFrameCreator
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        DemoView view = new DemoView();

        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 500, 500 );
        frameView.setTitle( "All the Things" );
        frameView.setContent( view.getView() );

        return frameView.getView();
    }
}
