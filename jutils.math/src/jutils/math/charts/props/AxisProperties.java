package jutils.math.charts.props;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AxisProperties
{
    /**  */
    public final TextProperties title;
    /**  */
    public final TextProperties subtitle;

    /**  */
    public double min;
    /**  */
    public double max;

    /**  */
    public final TicksProperties majorTicks;
    /**  */
    public final TicksProperties minorTicks;
    /**  */
    public boolean useAutoBounds;

    /***************************************************************************
     * 
     **************************************************************************/
    public AxisProperties()
    {
        this.title = new TextProperties();
        this.subtitle = new TextProperties();

        this.min = 0.0;
        this.max = 10.0;

        this.majorTicks = new TicksProperties();
        this.minorTicks = new TicksProperties();

        this.useAutoBounds = true;
    }
}
