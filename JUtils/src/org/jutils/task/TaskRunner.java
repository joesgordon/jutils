package org.jutils.task;

import org.jutils.concurrent.ITaskHandler;
import org.jutils.concurrent.TaskHandler;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.utils.Stopwatch;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskRunner implements Runnable
{
    /**  */
    private final IStatusTask task;
    /**  */
    private final ITaskStatusHandler handler;

    /**  */
    private TaskMetrics metrics;

    /***************************************************************************
     * @param task
     * @param view
     **************************************************************************/
    public TaskRunner( IStatusTask task, ITaskView view )
    {
        this( task, view, new TaskHandler() );
    }

    /***************************************************************************
     * @param task
     * @param view
     * @param stopManager
     **************************************************************************/
    public TaskRunner( IStatusTask task, ITaskView view, ITaskHandler stopManager )
    {
        this( task, new TaskStatusHandler( view, stopManager ) );
    }

    /***************************************************************************
     * @param task
     * @param handler
     **************************************************************************/
    public TaskRunner( IStatusTask task, ITaskStatusHandler handler )
    {
        this.task = task;
        this.handler = handler;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run()
    {
        Stopwatch watch = new Stopwatch();

        long start = watch.start();

        try
        {
            task.run( handler );
        }
        finally
        {
            long stop = watch.stop();

            metrics = new TaskMetrics( start, stop, !handler.canContinue() );

            handler.signalFinished();
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public TaskMetrics getMetrics()
    {
        return metrics;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void stop()
    {
        handler.stop();
    }

    /***************************************************************************
     * @throws InterruptedException
     **************************************************************************/
    public void stopAndWait() throws InterruptedException
    {
        handler.stopAndWaitFor();
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addFinishedListener( ItemActionListener<Boolean> l )
    {
        handler.addFinishedListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void removeFinishedListener( ItemActionListener<Boolean> l )
    {
        handler.removeFinishedListener( l );
    }
}
