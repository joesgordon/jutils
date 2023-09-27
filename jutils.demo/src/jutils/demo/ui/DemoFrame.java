package jutils.demo.ui;

import java.awt.event.ActionListener;

import javax.swing.JFrame;

import jutils.core.SwingUtils;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.model.IView;

/***************************************************************************
 * 
 **************************************************************************/
public class DemoFrame implements IView<JFrame>
{
    /**  */
    private StandardFrameView view;
    /**  */
    private DemoView demoPanel;

    /***************************************************************************
     * 
     **************************************************************************/
    public DemoFrame()
    {
        this.demoPanel = new DemoView();

        this.view = createView();
    }

    /**
     * @return
     */
    private StandardFrameView createView()
    {
        StandardFrameView frameView = new StandardFrameView();

        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 500, 500 );
        frameView.setTitle( "All the Things" );
        frameView.setContent( demoPanel.getView() );

        ActionListener listener = ( e ) -> SwingUtils.toggleFullScreen(
            getView() );

        SwingUtils.addKeyListener( demoPanel.getView(), "F11", listener,
            "F11_PRESSED", true );

        return frameView;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return view.getView();
    }

}
