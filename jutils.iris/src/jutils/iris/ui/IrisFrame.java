package jutils.iris.ui;

import javax.swing.JFrame;

import jutils.core.ui.StandardFrameView;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IrisFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final IrisView contentView;

    /***************************************************************************
     * 
     **************************************************************************/
    public IrisFrame()
    {
        this.frameView = new StandardFrameView();
        this.contentView = new IrisView();

        frameView.setTitle( "Iris" );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 800, 800 );
        frameView.setContent( contentView.getView() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }
}
