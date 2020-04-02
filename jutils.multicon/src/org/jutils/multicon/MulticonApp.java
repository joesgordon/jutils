package org.jutils.multicon;

import javax.swing.JFrame;

import org.jutils.core.ui.app.IFrameApp;
import org.jutils.multicon.ui.MulticonFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticonApp implements IFrameApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        MulticonFrame frame = new MulticonFrame();

        return frame.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }
}
