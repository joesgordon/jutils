package org.jutils.apps.filespy;

import javax.swing.JFrame;

import org.jutils.apps.filespy.ui.FileSpyFrameView;
import org.jutils.core.ui.app.IFrameApp;

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
