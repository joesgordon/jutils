package org.jutils.concurrent;

/*******************************************************************************
 * Represents a task that has the ability to be stopped.
 ******************************************************************************/
public interface ITask
{
    /***************************************************************************
     * Runs the task periodically checking the provided stop manager.
     * @param handler the object that contains the execution state for this
     * task.
     **************************************************************************/
    public void run( ITaskHandler handler );
}
