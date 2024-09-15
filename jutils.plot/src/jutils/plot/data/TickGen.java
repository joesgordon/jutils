package jutils.plot.data;

import java.util.List;

import jutils.plot.model.Axis;
import jutils.plot.model.Interval;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TickGen
{
    /** A half inch in pixels. */
    private static final int DEFAULT_MIN_SIZE = ( int )( 96 * 0.5 );
    /** An inch and a quarter in pixels. */
    private static final int DEFAULT_MAX_SIZE = ( int )( 96 * 1.25 );

    /**  */
    private int minTickSize;
    /**  */
    private int maxTickSize;

    /***************************************************************************
     * 
     **************************************************************************/
    public TickGen()
    {
        this.minTickSize = DEFAULT_MIN_SIZE;
        this.maxTickSize = DEFAULT_MAX_SIZE;
    }

    /***************************************************************************
     * @param axis the axis for which ticks are generated.
     * @param dist the distance in pixels to draw all the ticks.
     * @param ticks the list of ticks generated (empty if the axis has no
     * bounds).
     **************************************************************************/
    public void genTicks( Axis axis, int dist, List<Tick> ticks )
    {
        Interval axisBounds = axis.getBounds();

        ticks.clear();

        if( axisBounds == null )
        {
            return;
        }

        double minTick = axisBounds.min;
        double maxTick = axisBounds.max;

        if( axis.tickStart.isUsed )
        {
            minTick = axis.tickStart.data;
        }

        if( axis.tickEnd.isUsed )
        {
            maxTick = axis.tickEnd.data;
        }

        Interval tickBounds = new Interval( minTick, maxTick );

        TickMetrics tickMets = null;

        if( axis.tickInterval.isUsed )
        {
            tickMets = calculateTickMetrics( tickBounds,
                axis.tickInterval.data );
        }
        else
        {
            tickMets = generateTickMetrics( dist, tickBounds );
        }

        int order = Math.abs( tickMets.tickOrder );
        String fmt = "%.1f";

        if( tickMets.tickOrder < 0 )
        {
            fmt = "%." + order + "f";
        }

        for( int i = 0; i < tickMets.tickCount; i++ )
        {
            double d = tickMets.tickStart + tickMets.tickInterval * i;
            String label = String.format( fmt, d );
            Tick tick = new Tick( d, label );

            ticks.add( tick );
        }
    }

    /***************************************************************************
     * @param bounds
     * @param tickInterval the interval in series space between ticks.
     * @return
     **************************************************************************/
    private static TickMetrics calculateTickMetrics( Interval bounds,
        double tickInterval )
    {
        TickMetrics metrics = new TickMetrics();

        metrics.tickStart = bounds.min;
        metrics.tickInterval = tickInterval;
        metrics.tickCount = ( int )( ( bounds.max - bounds.min ) /
            tickInterval ) + 1;
        metrics.tickOrder = ( int )Math.floor(
            Math.log10( metrics.tickInterval ) );

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
        int orderOffset = 0;

        if( minTickCsNorm <= 100 && 100 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 100;
        }
        else if( minTickCsNorm <= 50 && 50 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 50;
        }
        else if( minTickCsNorm <= 40 && 40 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 40;
        }
        else if( minTickCsNorm <= 20 && 20 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 20;
        }
        // else if( minTickCsNorm <= 20 && 20 <= maxTickCsNorm )
        // {
        // tickWidthCsNorm = 20;
        // }
        else if( minTickCsNorm < 25 && 25 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 25;
            orderOffset = -1;
        }
        else if( minTickCsNorm <= 75 && 75 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 75;
            orderOffset = -1;
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
        metrics.tickOrder = ( int )tickCsOrder + orderOffset;
        metrics.tickStart = tickStart;
        metrics.tickInterval = tickWidthCs;

        // LogUtils.printDebug( "tick count = " + metrics.tickCount + " for " +
        // dist + " w/ " + span );

        return metrics;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    static class TickMetrics
    {
        public double tickInterval;
        public double tickStart;
        public int tickCount;
        public int tickOrder;
    }
}
