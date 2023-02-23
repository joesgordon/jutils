package org.jutils.core.pcap.ui;

import javax.swing.JFrame;

import org.jutils.core.ui.app.FrameRunner;

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
        FrameRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        PcapngParserFrame view = new PcapngParserFrame();

        return view.getView();
    }
}
