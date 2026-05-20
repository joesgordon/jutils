package jutils.math.charts.props;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AxesProperties
{
    /**  */
    public boolean visible;
    /**  */
    public final AxisProperties domain;
    /**  */
    public final AxisProperties range;

    /***************************************************************************
     * 
     **************************************************************************/
    public AxesProperties()
    {
        this( true );
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public AxesProperties( boolean visible )
    {
        this.visible = visible;
        this.domain = new AxisProperties();
        this.range = new AxisProperties();
    }
}
