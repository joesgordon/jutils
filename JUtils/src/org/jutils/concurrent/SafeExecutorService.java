package org.jutils.concurrent;

import java.util.concurrent.*;

/*******************************************************************************
 * Defines a thread pool that reports errors that occur.
 ******************************************************************************/
public class SafeExecutorService extends ThreadPoolExecutor
{
    /** The callbacks to run upon completion of the task. */
    private final IFinishedHandler finishedHandler;

    /***************************************************************************
     * Creates a new thread pool with the provided number of threads and
     * finished handler.
     * @param numThreads the number of threads to use in the thread pool.
     * @param finishedHandler the callbacks to run upon completion of the task.
     **************************************************************************/
    public SafeExecutorService( int numThreads,
        IFinishedHandler finishedHandler )
    {
        super( numThreads, // core threads
            numThreads, // max threads
            1, // timeout
            TimeUnit.MINUTES, // timeout units
            new LinkedBlockingQueue<Runnable>() // work queue
        );

        this.finishedHandler = finishedHandler;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    protected void afterExecute( Runnable r, Throwable t )
    {
        super.afterExecute( r, t );

        if( t == null && r instanceof Future<?> )
        {
            try
            {
                Future<?> future = ( Future<?> )r;
                if( future.isDone() )
                {
                    future.get();
                }

                finishedHandler.signalComplete();
            }
            catch( CancellationException ce )
            {
                t = ce;
            }
            catch( ExecutionException ee )
            {
                t = ee.getCause();
            }
            catch( InterruptedException ie )
            {
                Thread.currentThread().interrupt(); // ignore/reset
            }
        }

        if( t != null )
        {
            finishedHandler.signalError( t );
        }
    }
}
