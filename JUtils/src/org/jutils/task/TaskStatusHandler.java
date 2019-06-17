package org.jutils.task;

import org.jutils.concurrent.ITaskHandler;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskStatusHandler implements ITaskStatusHandler
{
    /**  */
    private final ITaskView view;
    /**  */
    final ITaskHandler stopManager;

    /***************************************************************************
     * @param view
     * @param stopManager
     **************************************************************************/
    public TaskStatusHandler( ITaskView view, ITaskHandler stopManager )
    {
        this.view = view;
        this.stopManager = stopManager;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean canContinue()
    {
        return stopManager.canContinue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalMessage( String message )
    {
        view.signalMessage( message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean signalPercent( int percent )
    {
        return view.signalPercent( percent );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalError( TaskError error )
    {
        view.signalError( error );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalFinished()
    {
        stopManager.signalFinished();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void stop()
    {
        stopManager.stop();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean stopAndWaitFor()
    {
        return stopManager.stopAndWaitFor();
    }

    @Override
    public boolean isFinished()
    {
        return stopManager.isFinished();
    }

    @Override
    public boolean waitFor()
    {
        return stopManager.waitFor();
    }

    @Override
    public boolean waitFor( long milliseconds )
    {
        return stopManager.waitFor( milliseconds );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addFinishedListener( ItemActionListener<Boolean> l )
    {
        stopManager.addFinishedListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeFinishedListener( ItemActionListener<Boolean> l )
    {
        stopManager.removeFinishedListener( l );
    }
}
