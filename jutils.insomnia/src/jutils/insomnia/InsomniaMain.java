package jutils.insomnia;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner;
import jutils.insomnia.ui.InsomniaFrameView;

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
