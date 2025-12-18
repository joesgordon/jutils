package jutils.math.charts.props;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AxisProperties
{
    /**  */
    public double min;
    /**  */
    public double max;

    /**  */
    public final TicksProperties majorTicks;
    /**  */
    public final TicksProperties minorTicks;

    /***************************************************************************
     * 
     **************************************************************************/
    public AxisProperties()
    {
        this.majorTicks = new TicksProperties();
        this.minorTicks = new TicksProperties();
    }
}
