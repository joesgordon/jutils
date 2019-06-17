package org.jutils.concurrent;

/*******************************************************************************
 * Defines the callback methods to be invoked at the completion of a task.
 ******************************************************************************/
public interface IFinishedHandler
{
    /***************************************************************************
     * Signals that the task has experienced an irrecoverable error.
     * @param t the error that occurred.
     **************************************************************************/
    public void signalError( Throwable t );

    /***************************************************************************
     * Signals that the task has completed with no errors.
     **************************************************************************/
    public void signalComplete();
}
