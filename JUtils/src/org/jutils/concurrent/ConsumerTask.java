package org.jutils.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/*******************************************************************************
 * Used to process data that is collected by a separate thread. When this task
 * has data AND has a chance to process said data, it will call the sub-class's
 * {@link #addData(Object)} function.
 * @param <T> the type of items to be consumed.
 ******************************************************************************/
public class ConsumerTask<T> implements ITask
{
    /** {@code true} if input should be accepted, {@code false} otherwise. */
    private final AtomicBoolean acceptInput;
    /** List of data to be consumed. */
    private final LinkedBlockingQueue<T> data;
    /** The callback to serially consume items. */
    private final IConsumer<T> consumer;
    /** The callback to run after the task is complete. */
    private final Runnable finalizer;

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
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final void run( ITaskHandler handler )
    {
        T obj = null;

        while( handler.canContinue() )
        {
            if( acceptInput.get() )
            {
                try
                {
                    obj = data.take();
                }
                catch( InterruptedException e )
                {
                    obj = data.poll();
                }
            }
            else
            {
                obj = data.poll();
            }

            if( obj != null )
            {
                consumer.consume( obj, handler );
                obj = null;
            }
            else
            {
                break;
            }
        }

        stopAcceptingInput();

        if( finalizer != null )
        {
            finalizer.run();
        }

        handler.signalFinished();
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
     * Flags the task to stop accepting input, but the user must ensure that the
     * Thread running this task is interrupted.
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
