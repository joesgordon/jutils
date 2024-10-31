package jutils.strip.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AxisTicks
{
    /**  */
    public double minValue;
    /**  */
    public double tickWidth;
    /** The number of tick sections. The number of ticks is one more. */
    public int sectionCount;

    /***************************************************************************
     * 
     **************************************************************************/
    public AxisTicks()
    {
        this.minValue = -1;
        this.tickWidth = 0.5;
        this.sectionCount = 4;
    }

    /***************************************************************************
     * @return the calculated range.
     **************************************************************************/
    public double getRange()
    {
        return tickWidth * sectionCount;
    }

    /***************************************************************************
     * @return the calculated maximum value for this axis.
     **************************************************************************/
    public double getMax()
    {
        return minValue + getRange();
    }
}
