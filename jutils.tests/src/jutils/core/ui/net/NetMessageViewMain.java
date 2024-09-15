package jutils.core.ui.net;

import java.nio.charset.Charset;

import javax.swing.JFrame;

import jutils.core.net.EndPoint;
import jutils.core.net.NetMessage;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetMessageViewMain
{
    /***************************************************************************
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new HexMessageApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class HexMessageApp implements IFrameApp
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            JFrame frame = new JFrame();

            NetMessageView panel = new NetMessageView();

            frame.setContentPane( panel.getView() );

            frame.setSize( 680, 400 );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            byte [] contents = "So long and thanks f".getBytes(
                Charset.forName( "UTF-8" ) );

            NetMessage msg = new NetMessage( true, new EndPoint( 186 ),
                new EndPoint( 282 ), contents );

            panel.setData( msg );

            return frame;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void finalizeGui()
        {
        }
    }
}
