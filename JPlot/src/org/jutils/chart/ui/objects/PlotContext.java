package org.jutils.chart.ui.objects;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.model.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlotContext
{
    /** The x coordinate of the plot drawing area. */
    public int x;
    /** The y coordinate of the plot drawing area. */
    public int y;
    /** The width of the plot drawing area. */
    public int width;
    /** The height of the plot drawing area. */
    public int height;

    /**  */
    public final IAxisCoords domainCoords;
    /**  */
    public final IAxisCoords rangeCoords;
    /**  */
    public final IAxisCoords secDomainCoords;
    /**  */
    public final IAxisCoords secRangeCoords;

    /***************************************************************************
     * @param domainAxis
     * @param rangeAxis
     * @param secDomainAxis
     * @param secRangeAxis
     **************************************************************************/
    public PlotContext( Axis domainAxis, Axis rangeAxis, Axis secDomainAxis,
        Axis secRangeAxis )
    {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;

        this.domainCoords = new DomainDimensionCoords( domainAxis, true );
        this.rangeCoords = new RangeDimensionCoords( rangeAxis, true );
        this.secDomainCoords = new DomainDimensionCoords( secDomainAxis,
            false );
        this.secRangeCoords = new RangeDimensionCoords( secRangeAxis, false );
    }

    /***************************************************************************
     * @param series
     **************************************************************************/
    public void calculateAutoBounds( List<Series> series )
    {
        domainCoords.calculateBounds( series );
        rangeCoords.calculateBounds( series );
        secDomainCoords.calculateBounds( series );
        secRangeCoords.calculateBounds( series );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void restoreAutoBounds()
    {
        domainCoords.getAxis().restoreAuto();
        rangeCoords.getAxis().restoreAuto();
        secDomainCoords.getAxis().restoreAuto();
        secRangeCoords.getAxis().restoreAuto();

        latchCoords();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void latchCoords()
    {
        // TODO Remove bread crumb
        // LogUtils.printDebug( "Latching Coords" );
        // LogUtils.printDebug( Utils.getStackTrace() );

        domainCoords.latchCoords( domainCoords.getAxis().getBounds(), width );
        rangeCoords.latchCoords( rangeCoords.getAxis().getBounds(), height );
        secDomainCoords.latchCoords( secDomainCoords.getAxis().getBounds(),
            width );
        secRangeCoords.latchCoords( secRangeCoords.getAxis().getBounds(),
            height );
    }

    /***************************************************************************
     * @param series
     * @param isDomain
     * @param isPrimary
     * @return
     **************************************************************************/
    public static List<Interval> getIntervals( List<Series> series,
        boolean isDomain, boolean isPrimary )
    {
        List<Interval> intervals = new ArrayList<>( series.size() );

        for( Series s : series )
        {
            boolean isRequestedAxis = false;

            if( isDomain && isPrimary ) // primary domain
            {
                isRequestedAxis = s.isPrimaryDomain;
            }
            else if( isDomain && !isPrimary ) // secondary domain
            {
                isRequestedAxis = !s.isPrimaryDomain;
            }
            else if( !isDomain && isPrimary ) // primary range
            {
                isRequestedAxis = s.isPrimaryRange;
            }
            else if( !isDomain && !isPrimary ) // secondary range
            {
                isRequestedAxis = !s.isPrimaryRange;
            }

            if( s.visible && isRequestedAxis )
            {
                Interval span;

                if( isDomain )
                {
                    span = s.calcDomainSpan();
                }
                else
                {
                    span = s.calcRangeSpan();
                }

                intervals.add( span );
            }
        }

        return intervals;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isAutoBounds()
    {
        // TODO This is terrible fix it!!!!
        return domainCoords.getAxis().isAutoBounds() &&
            rangeCoords.getAxis().isAutoBounds() &&
            secDomainCoords.getAxis().isAutoBounds() &&
            secRangeCoords.getAxis().isAutoBounds();
    }

    /***************************************************************************
     * @param p
     * @return
     **************************************************************************/
    public Point ensurePoint( Point p )
    {
        p.x = ensureHorizontal( p.x );
        p.y = ensureVertical( p.y );

        return p;
    }

    /***************************************************************************
     * @param x
     * @return
     **************************************************************************/
    private int ensureHorizontal( int x )
    {
        x = Math.max( x, this.x );
        x = Math.min( x, this.x + width );

        return x;
    }

    /***************************************************************************
     * @param y
     * @return
     **************************************************************************/
    private int ensureVertical( int y )
    {
        y = Math.max( y, this.y );
        y = Math.min( y, this.y + height );

        return y;
    }

    /***************************************************************************
     * Returns the bounds for the provided intervals that includes each interval
     * or {@code null} if the list is empty.
     * @param isPrimary
     * @param isDomain
     * @param intervals
     * @return
     **************************************************************************/
    private static Interval calculateAutoBounds( List<Series> series,
        boolean isDomain, boolean isPrimary )
    {
        List<Interval> intervals = getIntervals( series, isDomain, isPrimary );
        Double min = null;
        Double max = null;

        for( Interval span : intervals )
        {
            if( span == null )
            {
                continue;
            }

            if( min != null && max != null )
            {
                min = Math.min( min, span.min );
                max = Math.max( max, span.max );
            }
            else
            {
                min = span.min;
                max = span.max;
            }
        }

        if( min == null || max == null )
        {
            if( isPrimary )
            {
                min = -5.0;
                max = 5.0;
            }
            else
            {
                return null;
            }
        }
        else if( min.equals( max ) )
        {
            min -= 0.5;
            max += 0.5;
        }

        double r = max - min;

        return new Interval( min - 0.03 * r, max + 0.03 * r );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IAxisCoords
    {
        public double fromScreen( int s );

        public int fromCoord( double c );

        public Interval getBounds();

        public void calculateBounds( List<Series> series );

        public void latchCoords( Interval bounds, int length );

        public Axis getAxis();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static abstract class AbrstractCoords implements IAxisCoords
    {
        private final Axis axis;
        private final boolean isDomain;
        private final boolean isPrimary;
        protected DimensionStats stats;

        public AbrstractCoords( Axis axis, boolean isDomain, boolean isPrimary )
        {
            this.axis = axis;
            this.isDomain = isDomain;
            this.isPrimary = isPrimary;
            this.stats = new DimensionStats( new Interval( -5, 5 ), 500 );
        }

        @Override
        public abstract double fromScreen( int s );

        @Override
        public abstract int fromCoord( double c );

        @Override
        public final Interval getBounds()
        {
            return stats.bounds;
        }

        @Override
        public final void calculateBounds( List<Series> series )
        {
            axis.setAutoBounds(
                calculateAutoBounds( series, isDomain, isPrimary ) );
        }

        @Override
        public final void latchCoords( Interval bounds, int length )
        {
            this.stats = new DimensionStats( bounds, length );

            // TODO Remove bread crumb
            // if( bounds != null )
            // {
            // LogUtils.printDebug(
            // "Latching " + bounds.toString() + " with len " + length );
            // }
        }

        @Override
        public Axis getAxis()
        {
            return axis;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    static class DomainDimensionCoords extends AbrstractCoords
    {
        /**
         * @param axis
         * @param isPrimary
         */
        public DomainDimensionCoords( Axis axis, boolean isPrimary )
        {
            super( axis, true, isPrimary );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double fromScreen( int s )
        {
            return s / stats.scale + stats.bounds.min;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int fromCoord( double c )
        {
            return ( int )Math.round( ( c - stats.bounds.min ) * stats.scale );
            // return ( int )( ( c - stats.span.min ) * stats.scale );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class RangeDimensionCoords extends AbrstractCoords
    {
        /**
         * @param axis
         * @param isPrimary
         */
        public RangeDimensionCoords( Axis axis, boolean isPrimary )
        {
            super( axis, false, isPrimary );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double fromScreen( int s )
        {
            return stats.bounds.max - s / stats.scale;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int fromCoord( double c )
        {
            return ( int )Math.round(
                stats.length - ( c - stats.bounds.min ) * stats.scale );
            // return ( int )( stats.length - ( c - stats.span.min ) *
            // stats.scale );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DimensionStats
    {
        /**  */
        public final Interval bounds;
        /**  */
        public final double scale;
        /**  */
        public final int length;

        /**
         * @param bounds
         * @param length
         */
        public DimensionStats( Interval bounds, int length )
        {
            this.bounds = bounds;
            this.length = length;
            this.scale = bounds != null ? length / bounds.range : 0;
        }
    }
}
