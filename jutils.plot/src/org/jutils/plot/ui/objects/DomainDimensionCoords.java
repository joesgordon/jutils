package org.jutils.plot.ui.objects;

import org.jutils.plot.model.Axis;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DomainDimensionCoords extends AbrstractCoords
{
    /***************************************************************************
     * @param axis
     * @param isPrimary
     **************************************************************************/
    public DomainDimensionCoords( Axis axis, boolean isPrimary )
    {
        super( axis, true, isPrimary );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public double fromScreen( int s )
    {
        return s / stats.scale + stats.bounds.min;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int fromCoord( double c )
    {
        return ( int )Math.round( ( c - stats.bounds.min ) * stats.scale );
        // return ( int )( ( c - stats.span.min ) * stats.scale );
    }
}
