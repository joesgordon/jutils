package org.jutils.apps.summer.tasks;

import java.util.List;

import org.jutils.apps.summer.data.InvalidChecksum;
import org.jutils.apps.summer.data.SumFile;
import org.jutils.core.io.cksum.ChecksumType;
import org.jutils.core.task.IStatusTask;
import org.jutils.core.task.ITaskStatusHandler;

/*******************************************************************************
 * 
 ******************************************************************************/
public class VerificationFileTask implements IStatusTask
{
    /**  */
    private final List<InvalidChecksum> invalidSums;
    /**  */
    private final SumFile inputSum;
    /**  */
    private final SumFile calcSum;
    /**  */
    private final CreationFileTask creationTask;

    /***************************************************************************
     * @param invalidSums
     * @param inputSum
     * @param type
     **************************************************************************/
    public VerificationFileTask( List<InvalidChecksum> invalidSums,
        SumFile inputSum, ChecksumType type )
    {
        this.invalidSums = invalidSums;
        this.inputSum = inputSum;
        this.calcSum = new SumFile( inputSum );
        this.creationTask = new CreationFileTask( calcSum, type );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskStatusHandler handler )
    {
        creationTask.run( handler );

        if( !inputSum.checksum.equalsIgnoreCase( calcSum.checksum ) )
        {
            synchronized( invalidSums )
            {
                invalidSums.add( new InvalidChecksum( inputSum, calcSum ) );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return inputSum.path;
    }
}
