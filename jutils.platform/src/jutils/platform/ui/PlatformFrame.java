package jutils.platform.ui;

import javax.swing.JFrame;

import jutils.core.ui.StandardFrameView;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlatformFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;

    /***************************************************************************
     * 
     **************************************************************************/
    public PlatformFrame()
    {
        this.view = new StandardFrameView();

        view.setTitle( "Platform Frame" );
        view.setSize( 800, 800 );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
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
