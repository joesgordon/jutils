package org.jutils.task;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskUpdater
{
    /**  */
    public final ITaskStatusHandler handler;
    /**  */
    public final Number length;

    /**  */
    private Number position;
    /**  */
    private int percent;

    /***************************************************************************
     * @param handler
     * @param length
     **************************************************************************/
    public TaskUpdater( ITaskStatusHandler handler, Number length )
    {
        this.handler = handler;
        this.length = length;

        this.position = 0;
        this.percent = 0;

        handler.signalPercent( 0 );
    }

    /***************************************************************************
     * @param position
     * @return {@code true} if the percent complete changed enough to issue the
     * update; {@code false} otherwise.
     **************************************************************************/
    public boolean update( Number position )
    {
        this.position = position;
        int percent = ( int )( position.doubleValue() * 100.0 /
            length.doubleValue() );

        if( percent != this.percent )
        {
            this.percent = percent;
            return handler.signalPercent( percent );
        }

        return false;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Number getPosition()
    {
        return position;
    }
}
