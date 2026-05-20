package jutils.math.dsp;

import javax.swing.JFrame;

import jutils.core.ui.StandardFrameView;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DspTestFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;

    /***************************************************************************
     * 
     **************************************************************************/
    public DspTestFrame()
    {
        this.view = new StandardFrameView();

        view.setTitle( "DSP Test Frame" );
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
