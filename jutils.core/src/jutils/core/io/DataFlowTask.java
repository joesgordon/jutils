package jutils.core.io;

import java.io.IOException;

import jutils.core.Utils;
import jutils.core.concurrent.ConsumerTask;
import jutils.core.concurrent.ITask;
import jutils.core.concurrent.ITaskHandler;
import jutils.core.concurrent.TaskThread;
import jutils.core.task.TaskError;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataFlowTask
{
    /**  */
    private final IFlowHandler handler;
    /**  */
    private final ConsumerTask<byte []> writeTask;

    /**  */
    private IDataFlow flow;
    /**  */
    private TaskThread writeThread;
    /**  */
    private TaskThread readThread;

    /***************************************************************************
     * @param handler
     **************************************************************************/
    public DataFlowTask( IFlowHandler handler )
    {
        this.handler = handler;
        this.writeTask = new ConsumerTask<byte []>(
            ( d, h ) -> handleWrite( d ) );
    }

    /***************************************************************************
     * @param data
     * @param handler
     **************************************************************************/
    private void handleWrite( byte [] data )
    {
        try
        {
            flow.write( data );
            this.handler.signalWritten( data );
        }
        catch( IOException ex )
        {
            this.handler.signalError( new TaskError( "Write Error", ex ) );
        }
    }

    /***************************************************************************
     * @param h
     **************************************************************************/
    private void handleReceiveRun( ITaskHandler h )
    {
        byte [] buffer = new byte[IOUtils.DEFAULT_BUF_SIZE];

        while( h.canContinue() )
        {
            try
            {
                int length = flow.read( buffer );

                if( length > 0 )
                {
                    byte [] msg = new byte[length];

                    Utils.byteArrayCopy( buffer, 0, msg, 0, length );

                    this.handler.signalRead( msg );
                }
            }
            catch( IOException ex )
            {
                this.handler.signalError( new TaskError( "Read Error", ex ) );
            }
        }
    }

    /***************************************************************************
     * @param flow
     * @return
     **************************************************************************/
    public boolean start( IDataFlow flow )
    {
        if( this.flow == null )
        {
            ITask readTask = ( h ) -> handleReceiveRun( h );

            this.flow = flow;
            this.readThread = new TaskThread( readTask, "DataFlowReadTask" );
            this.writeThread = new TaskThread( writeTask, "DataFlowWriteTask" );

            this.readThread.start();
            this.writeThread.start();

            return true;
        }

        return false;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean stop()
    {
        boolean result = false;

        if( flow != null )
        {
            this.readThread.stop();
            this.writeThread.stop();

            this.readThread.waitFor();
            this.writeThread.waitFor();

            try
            {
                flow.close();

                this.flow = null;

                result = true;
            }
            catch( Exception ex )
            {
                handler.signalError( new TaskError( "Unable to close", ex ) );
            }
        }

        return result;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isRunning()
    {
        return flow != null;
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void write( byte [] data )
    {
        writeTask.addData( data );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IDataFlow getDataFlow()
    {
        return flow;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IFlowHandler
    {
        /**
         * @param data
         */
        public void signalWritten( byte [] data );

        /**
         * @param data
         * @param length
         */
        public void signalRead( byte [] data );

        /**
         * @param error
         */
        public void signalError( TaskError error );
    }
}
