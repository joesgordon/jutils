package org.jutils.plot.ui.objects;

import org.jutils.plot.model.Axis;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RangeDimensionCoords extends AbrstractCoords
{
    /***************************************************************************
     * @param axis
     * @param isPrimary
     **************************************************************************/
    public RangeDimensionCoords( Axis axis, boolean isPrimary )
    {
        super( axis, false, isPrimary );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public double fromScreen( int s )
    {
        return stats.bounds.max - s / stats.scale;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int fromCoord( double c )
    {
        return ( int )Math.round(
            stats.length - ( c - stats.bounds.min ) * stats.scale );
        // return ( int )( stats.length - ( c - stats.span.min ) *
        // stats.scale );
    }
}
