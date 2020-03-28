package org.jutils.apps.summer.tasks;

import java.util.concurrent.atomic.AtomicInteger;

import org.jutils.apps.summer.data.ChecksumResult;
import org.jutils.core.task.IMultiTask;
import org.jutils.core.task.IStatusTask;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CreationTasksManager implements IMultiTask
{
    /**  */
    private final ChecksumResult input;
    /**  */
    private AtomicInteger index;

    /***************************************************************************
     * @param input
     **************************************************************************/
    public CreationTasksManager( ChecksumResult input )
    {
        this.input = input;

        this.index = new AtomicInteger( 0 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IStatusTask nextTask()
    {
        IStatusTask task = null;
        int index = this.index.getAndIncrement();

        if( index < input.files.size() )
        {
            task = new CreationFileTask( input.files.get( index ), input.type );
        }

        return task;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getTaskCount()
    {
        return input.files.size();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getTaskAction()
    {
        return "Generating checksum for";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void startup()
    {
        // LogUtils.printDebug( "Starting up" );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void shutdown()
    {
        // LogUtils.printDebug( "Shutting down" );
    }
}
