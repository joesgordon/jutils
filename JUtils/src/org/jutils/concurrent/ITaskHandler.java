package org.jutils.concurrent;

import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * Represents the execution state (executing or stopped) and the methods to
 * modify said state.
 ******************************************************************************/
public interface ITaskHandler
{
    /***************************************************************************
     * Indicates that the task should continue processing or that it has been
     * requested that it cease.
     * @return {@code true} if the task can continue, {@code false} otherwise.
     **************************************************************************/
    public boolean canContinue();

    /***************************************************************************
     * Returns whether the task has finished.
     * @return {@code true} if the task is finished, {@code false} otherwise
     **************************************************************************/
    public boolean isFinished();

    /***************************************************************************
     * Invoked to indicate the task has finished processing and is about to
     * return.
     **************************************************************************/
    public void signalFinished();

    /***************************************************************************
     * Asynchronously requests that the task stop and returns immediately.
     **************************************************************************/
    public void stop();

    /***************************************************************************
     * Stops the task and waits for {@link #signalFinished()} to be invoked.
     * Calls {@link #stop()} and then {@link #waitFor()}. Recommend checking
     * {@link #isFinished()} if {@code false} is returned.
     * @return {@code true} if the thread completed or {@code false} if the
     * thread waiting on the task to complete was interrupted.
     **************************************************************************/
    public boolean stopAndWaitFor();

    /***************************************************************************
     * Waits for {@link #signalFinished()} to be invoked. Recommend checking
     * {@link #isFinished()} if {@code false} is returned.
     * @return {@code true} if the thread completed or {@code false} if the
     * thread waiting on the task to complete was interrupted.
     **************************************************************************/
    public boolean waitFor();

    /***************************************************************************
     * Waits for {@link #signalFinished()} to be invoked for the specified time.
     * Recommend checking {@link #isFinished()} if {@code false} is returned.
     * @param milliseconds the amount of time to wait or indefinitely if < 0.
     * @return {@code true} if the thread completed or {@code false} if the
     * thread waiting on the task to complete was interrupted.
     **************************************************************************/
    public boolean waitFor( long milliseconds );

    /***************************************************************************
     * Adds the provided listener to a list that is notified when the task
     * completes. The boolean contained in the event is {@code true} when the
     * task completes normally and {@code false} when it has been interrupted or
     * encounters an error severe enough to cease further processing.
     * @param l the listener to be called when the task completes.
     **************************************************************************/
    public void addFinishedListener( ItemActionListener<Boolean> l );

    /***************************************************************************
     * Removes the supplied listener from the list of finished listeners.
     * @param l the listener to be removed.
     **************************************************************************/
    public void removeFinishedListener( ItemActionListener<Boolean> l );
}
