package jutils.core.concurrent;

import jutils.core.io.LogUtils;
import jutils.core.ui.event.ItemActionListener;

/*******************************************************************************
 * {@link Runnable} that may be stopped synchronously or asynchronously.
 ******************************************************************************/
public class Taskable implements Runnable
{
    /** Object used to hold the continue/stop state. */
    private final ITaskHandler handler;
    /** The task to run */
    private final ITask task;

    /**
     * Indicates that the thread has been started. First statement of
     * {@link #run()}.
     */
    private boolean started;

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
        this.handler = stopManager;
        this.task = task;
        this.started = false;
    }

    /***************************************************************************
     * Adds a listener to be called when the task completes; reports
     * {@code true} if the process was not stopped preemptively, {@code false}
     * otherwise.
     * @param l the listener to be added.
     **************************************************************************/
    public void addFinishedListener( ItemActionListener<Boolean> l )
    {
        handler.addFinishedListener( l );
    }

    /***************************************************************************
     * Removes the supplied listener from the list of finished listeners.
     * @param l the listener to be removed.
     **************************************************************************/
    public void removeFinishedListener( ItemActionListener<Boolean> l )
    {
        handler.removeFinishedListener( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void run()
    {
        LogUtils.printDebug( "Taskable.run() Started" );
        this.started = true;
        try
        {
            task.run( handler );
        }
        finally
        {
            handler.signalFinished();
        }
        LogUtils.printDebug( "Taskable.run() Finished" );
    }

    /***************************************************************************
     * @see ITaskHandler#stop()
     **************************************************************************/
    public void stop()
    {
        handler.stop();
    }

    /***************************************************************************
     * Indicated if the task has been started.
     * @return {@code true} if the task has been started.
     **************************************************************************/
    public boolean isStarted()
    {
        return started;
    }

    /***************************************************************************
     * Returns whether the task is finished.
     * @return {@code true} if the task is finished, {@code false} otherwise.
     * @see ITaskHandler#isFinished()
     **************************************************************************/
    public boolean isFinished()
    {
        return handler.isFinished();
    }

    /***************************************************************************
     * Indicated if the task is running.
     * @return {@code true} if the task is running.
     **************************************************************************/
    public boolean isRunning()
    {
        return isStarted() && !isFinished();
    }

    /***************************************************************************
     * Waits for the task to complete.
     * @return {@code true} if the thread completed or {@code false} if the
     * thread waiting on the task to complete was interrupted.
     * @see ITaskHandler#waitFor()
     **************************************************************************/
    public boolean waitFor()
    {
        return handler.waitFor();
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
        return handler.waitFor( milliseconds );
    }

    /***************************************************************************
     * Signals the task to stop and waits for it to complete.
     * @return {@code true} if the thread completed or {@code false} if the
     * thread waiting on the task to complete was interrupted.
     * @see ITaskHandler#stopAndWaitFor()
     **************************************************************************/
    public boolean stopAndWaitFor()
    {
        return handler.stopAndWaitFor();
    }
}
