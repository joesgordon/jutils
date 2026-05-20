package jutils.strip.data;

import java.awt.Color;

import jutils.strip.StripUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataBuffer
{
    /**  */
    private final double [] xs;
    /**  */
    private final double [] ys;

    /**  */
    private int startIdx;
    /**  */
    private int size;

    /**  */
    private Color c;

    /***************************************************************************
     * @param size the number of points to size this buffer.
     **************************************************************************/
    public DataBuffer( int size )
    {
        this.xs = new double[size];
        this.ys = new double[size];
        this.startIdx = 0;
        this.size = 0;
        this.c = new Color( StripUtils.FG_DEFAULT );
    }

    /***************************************************************************
     * @param p storage for the point.
     * @param index the index of the point.
     **************************************************************************/
    public void get( DataPoint p, int index )
    {
        int i = this.startIdx + index;
        i = i < xs.length ? i : i - xs.length;

        p.x = xs[i];
        p.y = ys[i];
    }

    /***************************************************************************
     * @return the number of points that have been added to this buffer.
     **************************************************************************/
    public int getSize()
    {
        return size;
    }

    /***************************************************************************
     * @param x the x-value of the point to add.
     * @param y the y-value of the point to add.
     **************************************************************************/
    public void add( double x, double y )
    {
        int i = startIdx + size;
        i = i < xs.length ? i : i - xs.length;

        xs[i] = x;
        ys[i] = y;

        size++;

        if( size > xs.length )
        {
            size = xs.length;
            startIdx++;

            if( startIdx >= xs.length )
            {
                startIdx = 0;
            }
        }
    }

    /***************************************************************************
     * @param metrics storage for the x-axis metrics to be calculated.
     **************************************************************************/
    public void getXMetrics( DataMetrics metrics )
    {
        getMetrics( metrics, startIdx, size, xs );
    }

    /***************************************************************************
     * @param metrics storage for the y-axis metrics to be calculated.
     **************************************************************************/
    public void getYMetrics( DataMetrics metrics )
    {
        getMetrics( metrics, startIdx, size, ys );
    }

    /***************************************************************************
     * @param metrics storage for the axis to be calculated.
     * @param startIdx {@link #startIdx}
     * @param size {@link #size}
     * @param vals {@link #xs} or {@link #ys}.
     **************************************************************************/
    private static void getMetrics( DataMetrics metrics, int startIdx, int size,
        double [] vals )
    {
        metrics.reset();

        if( size < 1 )
        {
            return;
        }
        else if( size == 1 )
        {
            double v = Math.abs( vals[0] );
            double order = v <= 0 ? 1 : Math.ceil( Math.log( v ) );
            double w = 5 * Math.pow( 10, order );

            if( Double.isNaN( w ) )
            {
                throw new IllegalStateException();
            }

            metrics.range.min = vals[0] - w;
            metrics.range.max = vals[0] + w;
            metrics.count = 1;

            return;
        }

        int si1 = startIdx; // inclusive
        int ei1 = startIdx + size; // exclusive

        int si2 = 0; // inclusive
        int ei2 = 0; // exclusive

        boolean overlapped = ei1 > vals.length;

        ei1 = overlapped ? vals.length : ei1;
        ei2 = overlapped ? size - ( vals.length - si1 ) : 0;

        metrics.range.min = vals[si1];
        metrics.range.max = vals[si1];

        for( int i = ( si1 + 1 ); i < ei1; i++ )
        {
            metrics.update( vals[i] );
        }

        for( int i = si2; i < ei2; i++ )
        {
            metrics.update( vals[i] );
        }

        if( metrics.getRange() == 0 )
        {
            System.exit( 1 );
        }
    }

    /***************************************************************************
     * @return the color to paint this series.
     **************************************************************************/
    public Color getColor()
    {
        return c;
    }

    /***************************************************************************
     * @param c the color to paint this series.
     **************************************************************************/
    public void setColor( Color c )
    {
        if( c == null )
        {
            c = new Color( StripUtils.FG_DEFAULT );
        }

        this.c = c;
    }
}
