package jutils.multicon;

import javax.swing.JFrame;

import jutils.core.ui.app.IFrameApp;
import jutils.core.ui.model.IView;
import jutils.multicon.ui.MulticonFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticonApp implements IFrameApp
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        IView<JFrame> frame = new MulticonFrame();

        return frame.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }
}
