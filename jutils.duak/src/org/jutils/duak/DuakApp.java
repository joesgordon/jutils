package org.jutils.duak;

import javax.swing.JFrame;

import org.jutils.core.ui.app.IFrameApp;
import org.jutils.duak.ui.DuakFrame;

public class DuakApp implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        DuakFrame df = new DuakFrame();
        JFrame frame = df.getView();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }
}
