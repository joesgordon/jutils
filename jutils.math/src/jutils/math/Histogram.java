package jutils.math;

import java.awt.Color;
import java.util.Arrays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Histogram
{
    /** Data description. */
    public final String name;
    /**  */
    public final int [] bins;
    /** The scale used to get the bin index for a value */
    public final double bindexScale;
    /**  */
    public final int maxValue;
    /**  */
    public final Color color;
    /** The maximum value in {@link #bins}. */
    private int maxCount;

    /***************************************************************************
     * @param name
     * @param binCount The number of bins.
     * @param maxValue The maximum value to be provided for this histogram.
     **************************************************************************/
    public Histogram( String name, int binCount, int maxValue )
    {
        this.name = name;
        this.bins = new int[binCount];
        this.bindexScale = ( bins.length - 1 ) / ( float )maxValue;
        this.maxValue = maxValue;
        this.maxCount = 0;
        this.color = Color.black;
    }

    /***************************************************************************
     * @return the maximum value in {@link #bins}
     **************************************************************************/
    public int getMaxBinCount()
    {
        return maxCount;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        this.maxCount = 0;
        Arrays.fill( bins, 0 );
    }

    /***************************************************************************
     * @param value
     **************************************************************************/
    public void addValue( int value )
    {
        double bin = value * bindexScale;
        int binIndex = ( int )Math.round( bin );

        bins[binIndex]++;

        maxCount = Math.max( maxCount, bins[binIndex] );
    }
}
