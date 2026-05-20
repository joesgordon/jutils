package jutils.math;

import java.awt.Color;
import java.util.Arrays;

import jutils.core.utils.BitMasks;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Histogram
{
    /**  */
    public final int [] bins;
    /**  */
    public final int minValue;
    /**  */
    public final int maxValue;
    /**  */
    public final Color color;

    /***************************************************************************
     * @param binCount The number of bins.
     * @param minValue The minimum value to be provided for this histogram.
     * @param maxValue The maximum value to be provided for this histogram.
     **************************************************************************/
    public Histogram( int binCount, int minValue, int maxValue )
    {
        this.bins = new int[binCount];
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.color = Color.black;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        Arrays.fill( bins, 0 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getMaxBinCount()
    {
        int count = 0;

        for( int i = 0; i < bins.length; i++ )
        {
            count = Math.max( count, bins[i] );
        }

        return count;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getScale()
    {
        long lmax = maxValue & BitMasks.INT_MASK;
        long lmin = minValue & BitMasks.INT_MASK;

        return ( bins.length - 1.0f ) / ( lmax - lmin );
    }

    /***************************************************************************
     * @param bin
     * @return
     **************************************************************************/
    public int getBinStart( int bin )
    {
        int start = -1;

        return start;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class HistogramCalc
    {
        /**  */
        public final Histogram histogram;
        /** The scale used to get the bin index for a value */
        public final double bindexScale;

        /**
         * @param binCount
         * @param minValue
         * @param maxValue
         */
        public HistogramCalc( int binCount, int minValue, int maxValue )
        {
            this.histogram = new Histogram( binCount, minValue, maxValue );
            this.bindexScale = ( binCount - 1.0f ) /
                ( ( maxValue & BitMasks.INT_MASK ) - minValue );
        }

        /**
         * @param value
         */
        public void addValue( int value )
        {
            long lval = value & BitMasks.INT_MASK;
            double bin = ( lval - histogram.minValue ) * bindexScale;
            int binIndex = ( int )Math.round( bin );

            try
            {
                histogram.bins[binIndex]++;
            }
            catch( ArrayIndexOutOfBoundsException ex )
            {
                String err = String.format(
                    "Unable to increment count for bin %d * %f = %f", lval,
                    bindexScale, bin );
                throw new IllegalStateException( err, ex );
            }
        }

        /**
         * 
         */
        public void reset()
        {
            this.histogram.reset();
        }
    }
}
