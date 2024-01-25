package jutils.core.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import jutils.core.time.NanoWatch;

/*******************************************************************************
 * Used to process data that is collected by a separate thread. When this task
 * has data AND has a chance to process said data, it will call the sub-class's
 * {@link #addData(Object)} function.
 * @param <T> the type of items to be consumed.
 ******************************************************************************/
public class ConsumerTask<T> implements ITask
{
    /** The callback to serially consume items. */
    private final IConsumer<T> consumer;
    /** The callback to run after the task is complete. */
    private final Runnable finalizer;

    /** {@code true} if input should be accepted, {@code false} otherwise. */
    private final AtomicBoolean acceptInput;
    /** List of data to be consumed. */
    private final LinkedBlockingQueue<T> data;
    /**  */
    private final EventSignal dataReady;

    /***************************************************************************
     * Creates a new consumer task with no finished callback.
     * @param consumer called when there is data to be processed.
     **************************************************************************/
    public ConsumerTask( IConsumer<T> consumer )
    {
        this( consumer, null );
    }

    /***************************************************************************
     * Creates a new consumer task with the provided finished callback.
     * @param consumer called when there is data to be processed.
     * @param finalizer called when processing is complete.
     **************************************************************************/
    public ConsumerTask( IConsumer<T> consumer, Runnable finalizer )
    {
        this.consumer = consumer;
        this.finalizer = finalizer;

        this.acceptInput = new AtomicBoolean( true );
        this.data = new LinkedBlockingQueue<T>();
        this.dataReady = new EventSignal();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final void run( ITaskHandler handler )
    {
        // LogUtils.printDebug( "Starting consumer task %s",
        // Thread.currentThread().getName() );

        List<T> items = new ArrayList<>();
        NanoWatch watch = new NanoWatch();

        // acceptInput.set( true );

        while( handler.canContinue() )
        {
            int size = data.size();

            if( size < 1 )
            {
                watch.start();
                dataReady.mawait( 400 );
                watch.stop();
                size = data.size();
            }

            if( size > 0 )
            {
                items.addAll( data );
                data.clear();

                // LogUtils.printDebug( "Waited %d",
                // watch.getElapsed() / 1000000 );

                watch.start();
                for( T item : items )
                {
                    consumer.consume( item, handler );
                }
                items.clear();
                watch.stop();

                // LogUtils.printDebug( "Sent %d message in %d", size,
                // watch.getElapsed() / 1000000 );

                watch.reset();
            }
        }

        // LogUtils.printDebug( "Stopping consumer task %s",
        // Thread.currentThread().getName() );

        stopAcceptingInput();

        if( finalizer != null )
        {
            finalizer.run();
        }

        handler.signalFinished();

        // LogUtils.printDebug( "Stopped consumer task %s",
        // Thread.currentThread().getName() );
    }

    /***************************************************************************
     * @return {@code true} if the task is accepting input, {@code false}
     * otherwise.
     **************************************************************************/
    public boolean isAcceptingInput()
    {
        return acceptInput.get();
    }

    /***************************************************************************
     * Flags the task to accept input
     **************************************************************************/
    public void startAcceptingInput()
    {
        this.acceptInput.set( true );
    }

    /***************************************************************************
     * Flags the task to stop accepting input.
     **************************************************************************/
    public void stopAcceptingInput()
    {
        this.acceptInput.set( false );
    }

    /***************************************************************************
     * Adds data to this Thread to be processed when the thread is able.
     * @param obj The data to be processed.
     * @throws NullPointerException if the specified element is null.
     **************************************************************************/
    public final void addData( T obj ) throws NullPointerException
    {
        if( acceptInput.get() )
        {
            try
            {
                data.put( obj );
                dataReady.signal();
            }
            catch( InterruptedException e )
            {
            }
        }
    }

    /***************************************************************************
     * @return the number of elements that have not been processed.
     **************************************************************************/
    public int getNumItemsLeft()
    {
        return data.size();
    }
}
