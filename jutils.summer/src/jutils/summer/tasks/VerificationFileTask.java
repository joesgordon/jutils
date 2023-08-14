package jutils.summer.tasks;

import java.util.List;

import jutils.core.io.cksum.ChecksumType;
import jutils.core.task.IStatusTask;
import jutils.core.task.ITaskStatusHandler;
import jutils.summer.data.InvalidChecksum;
import jutils.summer.data.SumFile;

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
