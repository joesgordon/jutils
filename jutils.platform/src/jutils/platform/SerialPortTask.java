package jutils.platform;

import jutils.core.io.DataFlowTask;
import jutils.core.io.DataFlowTask.IFlowHandler;
import jutils.core.task.TaskError;
import jutils.platform.data.SerialMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialPortTask
{
    /**  */
    private final DataFlowTask flowTask;

    /***************************************************************************
     * @param handler
     **************************************************************************/
    public SerialPortTask( ISerialHandler handler )
    {
        this.flowTask = new DataFlowTask( new SerialFlowHandler( handler ) );
    }

    /***************************************************************************
     * @param serialPort
     * @return
     **************************************************************************/
    public boolean start( ISerialPort serialPort )
    {
        serialPort.setReadTimeout( 500 );

        return flowTask.start( serialPort );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean stop()
    {
        return flowTask.stop();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return flowTask.isRunning();
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void write( byte [] data )
    {
        flowTask.write( data );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ISerialPort getSerialPort()
    {
        return ( ISerialPort )flowTask.getDataFlow();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface ISerialHandler
    {
        /**
         * @param data
         */
        public void signalMessage( SerialMessage data );

        /**
         * @param error
         */
        public void signalError( TaskError error );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SerialFlowHandler implements IFlowHandler
    {
        /**  */
        private final ISerialHandler handler;

        /**
         * @param handler
         */
        public SerialFlowHandler( ISerialHandler handler )
        {
            this.handler = handler;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void signalWritten( byte [] data )
        {
            handler.signalMessage( new SerialMessage( true, data ) );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void signalRead( byte [] data )
        {
            if( data.length > 0 )
            {
                handler.signalMessage( new SerialMessage( false, data ) );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void signalError( TaskError error )
        {
            handler.signalError( error );
        }
    }
}
