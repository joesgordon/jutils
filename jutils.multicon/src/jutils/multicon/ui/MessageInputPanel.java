package jutils.multicon.ui;

import java.io.IOException;

import javax.swing.JComponent;

import jutils.core.OptionUtils;
import jutils.core.net.IConnection;
import jutils.core.net.NetMessage;
import jutils.core.ui.MessageInputView;
import jutils.core.ui.TitleView;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MessageInputPanel implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final MessageInputView msgView;

    /**  */
    private final IUpdater<NetMessage> msgNotifier;

    /**  */
    private IConnection connection;

    /***************************************************************************
     * @param msgNotifier
     **************************************************************************/
    public MessageInputPanel( IUpdater<NetMessage> msgNotifier )
    {
        this.msgView = new MessageInputView( ( m ) -> handleMsgSent( m ) );
        this.view = createView();

        this.msgNotifier = msgNotifier;

        this.connection = null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        TitleView titlePanel = new TitleView( "Send/Receive Messages",
            msgView.getView() );

        return titlePanel.getView();
    }

    /***************************************************************************
     * @param msgBytes
     **************************************************************************/
    private void handleMsgSent( byte[] msgBytes )
    {
        if( connection != null )
        {
            try
            {
                NetMessage netMsg = connection.sendMessage( msgBytes );

                msgNotifier.update( netMsg );
            }
            catch( IOException ex )
            {
                OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                    "Error sending message to " + connection.getRemote() );
            }
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @param connection
     **************************************************************************/
    public void setConnection( IConnection connection )
    {
        close();
        this.connection = connection;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void close()
    {
        msgView.close();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte[] getAdHocMessageText()
    {
        return msgView.getAdHocMessageText();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte[] getAutoMessageText()
    {
        return msgView.getAutoMessageText();
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setAdHocMessageText( byte[] text )
    {
        msgView.setAdHocMessageText( text );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        msgView.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void selectAll()
    {
        msgView.selectAll();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isAutoReplySet()
    {
        return msgView.isAutoReplySet();
    }
}
