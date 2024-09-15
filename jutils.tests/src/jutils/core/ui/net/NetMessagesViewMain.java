package jutils.core.ui.net;

import java.awt.event.ActionListener;
import java.nio.ByteBuffer;
import java.util.Random;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;

import jutils.core.IconConstants;
import jutils.core.io.IStringWriter;
import jutils.core.io.StringPrintStream;
import jutils.core.net.EndPoint;
import jutils.core.net.NetMessage;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IFrameApp;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.net.NetMessagesView.IMessageFields;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetMessagesViewMain
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
        /**  */
        private final Random rand = new Random( 42 );

        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();

            // HexMessagePanel panel = new HexMessagePanel();
            NetMessagesView view = new NetMessagesView( new MessageFields(),
                new MsgWriter() );

            view.setMsgsPerPage( 5 );

            frameView.setContent( view.getView() );

            frameView.setTitle( "Net Messages View Test" );
            frameView.setSize( 680, 400 );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            JFrame frame = frameView.getView();

            frame.setIconImages( IconConstants.getPageMagImages() );

            view.setOpenVisible( true );

            ActionListener listener = ( e ) -> view.addMessage(
                buildMessage() );
            Icon icon = IconConstants.getIcon( IconConstants.EDIT_ADD_16 );
            Action action = new ActionAdapter( listener, "Add Message", icon );

            view.addToToolbar( action );

            return frame;
        }

        /**
         * @return
         */
        private NetMessage buildMessage()
        {
            int len = 20 + rand.nextInt( 1004 );
            byte [] bytes = new byte[len];
            rand.nextBytes( bytes );

            NetMessage msg = new NetMessage( true, new EndPoint( 186 ),
                new EndPoint( 282 ), bytes );
            return msg;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void finalizeGui()
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class MessageFields implements IMessageFields
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public int getFieldCount()
        {
            return 1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getFieldName( int index )
        {
            if( index == 0 )
            {
                return "First Int";
            }

            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getFieldValue( NetMessage message, int index )
        {
            if( index == 0 )
            {
                return "" + ByteBuffer.wrap( message.contents ).getInt();
            }

            return null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class MsgWriter implements IStringWriter<NetMessage>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString( NetMessage item )
        {
            try( StringPrintStream str = new StringPrintStream() )
            {
                for( int i = 0; i < item.contents.length; i++ )
                {
                    str.println( "Byte %d = %02X", i, item.contents[i] );
                }

                return str.toString();
            }
        }
    }
}
