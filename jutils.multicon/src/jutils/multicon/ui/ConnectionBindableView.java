package jutils.multicon.ui;

import java.awt.*;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import jutils.core.OptionUtils;
import jutils.core.io.LogUtils;
import jutils.core.net.*;
import jutils.core.ui.TitleView;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.net.NetMessagesView;
import jutils.multicon.ui.BindingFrameView.IBindableView;

/*******************************************************************************
 *
 ******************************************************************************/
public class ConnectionBindableView<T> implements IBindableView<T>
{
    /**  */
    private final IConnectionView<T> connectionView;

    /**  */
    private final JPanel view;
    /**  */
    private final NetMessagesView messagesPanel;
    /**  */
    private final MessageInputPanel inputPanel;

    /**  */
    private ConnectionListener commModel;

    /***************************************************************************
     * @param connectionView
     **************************************************************************/
    public ConnectionBindableView( IConnectionView<T> connectionView )
    {
        this.connectionView = connectionView;

        IUpdater<NetMessage> msgNotifier = ( m ) -> SwingUtilities.invokeLater(
            () -> addMessage( m ) );

        this.messagesPanel = new NetMessagesView();
        this.inputPanel = new MessageInputPanel( msgNotifier );
        this.view = createView();

        this.commModel = null;

        inputPanel.setEditable( false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        TitleView msgsTitlePanel = new TitleView( "Messages",
            messagesPanel.getView() );

        TitleView cfgTitlePanel = new TitleView( "Configuration",
            connectionView.getView() );

        panel.add( cfgTitlePanel.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 6, 6, 4, 6 ), 0, 0 ) );

        panel.add( inputPanel.getView(),
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );

        panel.add( msgsTitlePanel.getView(),
            new GridBagConstraints( 1, 0, 1, 2, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T getData()
    {
        return connectionView.getData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( T data )
    {
        connectionView.setData( data );
    }

    /***************************************************************************
     * @param bind
     **************************************************************************/
    @Override
    public void bind() throws IOException
    {
        @SuppressWarnings( "resource")
        IConnection connection = connectionView.createConnection();

        setConnection( connection );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void unbind() throws IOException
    {
        inputPanel.setEditable( false );
        inputPanel.close();

        messagesPanel.clearMessages();

        if( commModel != null )
        {
            try
            {
                commModel.close();
                commModel = null;
            }
            finally
            {
                connectionView.setEditable( true );
            }
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isBound()
    {
        return commModel != null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return connectionView.getTitle();
    }

    /***************************************************************************
     * @param msg
     **************************************************************************/
    public void addMessage( NetMessage msg )
    {
        // LogUtils.printDebug(
        // "rx'd msg: " + HexUtils.toHexString( msg.contents ) );
        messagesPanel.addMessage( msg );

        if( inputPanel.isAutoReplySet() )
        {
            ConnectionListener commModel = this.commModel;

            if( commModel != null )
            {
                try
                {
                    commModel.sendMessage( inputPanel.getAutoMessageText() );
                }
                catch( IOException e )
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /***************************************************************************
     * @param connection
     * @throws IOException
     **************************************************************************/
    public void setConnection( IConnection connection )
    {
        if( commModel != null )
        {
            try
            {
                unbind();
            }
            catch( IOException ex )
            {
                displayErrorMessage( ex.getMessage() );
            }
        }

        IUpdater<NetMessage> rxListener;
        IUpdater<String> errListener;

        rxListener = ( m ) -> SwingUtilities.invokeLater(
            () -> addMessage( m ) );
        errListener = ( m ) -> SwingUtilities.invokeLater(
            () -> displayErrorMessage( m ) );

        commModel = new ConnectionListener();
        commModel.addMessageListener( rxListener );
        commModel.addErrorListener( errListener );
        commModel.start( connection );

        inputPanel.setConnection( connection );

        connectionView.setEditable( false );
        inputPanel.setEditable( true );
    }

    /***************************************************************************
     * @param errorMsg
     **************************************************************************/
    private void displayErrorMessage( String errorMsg )
    {
        LogUtils.printError( errorMsg );

        OptionUtils.showErrorMessage( getView(), errorMsg,
            "Communication Error" );
    }
}
