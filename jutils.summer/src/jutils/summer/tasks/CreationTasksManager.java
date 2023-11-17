package jutils.summer.tasks;

import java.util.concurrent.atomic.AtomicInteger;

import jutils.core.task.IMultiTask;
import jutils.core.task.IStatusTask;
import jutils.summer.data.ChecksumResult;

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
