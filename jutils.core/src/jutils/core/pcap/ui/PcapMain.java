package jutils.core.pcap.ui;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcapMain
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
    private static JFrame createFrame()
    {
        PcapFrame view = new PcapFrame();

        return view.getView();
    }
}
