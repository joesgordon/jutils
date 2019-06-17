package org.jutils.chart.data;

import java.util.Iterator;
import java.util.List;

import org.jutils.chart.model.ISeriesData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DefaultSeries implements ISeriesData<XYPoint>
{
    /**  */
    private final List<XYPoint> points;

    /***************************************************************************
     * @param points
     **************************************************************************/
    public DefaultSeries( List<XYPoint> points )
    {
        this.points = points;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getCount()
    {
        return points.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double getX( int index )
    {
        return get( index ).x;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double getY( int index )
    {
        return get( index ).y;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<XYPoint> iterator()
    {
        return points.iterator();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public XYPoint get( int index )
    {
        return points.get( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isHidden( int index )
    {
        return get( index ).hidden;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setHidden( int index, boolean hidden )
    {
        XYPoint point = get( index );

        if( !hidden && point.isNan() )
        {
            throw new IllegalArgumentException( "The point at index " + index +
                " cannot be set visible because a component is NaN: (" +
                point.x + "," + point.y + ")" );
        }

        get( index ).hidden = hidden;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isSelected( int index )
    {
        return get( index ).hidden;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setSelected( int index, boolean selected )
    {
        get( index ).selected = selected;
    }

    /***************************************************************************
     * @param i
     **************************************************************************/
    public void remove( int i )
    {
        points.remove( i );
    }
}
