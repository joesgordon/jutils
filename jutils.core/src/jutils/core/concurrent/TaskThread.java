package jutils.core.concurrent;

import jutils.core.io.LogUtils;

/*******************************************************************************
 * Defines a thread for {@link ITask}s. A {@link TaskThread} can be started only
 * once just like a {@link Thread}.
 ******************************************************************************/
public class TaskThread
{
    /**  */
    private final ITask task;
    /**  */
    private final String name;
    /** The {@link Runnable} that can be stopped. */
    private Taskable taskable;
    /** The thread running the task. */
    private Thread thread;

    /***************************************************************************
     * Creates a new thread with the provided task and name.
     * @param task the task to be executed.
     * @param name the name of the thread.
     **************************************************************************/
    public TaskThread( ITask task, String name )
    {
        this.task = task;
        this.name = name;

        this.taskable = new Taskable( task );
        this.thread = new Thread( taskable, name );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        this.taskable = new Taskable( task );
        this.thread = new Thread( taskable, name );
    }

    /***************************************************************************
     * Starts the thread by scheduling it to be run. The thread will likely not
     * be started upon return from this function.
     * @see Thread#start()
     **************************************************************************/
    public void start()
    {
        LogUtils.printDebug( "TaskThread.start() Started" );
        if( isFinished() )
        {
            LogUtils.printDebug( "resetting" );
            reset();
        }

        thread.start();
        LogUtils.printDebug( "TaskThread.start() Finished" );
    }

    /***************************************************************************
     * Interrupts this thread.
     * @see Thread#interrupt()
     **************************************************************************/
    public void interrupt()
    {
        thread.interrupt();
    }

    /***************************************************************************
     * Signals this thread to stop as soon as possible.
     **************************************************************************/
    public void stop()
    {
        taskable.stop();
    }

    /***************************************************************************
     * Signals this thread to stop and waits for it to complete. Recommend
     * checking {@link #isFinished()} if {@code false} is returned.
     * @return {@code true} if the thread completed or {@code false} if the
     * thread waiting on the task to complete was interrupted.
     **************************************************************************/
    public boolean stopAndWait()
    {
        if( isStarted() )
        {
            return taskable.stopAndWaitFor();
        }

        return false;
    }

    /***************************************************************************
     * Waits for the task to complete. Recommend checking {@link #isFinished()}
     * if {@code false} is returned.
     * @return {@code true} if the thread completed or {@code false} if the
     * thread waiting on the task to complete was interrupted.
     **************************************************************************/
    public boolean waitFor()
    {
        if( isStarted() )
        {
            LogUtils.printDebug( "waiting for %s", thread.getName() );
            return taskable.waitFor();
        }

        return false;
    }

    /***************************************************************************
     * Indicated if the task has been started.
     * @return {@code true} if the task has been started.
     **************************************************************************/
    public boolean isStarted()
    {
        return taskable.isStarted();
    }

    /***************************************************************************
     * Returns whether the thread is finished.
     * @return {@code true} if the task is complete, {@code false} otherwise.
     **************************************************************************/
    public boolean isFinished()
    {
        return taskable.isFinished();
    }

    /***************************************************************************
     * Indicated if the task is running.
     * @return {@code true} if the task is running.
     **************************************************************************/
    public boolean isRunning()
    {
        return taskable.isRunning();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getName()
    {
        return thread.getName();
    }

    /***************************************************************************
     * @param on
     **************************************************************************/
    public void setDaemon( boolean on )
    {
        thread.setDaemon( on );
    }
}
