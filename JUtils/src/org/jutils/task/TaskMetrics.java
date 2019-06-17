package org.jutils.task;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskMetrics
{
    /**  */
    public final long startTime;
    /**  */
    public final long stopTime;
    /**  */
    public final boolean interrupted;

    /***************************************************************************
     * @param start
     * @param stop
     * @param interrupted
     **************************************************************************/
    public TaskMetrics( long start, long stop, boolean interrupted )
    {
        this.startTime = start;
        this.stopTime = stop;
        this.interrupted = interrupted;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getDuration()
    {
        return stopTime - startTime;
    }
}
