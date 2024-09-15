package jutils.core.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.function.Consumer;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import jutils.core.SwingUtils;
import jutils.core.concurrent.ScheduledTask;
import jutils.core.ui.event.updater.CheckBoxUpdater;
import jutils.core.ui.fields.DoubleFormField;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MessageInputView implements IView<JComponent>
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
    private final Consumer<byte []> msgNotifier;
    /**  */
    private final MessageScheduler scheduler;

    /***************************************************************************
     * @param msgNotifier
     **************************************************************************/
    public MessageInputView( Consumer<byte []> msgNotifier )
    {
        this.scheduleField = new JCheckBox( "Schedule Messages" );
        this.rateField = new DoubleFormField( "Message Rate", "Hz", 4, 1.0,
            1000.0 );
        this.autoEnabledCheckbox = new JCheckBox( "Auto-Reply" );

        this.adHocView = new TextHexView();
        this.scheduleView = new TextHexView();
        this.autoReplyView = new TextHexView();

        this.msgNotifier = msgNotifier;
        this.scheduler = new MessageScheduler( msgNotifier );

        this.view = createView();

        adHocView.setText( "Send Me" );
        scheduleView.setText( "Schedule Me" );
        autoReplyView.setText( "Reply with Me" );

        adHocView.addEnterListener( ( d ) -> sendAdHoc( d ) );
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
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Ad Hoc", adHocView.getView() );
        tabs.addTab( "Scheduled", scheduleView.getView() );
        tabs.addTab( "Auto-Reply", autoReplyView.getView() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( tabs, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        scheduleField.addActionListener(
            new CheckBoxUpdater( ( b ) -> handleScheduleChanged( b ) ) );

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
    private void handleScheduleChanged( boolean scheduleMessages )
    {
        if( scheduleMessages )
        {
            rateField.setEditable( false );
            scheduler.start( rateField.getValue(), scheduleView.getData() );
        }
        else
        {
            rateField.setEditable( true );
            scheduler.stop();
        }
    }

    /***************************************************************************
     * @param msgBytes
     **************************************************************************/
    private void sendAdHoc( byte [] msgBytes )
    {
        msgNotifier.accept( msgBytes );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void close()
    {
        scheduler.stop();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte [] getAdHocMessageText()
    {
        return adHocView.getData();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte [] getAutoMessageText()
    {
        return autoReplyView.getData();
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setAdHocMessageText( byte [] text )
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class MessageScheduler
    {
        /**  */
        private final Consumer<byte []> msgNotifier;

        /**  */
        private ScheduledTask scheduler;
        /**  */
        private byte [] msgToSend;

        /**
         * @param msgNotifier
         */
        public MessageScheduler( Consumer<byte []> msgNotifier )
        {
            this.msgNotifier = msgNotifier;

            this.scheduler = null;
            this.msgToSend = new byte[0];
        }

        /**
         * 
         */
        private void handleTask()
        {
            msgNotifier.accept( msgToSend );
        }

        /**
         * @param rate
         * @param msg
         */
        public void start( double rate, byte [] msg )
        {
            if( scheduler == null )
            {
                this.msgToSend = msg;
                this.scheduler = new ScheduledTask( rate,
                    ( c, m ) -> handleTask() );
                scheduler.start();
            }
        }

        /**
         * 
         */
        public void stop()
        {
            if( scheduler != null )
            {
                scheduler.stop();
                scheduler = null;
            }
        }
    }
}
