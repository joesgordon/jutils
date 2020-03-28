package org.jutils.chart.data;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jutils.chart.model.IDataPoint;
import org.jutils.chart.model.ISeriesData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataSeriesPointIterator implements Iterator<IDataPoint>
{
    /**  */
    private final ISeriesData<?> series;

    /**  */
    private int index;

    /***************************************************************************
     * @param series
     **************************************************************************/
    public DataSeriesPointIterator( ISeriesData<?> series )
    {
        this.series = series;
        this.index = 0;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean hasNext()
    {
        return index < series.getCount();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IDataPoint next()
    {
        try
        {
            return new DataSeriesPoint( series, index++ );
        }
        catch( ArrayIndexOutOfBoundsException ex )
        {
            throw new NoSuchElementException( ex.getMessage() );
        }
    }
}
