package org.jutils.core.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EventSignal
{
    /**  */
    private final AtomicBoolean signalled;
    /**  */
    private final ReentrantLock lock;
    /**  */
    private final Condition signal;

    /***************************************************************************
     * 
     **************************************************************************/
    public EventSignal()
    {
        this.signalled = new AtomicBoolean( false );
        this.lock = new ReentrantLock();
        this.signal = lock.newCondition();
    }

    /***************************************************************************
     * Waits until interrupted or signaled.
     * @return {@code true} if this event has been signaled; {@code false} if it
     * has timed out or interrupted.
     **************************************************************************/
    public boolean await()
    {
        return await( -1 );
    }

    /***************************************************************************
     * Polls, waits, or waits forever for a signal or interrupt.
     * @param nanos nanoseconds to wait; 0 to poll; negative to wait forever.
     * @return {@code true} if this event has been signaled; {@code false} if it
     * has timed out or interrupted.
     **************************************************************************/
    public boolean await( long nanos )
    {
        boolean isSignalled = this.signalled.getAndSet( false );

        if( !isSignalled && nanos != 0 )
        {
            boolean uninterruped = true;

            lock.lock();

            try
            {
                try
                {
                    if( nanos > 0 )
                    {
                        signal.awaitNanos( nanos );
                    }
                    else
                    {
                        signal.await();
                    }
                }
                catch( InterruptedException e )
                {
                    uninterruped = false;
                }
            }
            finally
            {
                lock.unlock();
            }

            isSignalled = this.signalled.getAndSet( false ) && uninterruped;
        }

        return isSignalled;
    }

    /***************************************************************************
     * Polls, waits, or waits forever for a signal or interrupt.
     * @param micros microseconds to wait; 0 to poll; negative to wait forever.
     * @return {@code true} if this event has been signaled; {@code false} if it
     * has timed out or interrupted.
     **************************************************************************/
    public boolean uawait( long micros )
    {
        return await( micros * 1000L );
    }

    /***************************************************************************
     * Polls, waits, or waits forever for a signal or interrupt.
     * @param millis milliseconds to wait; 0 to poll; negative to wait forever.
     * @return {@code true} if this event has been signaled; {@code false} if it
     * has timed out or interrupted.
     **************************************************************************/
    public boolean mawait( long millis )
    {
        return uawait( millis * 1000L );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void signal()
    {
        signalled.set( true );
        lock.lock();
        try
        {
            signal.signal();
        }
        finally
        {
            lock.unlock();
        }
    }
}
