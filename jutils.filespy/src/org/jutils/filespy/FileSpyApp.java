package org.jutils.filespy;

import javax.swing.JFrame;

import org.jutils.core.ui.app.IFrameApp;
import org.jutils.filespy.ui.FileSpyFrameView;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileSpyApp implements IFrameApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        FileSpyFrameView frameView = new FileSpyFrameView();
        JFrame frame = frameView.getView();

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }
}
