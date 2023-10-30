package jutils.demo.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import jutils.core.concurrent.ConsumerTask;
import jutils.core.concurrent.TaskThread;
import jutils.core.ui.MessageInputView;
import jutils.core.ui.hex.HexUtils;
import jutils.core.ui.model.DefaultTableItemsConfig;
import jutils.core.ui.model.IView;
import jutils.core.ui.model.ItemsTableModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MsgInputDemoView implements IView<JComponent>
{
    /**  */
    private final JComponent view;

    /**  */
    private final MessageInputView msgView;
    /**  */
    private final ItemsTableModel<DemoMsg> msgsModel;
    /**  */
    private final JTable msgsTable;

    /**  */
    private final MessagesThread thread;

    /***************************************************************************
     * 
     **************************************************************************/
    public MsgInputDemoView()
    {
        this.msgView = new MessageInputView( ( m ) -> handleInputMsg( m ) );
        this.msgsModel = new ItemsTableModel<MsgInputDemoView.DemoMsg>(
            new DemoMsgsConfig() );
        this.msgsTable = new JTable( msgsModel );

        Consumer<DemoMsg> msgConsumer = ( m ) -> SwingUtilities.invokeLater(
            () -> msgsModel.addItem( m ) );
        this.thread = new MessagesThread( msgConsumer );

        this.view = createView();

        view.addAncestorListener( new AncestorListener()
        {
            @Override
            public void ancestorRemoved( AncestorEvent event )
            {
                thread.stop();
            }

            @Override
            public void ancestorMoved( AncestorEvent event )
            {
            }

            @Override
            public void ancestorAdded( AncestorEvent event )
            {
            }
        } );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JScrollPane pane = new JScrollPane( msgsTable );

        pane.setMinimumSize( new Dimension( 200, 200 ) );
        pane.setPreferredSize( new Dimension( 200, 200 ) );

        panel.add( msgView.getView(), BorderLayout.CENTER );
        panel.add( pane, BorderLayout.SOUTH );

        return panel;
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    private void handleInputMsg( byte [] message )
    {
        if( thread.isStopped() )
        {
            thread.start();
        }

        thread.add( new DemoMsg( message ) );
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
     * 
     **************************************************************************/
    private static final class DemoMsg
    {
        /**  */
        public final LocalDateTime time;
        /**  */
        public final byte [] bytes;

        /**
         * @param bytes
         */
        public DemoMsg( byte [] bytes )
        {
            this.time = LocalDateTime.now( ZoneOffset.UTC );
            this.bytes = bytes;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class DemoMsgsConfig
        extends DefaultTableItemsConfig<DemoMsg>
    {
        /**  */
        private final DateTimeFormatter dateFmt;
        /**  */
        private final DateTimeFormatter timeFmt;

        /**
         * 
         */
        public DemoMsgsConfig()
        {
            this.dateFmt = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
            this.timeFmt = DateTimeFormatter.ofPattern( "HHmmss.SSSSSS" );
            super.addCol( "Date", String.class,
                ( m ) -> m.time.format( dateFmt ) );
            super.addCol( "Time", String.class,
                ( m ) -> m.time.format( timeFmt ) );
            super.addCol( "Contents", String.class,
                ( m ) -> HexUtils.toHexString( m.bytes, " ", 0,
                    Math.min( 64, m.bytes.length ) ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class MessagesThread
    {
        /**  */
        private final Consumer<DemoMsg> consumer;
        /**  */
        private final ConsumerTask<DemoMsg> task;

        /**  */
        private TaskThread thread;

        /**
         * @param consumer
         */
        public MessagesThread( Consumer<DemoMsg> consumer )
        {
            this.consumer = consumer;
            this.task = new ConsumerTask<DemoMsg>( ( m, h ) -> handleMsg( m ) );
            this.thread = null;
        }

        /**
         * @param m
         */
        private void handleMsg( DemoMsg m )
        {
            consumer.accept( m );
        }

        /**
         * @param demoMsg
         */
        public void add( DemoMsg demoMsg )
        {
            task.addData( demoMsg );
        }

        /**
         * @return
         */
        public synchronized boolean isStopped()
        {
            // TODO Auto-generated method stub
            return thread == null || !thread.isFinished();
        }

        /**
         * 
         */
        public synchronized void start()
        {
            if( thread == null )
            {
                thread = new TaskThread( task, "MessagesThread" );
                thread.start();
            }
        }

        /**
         * 
         */
        public synchronized void stop()
        {
            if( thread != null )
            {
                thread.stopAndWait();
                thread = null;
            }
        }
    }
}
