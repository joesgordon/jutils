package jutils.math.rand;

import java.util.Random;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RayleighDistribution implements IDistribution
{
    /**  */
    private final Random rand;

    /***************************************************************************
     * 
     **************************************************************************/
    public RayleighDistribution()
    {
        this.rand = new Random();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public double next( double mean, double stddev )
    {
        double variance = Math.pow( stddev, 2 );
        double sigma = Math.sqrt( 4 * variance / ( 4 - Math.PI ) );
        double x = rand.nextGaussian() * sigma + mean;
        double y = rand.nextGaussian() * sigma + mean;

        return Math.sqrt( Math.pow( x, 2 ) + Math.pow( y, 2 ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public double getProbability( double mean, double stddev, double value )
    {
        double variance = Math.pow( stddev, 2 );
        double sigma = Math.sqrt( 4 * variance / ( 4 - Math.PI ) );
        double p = value / Math.pow( sigma, 2 );
        p = p * Math.exp( -Math.pow( value, 2 ) / 2 / Math.pow( sigma, 2 ) );

        return p;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public DistributionType getType()
    {
        return DistributionType.RAYLEIGH;
    }
}
