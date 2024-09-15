package jutils.demo;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner.IFrameCreator;
import jutils.demo.ui.DemoFrame;

/*******************************************************************************
 * Defines the application that shows a frame that displays all UI and
 * functionality in JUtils.
 ******************************************************************************/
public class DemoApp implements IFrameCreator
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        DemoFrame df = new DemoFrame();

        return df.getView();
    }
}
