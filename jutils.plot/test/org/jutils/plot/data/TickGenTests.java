package org.jutils.plot.data;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.core.io.LogUtils;
import org.jutils.plot.data.Tick;
import org.jutils.plot.data.TickGen;
import org.jutils.plot.data.TickGen.TickMetrics;
import org.jutils.plot.model.Axis;
import org.jutils.plot.model.Interval;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TickGenTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testGenTicksPrint()
    {
        Axis axis = new Axis();
        TickGen gen = new TickGen();
        List<Tick> ticks = new ArrayList<>();
        int width = 650;
        Interval bounds;

        // bounds = new Interval( -6.0, 5.0 );
        // bounds = new Interval( 12.0, 50824.0 );
        // bounds = new Interval( 6.593, 6.9759 );
        // bounds = new Interval( -8e32, -8e32 );
        // bounds = new Interval( 19, 21 );
        // bounds = new Interval( 4.700843367370089E-4, 5.299156632624369E-4 );
        // bounds = new Interval( 7.499749815335413, 7.499750184664589 );
        // bounds = new Interval( 7.499749993160572, 7.49975000683943 );
        // bounds = new Interval( 7.99970999206626, 7.999710007933737 );
        // bounds = new Interval( 0, Math.nextUp( 0 ) );
        bounds = new Interval( -1.7016479925638157E308,
            1.7016479925638157E308 );
        axis.setBounds( bounds );
        gen.genTicks( axis, width, ticks );

        LogUtils.printDebug( "Interval: " + bounds.toString() );
        LogUtils.printDebug( "Tick count: " + ticks.size() );
        for( Tick t : ticks )
        {
            LogUtils.printDebug( "Tick @ " + t.value + " : " + t.label );
        }
        Assert.assertNotEquals( 0, ticks.size() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    // @Test
    public void testgenerateTickMetrics()
    {
        TickGen gen = new TickGen();

        Interval s = new Interval( 8e-32, 8e-32 );
        TickMetrics tm = gen.generateTickMetrics( 5 * 96, s );

        Assert.assertEquals( 3, tm.tickCount );
    }
}
