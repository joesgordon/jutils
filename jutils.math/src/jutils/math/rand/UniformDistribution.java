package jutils.math.rand;

import java.util.Random;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UniformDistribution implements IDistribution
{
    /**  */
    private final Random rand;

    /***************************************************************************
     * 
     **************************************************************************/
    public UniformDistribution()
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
        double p = 0;

        if( value >= ( mean - stddev ) && value <= ( mean + stddev ) )
        {
            p = 1 / ( 2 * stddev );
        }

        return p;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public DistributionType getType()
    {
        return DistributionType.UNIFORM;
    }
}
