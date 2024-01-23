package jutils.math;

import java.util.Random;

import jutils.core.INamedValue;

/*******************************************************************************
 * Generates random values and data points based on set of customizable
 * distribution characteristics.
 ******************************************************************************/
public class RandomDistribution
{
    // --------------------------------------------------------------------------
    // Class variables.
    // --------------------------------------------------------------------------
    /**  */
    private transient Random rand = new Random();
    /**  */
    private double mean = 1.0;
    /**  */
    private double standardDeviation = 1.0;
    /**  */
    private double value = 0.0;
    /**  */
    private double probability = 0.0;
    /**  */
    private DistributionType distribution = DistributionType.NORMAL;

    /***************************************************************************
     * Constructs a random distribution.
     **************************************************************************/
    public RandomDistribution()
    {
        this( null );
    }

    /***************************************************************************
     * Constructs a copy of a random distribution instance.
     * @param copy - the distribution to be copied.
     **************************************************************************/
    public RandomDistribution( RandomDistribution copy )
    {
        if( copy != null )
        {
            // TODO: need to copy the rand
            this.mean = copy.mean;
            this.standardDeviation = copy.standardDeviation;
            this.value = copy.value;
            this.probability = copy.probability;
            this.distribution = copy.distribution;
        }
        else
        {
            nextValue();
        }
    }

    /***************************************************************************
     * @param x double
     * @return double
     **************************************************************************/
    private double rayleighDensityFunction( double x )
    {
        double variance = Math.pow( standardDeviation, 2 );
        double sigma = Math.sqrt( 4 * variance / ( 4 - Math.PI ) );
        double p = x / Math.pow( sigma, 2 );
        p = p * Math.exp( -Math.pow( x, 2 ) / 2 / Math.pow( sigma, 2 ) );

        return p;
    }

    /***************************************************************************
     * @param x double
     * @return double
     **************************************************************************/
    private double logNormalDensityFunction( double x )
    {
        double p = 0;
        if( standardDeviation > 0.0 && mean > 0.0 )
        {
            double t1 = Math.pow( standardDeviation / mean, 2 ) + 1;
            double sigma = Math.sqrt( Math.log( t1 ) );
            double mu = Math.log( mean ) - 0.5 * Math.log( t1 );

            if( x > 0.0 )
            {
                p = normalDensityFunction( Math.log( x ), sigma, mu ) / x;
                // p = normalDensityFunction( Math.log( x ) ) / x;
            }
        }
        return p;
    }

    /***************************************************************************
     * @param x double
     * @return double
     **************************************************************************/
    private double tangentLogNormalDensityFunction( double x )
    {
        double p = 0;
        if( standardDeviation > 0.0 && mean > 0.0 && x > 0.0 )
        {
            double mu = Math.log( Math.abs( Math.tan( mean ) ) );
            double sigma = standardDeviation / mean;

            p = normalDensityFunction( Math.log( Math.abs( Math.tan( x ) ) ),
                sigma, mu ) / ( Math.cos( x ) * Math.sin( x ) );

        }
        return p;
    }

    /***************************************************************************
     * @param x double
     * @return double
     **************************************************************************/
    private double normalDensityFunction( double x )
    {
        return normalDensityFunction( x, standardDeviation, mean );
    }

    /***************************************************************************
     * @param x double
     * @param sigma double
     * @param mu double
     * @return double
     **************************************************************************/
    private static double normalDensityFunction( double x, double sigma,
        double mu )
    {
        double p = Math.exp(
            -( Math.pow( x - mu, 2 ) ) / ( 2 * Math.pow( sigma, 2 ) ) ) /
            ( sigma * Math.sqrt( 2 * Math.PI ) );

        return p;
    }

    /***************************************************************************
     * @param x double
     * @return double
     **************************************************************************/
    private double uniformDensityFunction( double x )
    {
        double p = 0;
        if( x >= ( mean - standardDeviation ) &&
            x <= ( mean + standardDeviation ) )
        {
            p = 1 / ( 2 * standardDeviation );
        }
        return p;
    }

    /***************************************************************************
     * Returns the mean.
     * @return the mean.
     **************************************************************************/
    public double getMean()
    {
        return mean;
    }

    /***************************************************************************
     * Returns the standard deviation.
     * @return the standard deviation.
     **************************************************************************/
    public double getStandardDeviation()
    {
        return standardDeviation;
    }

    /***************************************************************************
     * Returns the current random value.
     * @return the current random value.
     **************************************************************************/
    public double getValue()
    {
        return value;
    }

    public void setValue( double value )
    {
        this.value = value;
    }

