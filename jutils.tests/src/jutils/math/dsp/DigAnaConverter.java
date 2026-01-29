package jutils.math.dsp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DigAnaConverter
{
    /**  */
    private int bits;
    /**  */
    private double min;
    /**  */
    private double max;

    /***************************************************************************
     * 
     **************************************************************************/
    public DigAnaConverter()
    {
        this( 16, -5.0, 5.0 );
    }

    /***************************************************************************
     * @param bits
     * @param min
     * @param max
     **************************************************************************/
    public DigAnaConverter( int bits, double min, double max )
    {
        this.bits = bits;
        this.min = min;
        this.max = max;
    }

    /***************************************************************************
     * @param sample
     * @return
     **************************************************************************/
    public int analogToDigital( double sample )
    {
        int levels = ( 1 << bits ) - 1;
        double range = max - min;

        double scaled = ( sample - min ) / range * levels;

        return ( int )Math.round( scaled );
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public double digitalToAnalog( int value )
    {
        int levels = ( 1 << bits ) - 1;
        double range = max - min;

        double sample = range / levels * value + min;

        return sample;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getMinimum()
    {
        return min;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getMaximum()
    {
        return max;
    }
}
