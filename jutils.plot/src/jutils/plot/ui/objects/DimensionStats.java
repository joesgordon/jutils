package jutils.plot.ui.objects;

import jutils.plot.model.Interval;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DimensionStats
{
    /**  */
    public final Interval bounds;
    /**  */
    public final double scale;
    /**  */
    public final int length;

    /***************************************************************************
     * @param bounds
     * @param length
     **************************************************************************/
    public DimensionStats( Interval bounds, int length )
    {
        this.bounds = bounds;
        this.length = length;
        this.scale = bounds != null ? length / bounds.range : 0;
    }
}
