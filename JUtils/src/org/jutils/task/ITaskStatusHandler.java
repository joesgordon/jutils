package org.jutils.task;

import org.jutils.concurrent.ITaskHandler;

/*******************************************************************************
 * Defines a set of common functions an {@link IStatusTask} needs to report status and
 * determine if it should stop.
 ******************************************************************************/
public interface ITaskStatusHandler extends ITaskHandler
{
    /***************************************************************************
     * Issues the provided status message to any listeners. Intended for
     * messages such as "Loading file 1 of 34: config.xml"
     * @param message the status message from the task.
     **************************************************************************/
    public void signalMessage( String message );

    /***************************************************************************
     * Issues the provided percent complete to any listeners if the provided
     * percent is different from the previous percent. A percent of -1 indicates
     * an indeterminate percent.
     * @param percent the current percent complete of the task, range (-1, 100).
     * @return {@code true} if the percent complete changed enough to issue the
     * update; {@code false} otherwise.
     **************************************************************************/
    public boolean signalPercent( int percent );

    /***************************************************************************
     * Issues the provided error to any listeners. This will normally result in
     * the task stopping prematurely.
     * @param error the error from the task.
     * @see #signalFinished()
     **************************************************************************/
    public void signalError( TaskError error );
}
