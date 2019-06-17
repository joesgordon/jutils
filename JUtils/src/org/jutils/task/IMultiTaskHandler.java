package org.jutils.task;

/*******************************************************************************
 * Defines the functions that check for user control and reports status of a
 * {@link TaskPool}.
 ******************************************************************************/
public interface IMultiTaskHandler
{
    /***************************************************************************
     * Gets the next task in the queue or {@code null} if none remain. This
     * function is guaranteed to be called in a thread-safe manner.
     * @return the next task in the queue.
     **************************************************************************/
    public IStatusTask nextTask();

    /***************************************************************************
     * Returns {@code true} if processing can continue; {@code false} otherwise.
     * @return
     **************************************************************************/
    public boolean canContinue();

    /***************************************************************************
     * Creates an {@link ITaskView} and initializes the view's message field to
     * the provided task's name.
     * @return a view for displaying the provided task.
     **************************************************************************/
    public ITaskView createView( String taskName );

    /***************************************************************************
     * @param error
     **************************************************************************/
    public void signalError( TaskError error );

    /***************************************************************************
     * @param view
     **************************************************************************/
    public void removeView( ITaskView view );

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getTaskCount();

    /***************************************************************************
     * @param percent
     **************************************************************************/
    public void signalPercent( int percent );

    /***************************************************************************
     * @param message
     **************************************************************************/
    public void signalMessage( String message );
}
