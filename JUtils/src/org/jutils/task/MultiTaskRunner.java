package org.jutils.task;

import java.awt.event.ActionListener;

import org.jutils.ui.event.ActionListenerList;
import org.jutils.utils.Stopwatch;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MultiTaskRunner implements Runnable
{
    private final IMultiTask tasker;
    /**  */
    private final MultiTaskHandler handler;
    /**  */
    private final TaskPool pool;

    /**  */
    private final ActionListenerList finishedListeners;

    /**  */
    private TaskMetrics metrics;

    /***************************************************************************
     * @param tasker
     * @param view
     * @param numThreads
     **************************************************************************/
    public MultiTaskRunner( IMultiTask tasker, IMultiTaskView view,
        int numThreads )
    {
        this.tasker = tasker;
        this.handler = new MultiTaskHandler( tasker, view );
        this.pool = new TaskPool( handler, numThreads );
        this.finishedListeners = new ActionListenerList();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void run()
    {
        long start;
        long stop;
        Stopwatch watch = new Stopwatch();

        start = watch.start();
        tasker.startup();
        pool.start();
        tasker.shutdown();
        stop = watch.stop();

        metrics = new TaskMetrics( start, stop,
            !handler.canContinue() || handler.error != null );

        finishedListeners.fireListeners( this, 0, null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void stop()
    {
        pool.shutdown();
        handler.stop();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public TaskMetrics getMetrics()
    {
        return metrics;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addFinishedListener( ActionListener l )
    {
        finishedListeners.addListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public TaskError getError()
    {
        return handler.error;
    }
}
