package jutils.math;

/*******************************************************************************
 *
 ******************************************************************************/
public class StatsCalc
{
    /**  */
    private int count;
    /**  */
    private double oldMean;
    /**  */
    private double newMean;
    /**  */
    private double oldDeviation;
    /**  */
    private double newDeviation;
    /**  */
    private double min;
    /**  */
    private double max;

    /***************************************************************************
     * 
     **************************************************************************/
    public StatsCalc()
    {
        this.count = 0;
        this.oldMean = 0.0;
        this.newMean = 0.0;
        this.oldDeviation = 0.0;
        this.newDeviation = 0.0;
        this.min = Double.MAX_VALUE;
        this.max = Double.MIN_VALUE;
    }

    /***************************************************************************
     * @param x
     **************************************************************************/
    public void account( double x )
    {
        count++;

        min = Math.min( x, min );
        max = Math.max( x, max );

        // See Knuth TAOCP vol 2, 3rd edition, page 232
        if( count == 1 )
        {
            oldMean = newMean = x;
            oldDeviation = 0.0;
        }
        else
        {
            newMean = oldMean + ( x - oldMean ) / count;
            newDeviation = oldDeviation + ( x - oldMean ) * ( x - newMean );

            // set up for next iteration
            oldMean = newMean;
            oldDeviation = newDeviation;
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getMin()
    {
        return min;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getMax()
    {
        return max;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double calcMean()
    {
        return ( count > 0 ) ? newMean : 0.0;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double calcVariance()
    {
        return ( ( count > 1 ) ? newDeviation / ( count - 1 ) : 0.0 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double calcStandardDeviation()
    {
        return Math.sqrt( calcVariance() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Stats calcStats()
    {
        return new Stats( min, max, calcMean(), calcVariance() );
    }
}
