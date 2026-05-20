package jutils.strip.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StripChart
{
    /**  */
    public final AxisTicks xTicks;
    /**  */
    public final AxisTicks yTicks;

    /**  */
    public final List<DataBuffer> buffers;

    /***************************************************************************
     * 
     **************************************************************************/
    public StripChart()
    {
        this.xTicks = new AxisTicks();
        this.yTicks = new AxisTicks();
        this.buffers = new ArrayList<>( 5 );

        xTicks.minValue = -30;
        xTicks.sectionCount = 6;
        xTicks.tickWidth = 5;
    }
}
