package org.jutils.chart.data;

import java.util.List;

import org.jutils.chart.model.Axis;
import org.jutils.chart.model.Interval;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TickGen
{
    /**  */
    private static final int DEFAULT_MIN_SIZE = ( int )( 96 * 0.5 );
    /**  */
    private static final int DEFAULT_MAX_SIZE = ( int )( 96 * 1.25 );
    /**  */
    private int minTickSize;
    /**  */
    private int maxTickSize;

    /***************************************************************************
     * @param axis
     **************************************************************************/
    public TickGen()
    {
        this.minTickSize = DEFAULT_MIN_SIZE;
        this.maxTickSize = DEFAULT_MAX_SIZE;
    }

    /***************************************************************************
     * @param axis
     * @param dist
     * @param ticks
     **************************************************************************/
    public void genTicks( Axis axis, int dist, List<Tick> ticks )
    {
        Interval bounds = axis.getBounds();

        ticks.clear();

        if( bounds == null )
        {
            return;
        }

        TickMetrics tickMets;

        if( axis.autoTicks )
        {
            tickMets = generateTickMetrics( dist, bounds );
        }
        else
        {
            tickMets = calculateMetrics( axis );
        }

        int order = Math.abs( tickMets.tickOrder );
        String fmt = "%.0f";

        if( order < 0 )
        {
            fmt = "%f";
        }
        else if( tickMets.tickOrder < 0 )
        {
            fmt = "%." + order + "f";
        }

        for( int i = 0; i < tickMets.tickCount; i++ )
        {
            double d = tickMets.tickStart + tickMets.tickWidth * i;

            ticks.add( new Tick( d, String.format( fmt, d ) ) );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static TickMetrics calculateMetrics( Axis axis )
    {
        TickMetrics metrics = new TickMetrics();

        metrics.tickStart = axis.tickStart;
        metrics.tickWidth = axis.tickWidth;
        metrics.tickCount = ( int )( ( axis.tickEnd - axis.tickStart ) /
            axis.tickWidth ) + 1;
        metrics.tickOrder = ( int )Math.floor(
            Math.log10( metrics.tickWidth ) );

        return metrics;
    }

    /***************************************************************************
     * @param dist
     * @param span
     * @return
     **************************************************************************/
    TickMetrics generateTickMetrics( int dist, Interval span )
    {
        TickMetrics metrics = new TickMetrics();

        double minTickPx = dist / ( double )maxTickSize;
        double maxTickPx = dist / ( double )minTickSize;

        double minTickCs = span.range / maxTickPx;
        double maxTickCs = span.range / minTickPx;

        int minTickCsOrder = ( int )Math.floor( Math.log10( minTickCs ) );

        int minTickCsNorm = ( int )Math.floor(
            minTickCs * 10 / Math.pow( 10, minTickCsOrder ) );
        int maxTickCsNorm = ( int )Math.floor(
            maxTickCs * 10 / Math.pow( 10, minTickCsOrder ) );

        int tickWidthCsNorm = 0;

        if( minTickCsNorm <= 100 && 100 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 100;
        }
        else if( minTickCsNorm <= 50 && 50 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 50;
        }
        // else if( minTickCsNorm <= 20 && 20 <= maxTickCsNorm )
        // {
        // tickWidthCsNorm = 20;
        // }
        else if( minTickCsNorm < 25 && 25 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 25;
        }
        else if( minTickCsNorm <= 75 && 75 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 75;
        }
        else
        {
            tickWidthCsNorm = 10 * ( int )Math.ceil( minTickCsNorm / 10.0 );
        }

        double tickWidthCs = tickWidthCsNorm * Math.pow( 10, minTickCsOrder ) /
            10.0;

        double tickCsOrder = Math.floor( Math.log10( tickWidthCs ) );

        long minCsNorm = Math.round(
            span.min * 10 / Math.pow( 10, tickCsOrder ) );
        long maxCsNorm = Math.round(
            span.max * 10 / Math.pow( 10, tickCsOrder ) );

        double addend = Math.round(
            10 * tickWidthCs / Math.pow( 10, tickCsOrder ) );

        double minTickNorm = addend * ( long )Math.floor( minCsNorm / addend );
        double maxTickNorm = addend * ( long )Math.floor( maxCsNorm / addend );

        double tickStart = minTickNorm < minCsNorm ? minTickNorm + addend
            : minTickNorm;
        double tickStop = maxTickNorm > maxCsNorm ? maxTickNorm - addend
            : maxTickNorm;

        tickStart = tickStart / 10 * Math.pow( 10, tickCsOrder );
        tickStop = tickStop / 10 * Math.pow( 10, tickCsOrder );

        metrics.tickCount = ( int )( Math.round(
            ( tickStop - tickStart ) / tickWidthCs ) ) + 1;
        metrics.tickOrder = ( int )tickCsOrder;
        metrics.tickStart = tickStart;
        metrics.tickWidth = tickWidthCs;

        // LogUtils.printDebug( "tick count = " + metrics.tickCount + " for " +
        // dist + " w/ " + span );

        return metrics;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    static class TickMetrics
    {
        public double tickWidth;
        public double tickStart;
        public int tickCount;
        public int tickOrder;
    }
}
