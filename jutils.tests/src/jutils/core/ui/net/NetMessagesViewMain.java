package jutils.core.ui.net;

import java.awt.event.ActionListener;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import jutils.core.IconConstants;
import jutils.core.OptionUtils;
import jutils.core.OptionUtils.YncAnswer;
import jutils.core.io.IStringWriter;
import jutils.core.io.StringPrintStream;
import jutils.core.io.parsers.IntegerParser;
import jutils.core.net.EndPoint;
import jutils.core.net.NetMessage;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IFrameApp;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.FileDropTarget;
import jutils.core.ui.event.FileDropTarget.IFileDropEvent;
import jutils.core.ui.event.ItemActionEvent;
import jutils.core.ui.net.NetMessagesTableConfig.IMessageFields;

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
        AppRunner.DEFAULT_LAF = AppRunner.JGOODIES_LAF;

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
            JFrame frame = frameView.getView();

            // HexMessagePanel panel = new HexMessagePanel();
            NetMessagesView view = new NetMessagesView( new MessageFields(),
                new MsgWriter() );

            view.setMsgsPerPage( 5 );
            view.setOpenVisible( true );
            view.getView().setDropTarget(
                new FileDropTarget( ( e ) -> handleFileDropped( view, e ) ) );

            frameView.setTitle( "Net Messages View Test" );
            frameView.setSize( 680, 400 );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frameView.setContent( view.getView() );

            createMenus( frameView.getMenuBar(), view );

            frame.setIconImages( IconConstants.getPageMagImages() );

            ActionListener listener = ( e ) -> view.addMessage(
                buildMessage() );
            Icon icon = IconConstants.getIcon( IconConstants.EDIT_ADD_16 );
            Action action = new ActionAdapter( listener, "Add Message", icon );

            view.addToToolbar( action );

            return frame;
        }

        /**
         * @param view
         * @param e
         */
        private void handleFileDropped( NetMessagesView view,
            ItemActionEvent<IFileDropEvent> evt )
        {
            List<File> files = evt.getItem().getFiles();

            if( files.isEmpty() )
            {
                return;
            }

            File file = files.get( 0 );

            if( files.size() > 1 )
            {
                YncAnswer ans = OptionUtils.showQuestionMessage( view.getView(),
                    "Only 1 file may be loaded. Do you want to load\n" +
                        file.getAbsolutePath(),
                    "WARNING", "Load", "Cancel" );

                if( ans != YncAnswer.YES )
                {
                    return;
                }
            }

            view.openNetMsgsFile( file );
        }

        /**
         * @param menubar
         * @param view
         */
        private void createMenus( JMenuBar menubar, NetMessagesView view )
        {
            JMenu menu = new JMenu( "Options" );

            menu.setMnemonic( 'O' );

            JMenuItem item = new JMenuItem( "Set Row Count" );

            item.addActionListener( ( e ) -> handleSetRowCount( view ) );

            menu.add( item );

            menubar.add( menu );
        }

        /**
         * @param view
         */
        private void handleSetRowCount( NetMessagesView view )
        {
            IntegerParser parser = new IntegerParser( 1, null );
            Integer ans = OptionUtils.promptForValue( view.getView(),
                "Row Count", parser, "Enter the number of messages per page" );

            if( ans != null )
            {
                view.setMsgsPerPage( ans.intValue() );
            }
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
    private static class MessageFields implements IMessageFields<NetMessage>
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
