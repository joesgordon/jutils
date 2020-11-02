package org.jutils.insomnia;

import javax.swing.JFrame;

import org.jutils.core.ui.app.FrameRunner;
import org.jutils.insomnia.ui.InsomniaFrameView;

/***************************************************************************
 * 
 **************************************************************************/
public class InsomniaMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        InsomniaFrameView frameView = new InsomniaFrameView();

        // TODO Auto-generated method stub

        return frameView.getView();
    }
}
