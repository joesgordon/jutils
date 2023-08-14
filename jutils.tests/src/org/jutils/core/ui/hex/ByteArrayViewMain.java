package org.jutils.core.ui.hex;

import java.util.Random;

import javax.swing.JFrame;

import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ByteArrayViewMain
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
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();
        ByteArrayView view = new ByteArrayView();
        Random r = new Random();

        frameView.getFileMenu().add( "test" ).addActionListener( ( e ) -> {
            int count = r.nextInt( 101 );
            byte [] b = new byte[count];

            for( int i = 0; i < b.length; i++ )
            {
                b[i] = ( byte )r.nextInt( 256 );
            }
            view.setData( b );
        } );

        view.setData( new byte[100] );

        frameView.setSize( 800, 800 );
        frameView.setTitle( "ByteArrayView Test" );
        frameView.setContent( view.getView() );

        return frame;
    }
}
