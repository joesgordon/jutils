package org.jutils.insomnia;

import javax.swing.JFrame;

import org.jutils.core.ui.app.AppRunner;
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
        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        InsomniaFrameView frameView = new InsomniaFrameView();

        return frameView.getView();
    }
}
