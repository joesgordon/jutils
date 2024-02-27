package jutils.platform.ui;

import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import jutils.core.concurrent.ConsumerTask;
import jutils.core.concurrent.TaskThread;
import jutils.core.io.LogUtils;
import jutils.core.task.TaskError;
import jutils.core.ui.model.IView;
import jutils.platform.IPlatform;
import jutils.platform.ISerialPort;
import jutils.platform.PlatformUtils;
import jutils.platform.SerialPortTask;
import jutils.platform.SerialPortTask.ISerialHandler;
import jutils.platform.data.SerialConfig;
import jutils.platform.data.SerialMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialConsoleView implements IView<JComponent>
{
    /**  */
    private final SerialConnectionView connectionView;
    /**  */
    private final SerialPortTask serialTask;
    /**  */
    private final DisplayThread msgsDisplayThread;

    /***************************************************************************
     * 
     **************************************************************************/
    public SerialConsoleView()
    {
        Consumer<byte []> msgHandler = ( m ) -> handleBytesToSend( m );
        Consumer<SerialMessage> msgConsumer = ( m ) -> handleMsgProcessed( m );
        ConsoleFlowHandler flowHandler = new ConsoleFlowHandler( this );

        this.connectionView = new SerialConnectionView( msgHandler );
        this.serialTask = new SerialPortTask( flowHandler );
        this.msgsDisplayThread = new DisplayThread( msgConsumer );
    }

    /***************************************************************************
     * @param m
     **************************************************************************/
    private void handleBytesToSend( byte [] m )
    {
        serialTask.write( m );
    }

    /***************************************************************************
     * @param m
     **************************************************************************/
    private void handleMsgProcessed( SerialMessage m )
    {
        msgsDisplayThread.add( m );
    }

    /***************************************************************************
     * @param m
     **************************************************************************/
    private void handleMessageToDisplay( SerialMessage m )
    {
        SwingUtilities.invokeLater( () -> connectionView.addItem( m ) );
    }

    /***************************************************************************
     * @param error
     **************************************************************************/
    private static void handleError( TaskError error )
    {
        LogUtils.printError( "%s: %s", error.name, error.description );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return connectionView.getView();
    }

    /***************************************************************************
     * @param config
     * @return
     **************************************************************************/
    public boolean connect( SerialConfig config )
    {
        boolean result = false;

        // LogUtils.printDebug( "SerialConsoleView.connect()" );

        if( !msgsDisplayThread.isRunning() )
        {
            IPlatform platform = PlatformUtils.getPlatform();
            @SuppressWarnings( "resource")
            ISerialPort serialPort = platform.createSerialPort();

            if( serialPort.open( config.comPort ) )
            {
                serialPort.setConfig( config.params );
                serialPort.setReadTimeout( config.rxTimeout );

                if( serialTask.start( serialPort ) )
                {
                    result = msgsDisplayThread.start();
                }
                else
                {
                    disconnect();
                }
            }
            else
            {
                try
                {
                    serialPort.close();
                }
                catch( Exception ex )
                {
                    handleError(
                        new TaskError( "Unable to close serial port", ex ) );
                }
            }
        }

        return result;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean disconnect()
    {
        LogUtils.printDebug( "SerialConsoleView.disconnect()" );

        if( serialTask.isRunning() )
        {
            serialTask.stop();
        }

        if( msgsDisplayThread.isRunning() )
        {
            LogUtils.printDebug( "Stopping display thread" );
            msgsDisplayThread.stop();
            return true;
        }
        else
        {
            LogUtils.printDebug( "Not Running" );
        }

        return false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ConsoleFlowHandler implements ISerialHandler
    {
        /**  */
        private final SerialConsoleView view;

        /**
         * @param msgConsumer
         */
        public ConsoleFlowHandler( SerialConsoleView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void signalMessage( SerialMessage data )
        {
            view.handleMessageToDisplay( data );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void signalError( TaskError error )
        {
            SerialConsoleView.handleError( error );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class DisplayThread
    {
        /**  */
        private final Consumer<SerialMessage> displayHandler;
        /**  */
        private final ConsumerTask<SerialMessage> consumerTask;

        /**  */
        private TaskThread thread;

        /**
         * @param displayHandler
         */
        public DisplayThread( Consumer<SerialMessage> displayHandler )
        {
            this.displayHandler = displayHandler;
            this.consumerTask = new ConsumerTask<>(
                ( m, h ) -> handleMsgToDisplay( m ),
                () -> LogUtils.printDebug( "Display consumer finished" ) );

            this.thread = null;
        }

        /**
         * @param m
         */
        private void handleMsgToDisplay( SerialMessage m )
        {
            displayHandler.accept( m );
        }

        /**
         * @param msg
         */
        public void add( SerialMessage msg )
        {
            consumerTask.addData( msg );
            LogUtils.printDebug( "Consumer task is running: ", isRunning() );
        }

        /**
         * @return
         */
        public synchronized boolean isRunning()
        {
            return thread != null && thread.isRunning();
        }

        /**
         * @param config
         * @return
         */
        public synchronized boolean start()
        {
            if( thread == null )
            {
                consumerTask.startAcceptingInput();
                thread = new TaskThread( consumerTask, "DisplayThread" );
                thread.start();

                return true;
            }

            return false;
        }

        /**
         * @return
         */
        public synchronized boolean stop()
        {
            if( thread != null )
            {
                thread.stopAndWait();
                thread = null;

                return true;
            }
            return false;
        }
    }
}