    /***************************************************************************
     * Returns the probability of the current value.
     * @return the probability of the current value.
     **************************************************************************/
    public double getProbability()
    {
        return probability;
    }

    /***************************************************************************
     * Returns the probability of the value x passed to the method, given the
     * current mean, standard deviation, and distribution settings of the Random
     * Distribution.
     * @param x - the value of which the probability will be calculated.
     * @return the probability of the passed value x.
     **************************************************************************/
    public double getProbability( double x )
    {
        double tempProb = Double.NaN;
        switch( distribution )
        {
            case UNIFORM:
            {
                tempProb = uniformDensityFunction( x );
                break;
            }
            case NORMAL:
            {
                tempProb = normalDensityFunction( x );
                break;
            }
            case LOG_NORMAL:
            {
                tempProb = logNormalDensityFunction( x );
                break;
            }
            case TANGENT_LOG_NORMAL:
            {
                tempProb = tangentLogNormalDensityFunction( x );
                break;
            }
            case RAYLEIGH:
            {
                tempProb = rayleighDensityFunction( x );
                break;
            }
        }

        return tempProb;
    }

    /***************************************************************************
     * Returns the distribution type represented by a static class defined int.
     * @return the distribution type represented by a static class defined int.
     **************************************************************************/
    public DistributionType getDistribution()
    {
        return distribution;
    }

    /***************************************************************************
     * Returns a new random value based on the distribution.
     * @return a new random value.
     **************************************************************************/
    public double nextValue()
    {
        switch( distribution )
        {
            case UNIFORM:
            {
                value = ( rand.nextDouble() - 0.5 ) * 2 * standardDeviation +
                    mean;
                break;
            }
            case NORMAL:
            {
                value = rand.nextGaussian() * standardDeviation + mean;
                break;
            }
            case LOG_NORMAL:
            {
                double t1 = Math.pow( standardDeviation / mean, 2 ) + 1;
                double sigma = Math.sqrt( Math.log( t1 ) );
                double mu = Math.log( mean ) - 0.5 * Math.log( t1 );

                value = rand.nextGaussian() * sigma + mu;
                value = Math.exp( value );
                break;
            }
            case TANGENT_LOG_NORMAL:
            {
                double mu = Math.log( Math.abs( Math.tan( mean ) ) );
                double sigma = standardDeviation / mean;

                value = Double.NaN;

                while( Double.isNaN( value ) )
                {
                    value = rand.nextGaussian() * sigma + mu;
                    value = Math.atan( Math.exp( value ) );
                }
                break;
            }
            case RAYLEIGH:
            {
                double variance = Math.pow( standardDeviation, 2 );
                double sigma = Math.sqrt( 4 * variance / ( 4 - Math.PI ) );
                double x = rand.nextGaussian() * sigma + mean;
                double y = rand.nextGaussian() * sigma + mean;
                value = Math.sqrt( Math.pow( x, 2 ) + Math.pow( y, 2 ) );
                break;
            }
        }

        probability = getProbability( value );

        return value;
    }

    /***************************************************************************
     * Sets the distribution type.
     * @param userDistribution int
     **************************************************************************/
    public void setDistribution( DistributionType userDistribution )
    {
        distribution = userDistribution;
    }

    /***************************************************************************
     * Sets the mean.
     * @param userMean double
     **************************************************************************/
    public void setMean( double userMean )
    {
        mean = userMean;
    }

    /***************************************************************************
     * Sets the standard deviation.
     * @param userStandardDeviation double
     **************************************************************************/
    public void setStandardDeviation( double userStandardDeviation )
    {
        standardDeviation = userStandardDeviation;
    }

    /***************************************************************************
     * Sets the seed.
     * @param seed long
     **************************************************************************/
    public void setSeed( long seed )
    {
        rand = new Random( seed );
    }

    /***************************************************************************
     * Distribution type enumerations.
     **************************************************************************/
    public static enum DistributionType implements INamedValue
    {
        /**  */
        UNIFORM( 0, "Uniform" ),
        /**  */
        NORMAL( 1, "Normal" ),
        /**  */
        LOG_NORMAL( 2, "Log-Normal" ),
        /**  */
        TANGENT_LOG_NORMAL( 3, "Tagent Log-Normal" ),
        /**  */
        RAYLEIGH( 4, "Rayleigh" ),;

        /**  */
        public final int value;
        /**  */
        public final String name;

        /**
         * @param value
         * @param name
         */
        private DistributionType( int value, String name )
        {
            this.value = value;
            this.name = name;
        }

        @Override
        public String getName()
        {
            return name;
        }

        @Override
        public int getValue()
        {
            return value;
        }
    }
}
