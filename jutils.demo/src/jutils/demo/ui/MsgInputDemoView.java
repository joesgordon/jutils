package jutils.demo.ui;

import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import jutils.core.concurrent.ConsumerTask;
import jutils.core.concurrent.TaskThread;
import jutils.core.ui.model.IView;
import jutils.platform.data.SerialMessage;
import jutils.platform.ui.SerialConnectionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MsgInputDemoView implements IView<JComponent>
{
    /**  */
    private final JComponent view;

    /**  */
    private final SerialConnectionView consoleView;

    /**  */
    private final MessagesThread thread;

    /***************************************************************************
     * 
     **************************************************************************/
    public MsgInputDemoView()
    {
        this.consoleView = new SerialConnectionView(
            ( m ) -> handleInputMsg( m ) );

        Consumer<SerialMessage> msgConsumer = (
            m ) -> SwingUtilities.invokeLater( () -> consoleView.addItem( m ) );
        this.thread = new MessagesThread( msgConsumer );

        this.view = consoleView.getView();

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
     * @param message
     **************************************************************************/
    private void handleInputMsg( byte [] message )
    {
        if( thread.isStopped() )
        {
            thread.start();
        }

        thread.add( new SerialMessage( true, message ) );
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
    private static final class MessagesThread
    {
        /**  */
        private final Consumer<SerialMessage> consumer;
        /**  */
        private final ConsumerTask<SerialMessage> task;

        /**  */
        private TaskThread thread;

        /**
         * @param consumer
         */
        public MessagesThread( Consumer<SerialMessage> consumer )
        {
            this.consumer = consumer;
            this.task = new ConsumerTask<SerialMessage>(
                ( m, h ) -> handleMsg( m ) );
            this.thread = null;
        }

        /**
         * @param m
         */
        private void handleMsg( SerialMessage m )
        {
            consumer.accept( m );
        }

        /**
         * @param msg
         */
        public void add( SerialMessage msg )
        {
            task.addData( msg );
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
