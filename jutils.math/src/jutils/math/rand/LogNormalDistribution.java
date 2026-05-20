package jutils.math.rand;

import java.util.Random;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LogNormalDistribution implements IDistribution
{
    /**  */
    private final Random rand;

    /***************************************************************************
     * 
     **************************************************************************/
    public LogNormalDistribution()
    {
        this.rand = new Random();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public double next( double mean, double stddev )
    {
        return ( rand.nextDouble() - 0.5 ) * 2 * stddev + mean;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public double getProbability( double mean, double stddev, double value )
    {
        double p = Math.exp(
            -( Math.pow( value - mean, 2 ) ) / ( 2 * Math.pow( stddev, 2 ) ) ) /
            ( stddev * Math.sqrt( 2 * Math.PI ) );

        return p;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public DistributionType getType()
    {
        return DistributionType.LOG_NORMAL;
    }
}
