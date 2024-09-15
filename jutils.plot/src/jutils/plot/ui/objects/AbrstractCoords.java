package jutils.plot.ui.objects;

import java.util.List;

import jutils.plot.model.Axis;
import jutils.plot.model.Interval;
import jutils.plot.model.Series;

/***************************************************************************
 * 
 **************************************************************************/
public abstract class AbrstractCoords implements IAxisCoords
{
    /**  */
    private final Axis axis;
    /**  */
    private final boolean isDomain;
    /**  */
    private final boolean isPrimary;
    /**  */
    protected DimensionStats stats;

    /***************************************************************************
     * @param axis
     * @param isDomain
     * @param isPrimary
     **************************************************************************/
    public AbrstractCoords( Axis axis, boolean isDomain, boolean isPrimary )
    {
        this.axis = axis;
        this.isDomain = isDomain;
        this.isPrimary = isPrimary;
        this.stats = new DimensionStats( new Interval( -5, 5 ), 500 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public abstract double fromScreen( int s );

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public abstract int fromCoord( double c );

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final Interval getBounds()
    {
        return stats.bounds;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final void calculateBounds( List<Series> series )
    {
        Interval bounds = PlotContext.calculateAutoBounds( series, isDomain,
            isPrimary );

        axis.setAutoBounds( bounds );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
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

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Axis getAxis()
    {
        return axis;
    }
}
