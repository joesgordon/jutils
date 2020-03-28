package org.mc.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;

import javax.swing.*;

import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.net.IConnection;
import org.jutils.core.net.NetMessage;
import org.jutils.core.ui.TextHexView;
import org.jutils.core.ui.TitleView;
import org.jutils.core.ui.event.updater.CheckBoxUpdater;
import org.jutils.core.ui.event.updater.IUpdater;
import org.jutils.core.ui.fields.DoubleFormField;
import org.jutils.core.ui.model.IView;
import org.mc.MsgScheduleTask;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MessageInputPanel implements IView<JComponent>
{
    /**  */
    private final JComponent view;

    /**  */
    private final JCheckBox scheduleField;
    /**  */
    private final DoubleFormField rateField;
    /**  */
    private final JCheckBox autoEnabledCheckbox;

    /**  */
    private final TextHexView adHocView;
    /**  */
    private final TextHexView scheduleView;
    /**  */
    private final TextHexView autoReplyView;

    /**  */
    private final IUpdater<NetMessage> msgNotifier;

    /**  */
    private IConnection connection;
    /**  */
    private MsgScheduleTask task;

    /***************************************************************************
     * @param msgNotifier
     **************************************************************************/
    public MessageInputPanel( IUpdater<NetMessage> msgNotifier )
    {
        this.scheduleField = new JCheckBox( "Schedule Messages" );
        this.rateField = new DoubleFormField( "Message Rate", "Hz", 4, 1.0,
            1000.0 );
        this.autoEnabledCheckbox = new JCheckBox( "Auto-Reply" );

        this.adHocView = new TextHexView();
        this.scheduleView = new TextHexView();
        this.autoReplyView = new TextHexView();

        this.msgNotifier = msgNotifier;

        this.view = createView();

        this.connection = null;
        this.task = null;

        adHocView.setText( "Send Me" );
        scheduleView.setText( "Schedule Me" );
        autoReplyView.setText( "Reply with Me" );

        adHocView.addEnterListener( ( e ) -> sendAdHoc( e.getItem() ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        TitleView titlePanel = new TitleView( "Send/Receive Messages", panel );

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Ad Hoc", adHocView.getView() );
        tabs.addTab( "Scheduled", scheduleView.getView() );
        tabs.addTab( "Auto-Reply", autoReplyView.getView() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( tabs, BorderLayout.CENTER );

        return titlePanel.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        scheduleField.addActionListener(
            new CheckBoxUpdater( ( b ) -> scheduleMessages( b ) ) );

        rateField.setValue( 5.0 );
        rateField.getView().setMaximumSize(
            rateField.getView().getPreferredSize() );

        toolbar.add( scheduleField );

        toolbar.addSeparator();
        toolbar.add( rateField.getView() );

        toolbar.addSeparator();
        toolbar.add( autoEnabledCheckbox );

        return toolbar;
    }

    /***************************************************************************
     * @param scheduleMessages
     **************************************************************************/
    private void scheduleMessages( boolean scheduleMessages )
    {
        if( connection != null )
        {
            stopScheduled();

            if( scheduleMessages )
            {
                startScheduled();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void startScheduled()
    {
        task = new MsgScheduleTask( rateField.getValue(),
            scheduleView.getData(), connection, msgNotifier );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void stopScheduled()
    {
        if( task != null )
        {
            task.stop();
            task = null;
            scheduleField.setSelected( false );
        }
    }

    /***************************************************************************
     * @param msgBytes
     **************************************************************************/
    private void sendAdHoc( byte[] msgBytes )
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
                    "Error sending message" );
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
        stopScheduled();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte[] getAdHocMessageText()
    {
        return adHocView.getData();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte[] getAutoMessageText()
    {
        return autoReplyView.getData();
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setMessageText( byte[] text )
    {
        adHocView.setData( text );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        scheduleField.setEnabled( editable );
        rateField.setEditable( editable );
        autoEnabledCheckbox.setEnabled( editable );

        adHocView.setEditable( editable );
        scheduleView.setEditable( editable );
        autoReplyView.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void selectAll()
    {
        adHocView.selectAll();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isAutoReplySet()
    {
        return autoEnabledCheckbox.isSelected();
    }
}
