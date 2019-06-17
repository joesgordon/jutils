package org.jutils.concurrent;

import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * {@link Runnable} that may be stopped synchronously or asynchronously.
 ******************************************************************************/
public class Taskable implements Runnable
{
    /** Object used to hold the continue/stop state. */
    private final ITaskHandler stopper;
    /** The task to run */
    private final ITask task;

    /***************************************************************************
     * Creates the {@link Runnable} to execute the provided task.
     * @param task the task to be executed.
     **************************************************************************/
    public Taskable( ITask task )
    {
        this( task, new TaskHandler() );
    }

    /***************************************************************************
     * Creates the {@link Runnable} to execute the provided task.
     * @param task the task to be executed.
     * @param stopManager the object that manages the stopping of this task.
     **************************************************************************/
    public Taskable( ITask task, ITaskHandler stopManager )
    {
        this.stopper = stopManager;
        this.task = task;
    }

    /***************************************************************************
     * Adds a listener to be called when the task completes; reports
     * {@code true} if the process was not stopped preemptively, {@code false}
     * otherwise.
     * @param l the listener to be added.
     **************************************************************************/
    public void addFinishedListener( ItemActionListener<Boolean> l )
    {
        stopper.addFinishedListener( l );
    }

    /***************************************************************************
     * Removes the supplied listener from the list of finished listeners.
     * @param l the listener to be removed.
     **************************************************************************/
    public void removeFinishedListener( ItemActionListener<Boolean> l )
    {
        stopper.removeFinishedListener( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void run()
    {
        try
        {
            task.run( stopper );
        }
        finally
        {
            stopper.signalFinished();
        }
    }

    /***************************************************************************
     * @see ITaskHandler#stop()
     **************************************************************************/
    public void stop()
    {
        stopper.stop();
    }

    /***************************************************************************
     * Returns whether the task is finished.
     * @return {@code true} if the task is finished, {@code false} otherwise.
     * @see ITaskHandler#isFinished()
     **************************************************************************/
    public boolean isFinished()
    {
        return stopper.isFinished();
    }

    /***************************************************************************
     * Waits for the task to complete.
     * @return {@code true} if the thread completed or {@code false} if the
     * thread waiting on the task to complete was interrupted.
     * @see ITaskHandler#waitFor()
     **************************************************************************/
    public boolean waitFor()
    {
        return stopper.waitFor();
    }

    /***************************************************************************
     * Waits for the task to complete.
     * @param milliseconds the amount of time to wait.
     * @return {@code true} if the thread completed or {@code false} if the
     * thread waiting on the task to complete was interrupted.
     * @see ITaskHandler#waitFor()
     **************************************************************************/
    public boolean waitFor( long milliseconds )
    {
        return stopper.waitFor( milliseconds );
    }

    /***************************************************************************
     * Signals the task to stop and waits for it to complete.
     * @return {@code true} if the thread completed or {@code false} if the
     * thread waiting on the task to complete was interrupted.
     * @see ITaskHandler#stopAndWaitFor()
     **************************************************************************/
    public boolean stopAndWaitFor()
    {
        return stopper.stopAndWaitFor();
    }
}
