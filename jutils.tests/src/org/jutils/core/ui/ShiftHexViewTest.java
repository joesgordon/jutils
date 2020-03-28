package org.jutils.core.ui;

import java.util.*;

import javax.swing.JFrame;

import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;
import org.jutils.core.ui.hex.ShiftHexView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ShiftHexViewTest
{
    /***************************************************************************
     * @param args Ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new ShiftHexViewApp(), true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ShiftHexViewApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            JFrame frame = new JFrame();
            List<byte []> list = new ArrayList<>();

            list.add( new byte[] { 0x65, 0x43 } );
            list.add( new byte[] { ( byte )0xA3, 0x1F } );
            list.add( new byte[] { ( byte )0xDE, ( byte )0xAD } );
            list.add( new byte[] { ( byte )0xBE, ( byte )0xEF } );

            ShiftHexView view = new ShiftHexView();

            view.setData( generateBytes() );

            view.setListOfBytes( list );

            frame.setContentPane( view.getView() );
            frame.setSize( 500, 500 );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            return frame;
        }

        private static byte [] generateBytes()
        {
            byte [] bytes = new byte[1024];
            Random rand = new Random( 31415926 );

            rand.nextBytes( bytes );

            return bytes;
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
