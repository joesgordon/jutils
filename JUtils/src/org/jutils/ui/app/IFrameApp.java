package org.jutils.ui.app;

import javax.swing.JFrame;

/*******************************************************************************
 * Defines methods for creating an application's frame.
 ******************************************************************************/
public interface IFrameApp
{
    /***************************************************************************
     * Creates the frame and all other UI (tray icons, etc.)
     * @return the frame for this application.
     **************************************************************************/
    public JFrame createFrame();

    /***************************************************************************
     * Called after the frame is set as visible.
     **************************************************************************/
    public void finalizeGui();
}
