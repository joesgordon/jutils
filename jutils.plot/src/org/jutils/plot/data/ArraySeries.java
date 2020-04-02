package org.jutils.plot.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jutils.plot.model.IDataPoint;
import org.jutils.plot.model.ISeriesData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ArraySeries implements ISeriesData<IDataPoint>
{
    /**  */
    private final List<DataSeries> series;
    /**  */
    private boolean [] hidden;
    /**  */
    private boolean [] selected;

    /**  */
    private int xidx;
    /**  */
    private int yidx;

    /**  */
    private int count;
    /**  */
    private int length;

    /***************************************************************************
     * 
     **************************************************************************/
    public ArraySeries()
    {
        this.series = new ArrayList<>();
        this.hidden = new boolean[0];
        this.selected = new boolean[0];
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<IDataPoint> iterator()
    {
        return new DataSeriesPointIterator( this );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getCount()
    {
        return count;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double getX( int index )
    {
        return series.get( xidx ).data[index];
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double getY( int index )
    {
        return series.get( yidx ).data[index];
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IDataPoint get( int index )
    {
        return new DataSeriesPoint( this, index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isHidden( int index )
    {
        return this.hidden[index];
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setHidden( int index, boolean hidden )
    {
        this.hidden[index] = hidden;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isSelected( int index )
    {
        return this.selected[index];
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setSelected( int index, boolean selected )
    {
        this.selected[index] = selected;
    }

    /***************************************************************************
     * @param points
     **************************************************************************/
    public void addPoints( List<Double> points )
    {
        ensureLength( count++ );

        if( series.size() < 1 )
        {
            for( int i = 0; i < points.size(); i++ )
            {
                DataSeries ds = new DataSeries( length );

                series.add( ds );
            }
        }

        for( int i = 0; i < points.size(); i++ )
        {
            series.get( i ).data[count] = points.get( i );
        }

        count++;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSeriesCount()
    {
        return series.size();
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public void setXSeries( int index )
    {
        if( index >= getSeriesCount() )
        {
            throw new ArrayIndexOutOfBoundsException(
                "Cannot set x-series as " + index + " when there are only " +
                    getSeriesCount() + " series" );
        }

        this.xidx = index;
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public void setYSeries( int index )
    {
        if( index >= getSeriesCount() )
        {
            throw new ArrayIndexOutOfBoundsException(
                "Cannot set y-series as " + index + " when there are only " +
                    getSeriesCount() + " series" );
        }

        this.yidx = index;
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public String getSeriesName( int index )
    {
        return series.get( index ).name;
    }

    /***************************************************************************
     * @param index
     * @param name
     **************************************************************************/
    public void setSeriesName( int index, String name )
    {
        series.get( index ).name = name;
    }

    /***************************************************************************
     * @param newCount
     **************************************************************************/
    private void ensureLength( int newCount )
    {
        if( newCount > length )
        {
            boolean [] bs;
            double [] ds;

            int newLength = newCount + 1024;

            bs = new boolean[newLength];
            System.arraycopy( hidden, 0, bs, 0, hidden.length );
            hidden = bs;

            bs = new boolean[newLength];
            System.arraycopy( selected, 0, bs, 0, selected.length );
            selected = bs;

            for( int i = 0; i < series.size(); i++ )
            {
                DataSeries s = series.get( i );
                ds = new double[newLength];
                System.arraycopy( s.data, 0, ds, 0, s.data.length );
                s.data = ds;
            }

            length = newLength;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DataSeries
    {
        /**  */
        public String name;
        /**  */
        public double [] data;

        /**
         * @param length
         */
        public DataSeries( int length )
        {
            this.data = new double[length];
            this.name = "";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            return name;
        }
    }
}
