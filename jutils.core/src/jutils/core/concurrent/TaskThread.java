package jutils.core.concurrent;

/*******************************************************************************
 * Defines a thread for {@link ITask}s.
 ******************************************************************************/
public class TaskThread
{
    /** The {@link Runnable} that can be stopped. */
    private final Taskable runnable;
    /** The thread running the task. */
    private final Thread thread;

    /***************************************************************************
     * Creates a new thread with the provided task and name.
     * @param task the task to be executed.
     * @param name the name of the thread.
     **************************************************************************/
    public TaskThread( ITask task, String name )
    {
        this.runnable = new Taskable( task );
        this.thread = new Thread( runnable, name );
    }

    /***************************************************************************
     * Starts the thread.
     * @see Thread#start()
     **************************************************************************/
    public void start()
    {
        thread.start();
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
        runnable.stop();
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
            return runnable.stopAndWaitFor();
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
            return runnable.waitFor();
        }

        return false;
    }

    /***************************************************************************
     * Indicated if the task has been started.
     * @return {@code true} if the task has been started.
     **************************************************************************/
    public boolean isStarted()
    {
        return runnable.isStarted();
    }

    /***************************************************************************
     * Returns whether the thread is finished.
     * @return {@code true} if the task is complete, {@code false} otherwise.
     **************************************************************************/
    public boolean isFinished()
    {
        return runnable.isFinished();
    }

    /***************************************************************************
     * Indicated if the task is running.
     * @return {@code true} if the task is running.
     **************************************************************************/
    public boolean isRunning()
    {
        return runnable.isRunning();
    }
}
