package org.jutils.plot.data;

import org.jutils.plot.model.IDataPoint;
import org.jutils.plot.model.ISeriesData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataSeriesPoint implements IDataPoint
{
    /**  */
    private final ISeriesData<?> series;
    /**  */
    private final int index;

    /***************************************************************************
     * @param series
     * @param index
     **************************************************************************/
    public DataSeriesPoint( ISeriesData<?> series, int index )
    {
        this.series = series;
        this.index = index;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double getX()
    {
        return series.getX( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double getY()
    {
        return series.getY( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isSelected()
    {
        return series.isSelected( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isHidden()
    {
        return series.isHidden( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setSelected( boolean selected )
    {
        series.setSelected( index, selected );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setHidden( boolean hidden )
    {
        series.setHidden( index, hidden );
    }
}
