package jutils.core.pcapng.ui;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcapngMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.SIMPLE_LAF;

        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static JFrame createFrame()
    {
        PcapngFrame view = new PcapngFrame();

        return view.getView();
    }
}
