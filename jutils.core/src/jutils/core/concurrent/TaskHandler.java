package jutils.core.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import jutils.core.ui.event.ItemActionList;
import jutils.core.ui.event.ItemActionListener;

/*******************************************************************************
 * This class is a sort of semaphore that represents the execution state
 * (executing or stopped) and the methods to modify said state.
 ******************************************************************************/
public final class TaskHandler implements ITaskHandler
{
    /**
     * Execution continues as long as {@code continueRunning} is {@code true}.
     */
    private AtomicBoolean continueRunning;
    /**
     * {@code true} after {@link #signalFinished()} called; {@code false}
     * otherwise.
     */
    private AtomicBoolean isFinished;
    /** Lock used to protect the finished flag and the stop condition. */
    private final ReentrantLock stopLock;
    /** Condition used to signal that execution is complete. */
    private final Condition stopCondition;
    /**
     * List of listeners to be called when {@link #signalFinished()} is called.
     */
    private final ItemActionList<Boolean> finishedListeners;

    /***************************************************************************
     * Creates a new object.
     **************************************************************************/
    public TaskHandler()
    {
        this.continueRunning = new AtomicBoolean( true );
        this.isFinished = new AtomicBoolean( false );
        this.stopLock = new ReentrantLock();
        this.stopCondition = stopLock.newCondition();
        this.finishedListeners = new ItemActionList<Boolean>();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addFinishedListener( ItemActionListener<Boolean> l )
    {
        finishedListeners.addListener( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean canContinue()
    {
        return continueRunning.get();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isFinished()
    {
        return isFinished.get();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeFinishedListener( ItemActionListener<Boolean> l )
    {
        finishedListeners.removeListener( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void signalFinished()
    {
        try
        {
            stopLock.lock();
            isFinished.set( true );
            stopCondition.signalAll();
        }
        finally
        {
            stopLock.unlock();
        }

        finishedListeners.fireListeners( this, continueRunning.get() );

        finishedListeners.removeAll();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void stop()
    {
        continueRunning.set( false );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean stopAndWaitFor()
    {
        stop();
        return waitFor();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean waitFor()
    {
        boolean stopped = true;

        stopLock.lock();

        try
        {
            while( !isFinished.get() )
            {
                try
                {
                    stopCondition.await();
                }
                catch( InterruptedException ex )
                {
                    stopped = false;
                    break;
                }
            }
        }
        finally
        {
            stopLock.unlock();
        }

        return stopped;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean waitFor( long milliseconds )
    {
        boolean stopped = true;

        stopLock.lock();

        try
        {
            try
            {
                stopCondition.await( milliseconds, TimeUnit.MILLISECONDS );
            }
            catch( InterruptedException ex )
            {
                stopped = false;
            }
        }
        finally
        {
            stopLock.unlock();
        }

        return stopped;
    }
}
