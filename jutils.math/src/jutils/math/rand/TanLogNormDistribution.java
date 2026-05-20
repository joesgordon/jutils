package jutils.math.rand;

import java.util.Random;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TanLogNormDistribution implements IDistribution
{
    /**  */
    private final Random rand;
    /**  */
    private final NormalDistribution normal;

    /***************************************************************************
     * 
     **************************************************************************/
    public TanLogNormDistribution()
    {
        this.rand = new Random();
        this.normal = new NormalDistribution();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public double next( double mean, double stddev )
    {
        double mu = Math.log( Math.abs( Math.tan( mean ) ) );
        double sigma = stddev / mean;

        double value = Double.NaN;

        while( Double.isNaN( value ) )
        {
            value = rand.nextGaussian() * sigma + mu;
            value = Math.atan( Math.exp( value ) );
        }

        return value;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public double getProbability( double mean, double stddev, double value )
    {
        double p = 0;

        if( stddev > 0.0 && mean > 0.0 && value > 0.0 )
        {
            double normMean = Math.log( Math.abs( Math.tan( mean ) ) );
            double normStddev = stddev / mean;

            double normVal = Math.log( Math.abs( Math.tan( value ) ) );
            double normProb = normal.getProbability( normMean, normStddev,
                normVal );

            p = normProb / ( Math.cos( value ) * Math.sin( value ) );
        }

        return p;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public DistributionType getType()
    {
        return DistributionType.TANGENT_LOG_NORMAL;
    }
}
