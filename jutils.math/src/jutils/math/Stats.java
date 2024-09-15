package jutils.math;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Stats
{
    /**  */
    public final double min;
    /**  */
    public final double max;
    /**  */
    public final double mean;
    /**  */
    public final double variance;

    /***************************************************************************
     * @param min
     * @param max
     * @param mean
     * @param variance
     **************************************************************************/
    public Stats( double min, double max, double mean, double variance )
    {
        this.min = min;
        this.max = max;
        this.mean = mean;
        this.variance = variance;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getRange()
    {
        return max - min;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getStddev()
    {
        return Math.sqrt( variance );
    }
}
