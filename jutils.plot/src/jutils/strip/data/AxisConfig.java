package jutils.strip.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AxisConfig
{
    /**  */
    public boolean autoBounds;
    /**  */
    public final AxisTicks ticks;

    /***************************************************************************
     * 
     **************************************************************************/
    public AxisConfig()
    {
        this.autoBounds = true;
        this.ticks = new AxisTicks();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getRange()
    {
        return ticks.getRange();
    }
}