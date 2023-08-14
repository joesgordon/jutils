package jutils.core.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner;
import jutils.core.ui.hex.BitArrayView;
import jutils.core.utils.BitArray;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitArrayViewTest
{
    /***************************************************************************
     * @param args Ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( () -> createFrame(), true );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JFrame createFrame()
    {
        JFrame frame = new JFrame();
        List<byte []> list = new ArrayList<>();

        list.add( new byte[] { 0x65, 0x43 } );
        list.add( new byte[] { ( byte )0xA3, 0x1F } );
        list.add( new byte[] { ( byte )0xDE, ( byte )0xAD } );
        list.add( new byte[] { ( byte )0xBE, ( byte )0xEF } );

        BitArrayView view = new BitArrayView( list );

        view.setData( new BitArray() );

        frame.setContentPane( view.getView() );
        frame.setSize( 500, 500 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }
}
