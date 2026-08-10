package jutils.core.net;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.TitleView;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IView;
import jutils.core.ui.net.NetMessagesView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MessagingView implements IView<JComponent>
{
    /**  */
    private final IView<?> connectionView;

    /**  */
    private final JPanel view;
    /**  */
    private final NetMessagesView messagesPanel;
    /**  */
    private final MessageInputPanel inputPanel;

    /***************************************************************************
     * @param connectionView
     * @param msgNotifier
     **************************************************************************/
    public MessagingView( IView<?> connectionView,
        IUpdater<NetMessage> msgNotifier )
    {
        this.connectionView = connectionView;

        this.messagesPanel = new NetMessagesView();
        this.inputPanel = new MessageInputPanel( msgNotifier );
        this.view = createView();

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

        JPanel cfgComp = cfgTitlePanel.getView();

        Dimension dim = cfgComp.getMinimumSize();
        dim.width = 200;
        cfgComp.setMinimumSize( dim );

        panel.add( cfgComp,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 6, 6, 6, 0 ), 0, 0 ) );

        panel.add( msgsTitlePanel.getView(),
            new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 6, 6, 6, 6 ), 0, 0 ) );

        panel.add( inputPanel.getView(),
            new GridBagConstraints( 0, 1, 2, 1, 0.0, 0.0,
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
     * @param msg
     **************************************************************************/
    public void addMessage( NetMessage msg )
    {
        messagesPanel.addMessage( msg );
    }
}
