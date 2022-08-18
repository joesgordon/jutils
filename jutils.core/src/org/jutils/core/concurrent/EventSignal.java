package org.jutils.core.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class EventSignal
{
    private final AtomicBoolean signalled;
    private final ReentrantLock lock;
    private final Condition signal;

    public EventSignal()
    {
        this.signalled = new AtomicBoolean( false );
        this.lock = new ReentrantLock();
        this.signal = lock.newCondition();
    }

    public boolean await()
    {
        boolean uninterruped = true;
        boolean isSignalled = this.signalled.getAndSet( false );

        if( !isSignalled )
        {
            lock.lock();
            try
            {
                try
                {
                    signal.await();
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
        }

        return uninterruped;
    }

    public boolean await( long nanos )
    {
        boolean uninterruped = true;
        boolean isSignalled = this.signalled.getAndSet( false );

        if( !isSignalled )
        {
            lock.lock();
            try
            {
                try
                {
                    signal.awaitNanos( nanos );
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
        }

        return uninterruped;
    }

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
