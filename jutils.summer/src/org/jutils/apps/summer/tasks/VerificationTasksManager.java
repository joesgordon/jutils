package org.jutils.apps.summer.tasks;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.jutils.apps.summer.data.ChecksumResult;
import org.jutils.apps.summer.data.InvalidChecksum;
import org.jutils.core.task.IMultiTask;
import org.jutils.core.task.IStatusTask;

/*******************************************************************************
 * 
 ******************************************************************************/
public class VerificationTasksManager implements IMultiTask
{
    /**  */
    private final ChecksumResult input;
    /**  */
    private final List<InvalidChecksum> invalidSums;
    /**  */
    private AtomicInteger index;

    /***************************************************************************
     * @param input
     * @param invalidSums
     **************************************************************************/
    public VerificationTasksManager( ChecksumResult input,
        List<InvalidChecksum> invalidSums )
    {
        this.input = input;
        this.invalidSums = invalidSums;

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
            task = new VerificationFileTask( invalidSums,
                input.files.get( index ), input.type );
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
        return "Verifying checksum for";
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
