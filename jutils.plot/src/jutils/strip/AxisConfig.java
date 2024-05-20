package jutils.strip;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AxisConfig
{
    /**  */
    public boolean autoBounds;
    /**  */
    public double minValue;
    /**  */
    public double tickWidth;
    /** Really the number of tick sections. The number of ticks is one more. */
    public int tickCount;

    /***************************************************************************
     * 
     **************************************************************************/
    public AxisConfig()
    {
        this.autoBounds = true;
        this.minValue = -1;
        this.tickWidth = 0.5;
        this.tickCount = 4;
    }

    /***************************************************************************
     * @return the calculated range.
     **************************************************************************/
    public double getRange()
    {
        return tickWidth * tickCount;
    }

    /***************************************************************************
     * @return the calculated maximum value for this axis.
     **************************************************************************/
    public double getMax()
    {
        return minValue + getRange();
    }
}
