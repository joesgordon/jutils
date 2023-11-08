package jutils.platform.ui;

import javax.swing.JFrame;

import jutils.core.ui.StandardFrameView;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DemoPlatformFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final SerialParamsView configView;

    /***************************************************************************
     * 
     **************************************************************************/
    public DemoPlatformFrame()
    {
        this.view = new StandardFrameView();
        this.configView = new SerialParamsView();

        view.setTitle( "Demo Platform Frame" );
        view.setSize( 800, 800 );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setContent( configView.getView() );
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
