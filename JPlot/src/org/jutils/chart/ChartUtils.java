package org.jutils.chart;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.data.DefaultSeries;
import org.jutils.chart.data.XYPoint;
import org.jutils.chart.model.ISeriesData;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class ChartUtils
{
    /***************************************************************************
     * 
     **************************************************************************/
    private ChartUtils()
    {
    }

    /***************************************************************************
     * @param count
     * @param slope
     * @param offset
     * @param min
     * @param max
     * @return
     **************************************************************************/
    public static ISeriesData<?> createLineSeries( int count, double slope,
        double offset, double min, double max )
    {
        List<XYPoint> points = new ArrayList<>();

        double m = ( max - min ) / count;

        for( int i = 0; i < count; i++ )
        {
            XYPoint pt = new XYPoint();

            pt.x = m * i + min;
            pt.y = slope * pt.x + offset;

            points.add( pt );
        }

        return new DefaultSeries( points );
    }

    /**
     * @param count
     * @param amplitude
     * @param frequency
     * @param phase
     * @param min
     * @param max
     * @return
     */
    public static ISeriesData<?> createSinSeries( int count, double amplitude,
        double frequency, double phase, double min, double max )
    {
        List<XYPoint> points = new ArrayList<>();

        double m = ( max - min ) / count;

        for( int i = 0; i < count; i++ )
        {
            XYPoint pt = new XYPoint();

            pt.x = m * i + min;
            pt.y = amplitude * Math.sin( frequency * ( pt.x + phase ) );

            points.add( pt );
        }

        return new DefaultSeries( points );
    }

    /***************************************************************************
     * @param lastlp
     * @param p
     * @return
     **************************************************************************/
    public static double distance( Point lastlp, Point p )
    {
        return Math.sqrt(
            Math.pow( lastlp.x - p.x, 2 ) + Math.pow( lastlp.y - p.y, 2 ) );
    }

    /***************************************************************************
     * @param series
     * @param x
     * @return
     **************************************************************************/
    public static int findNearest( ISeriesData<?> series, double x )
    {
        int lo = 0;
        int hi = series.getCount() - 1;
        int value = -1;

        if( series.getX( hi ) < x )
        {
            value = hi;
        }
        else if( x < series.getX( 0 ) )
        {
            value = 0;
        }
        else
        {
            while( value < 0 && lo <= hi )
            {
                // Key is in a[lo..hi] or not present.
                int mid = lo + ( hi - lo ) / 2;
                double x1 = series.getX( mid );
                double x2 = series.getX( mid + 1 );

                if( x < x1 )
                {
                    hi = mid - 1;
                }
                else if( x2 < x )
                {
                    lo = mid + 1;
                }
                else
                {
                    value = mid;
                }
            }
        }

        if( value < ( series.getCount() - 1 ) &&
            ( x - series.getX( value ) ) > ( series.getX( value + 1 ) - x ) )
        {
            value++;
        }

        if( value > -1 )
        {
            boolean found = false;
            boolean inBound;
            for( int i = 0; i < series.getCount(); i++ )
            {
                hi = value + i;
                lo = value - i;

                if( hi < series.getCount() )
                {
                    if( !series.isHidden( hi ) )
                    {
                        value = hi;
                        found = true;
                        break;
                    }
                    inBound = true;
                }
                else
                {
                    inBound = false;
                }

                if( lo > -1 )
                {
                    if( !series.isHidden( lo ) )
                    {
                        value = lo;
                        found = true;
                        break;
                    }
                    inBound = true;
                }
                else
                {
                    inBound = false;
                }

                if( !inBound )
                {
                    value = -1;
                    break;
                }
            }

            if( !found )
            {
                value = -1;
            }
        }

        // LogUtils.printDebug( "nearest: " + value + " hi: " + hi + " lo: " +
        // lo );

        return value;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static ISeriesData<?> createTestSeries()
    {
        List<XYPoint> points = new ArrayList<>();

        points.add( new XYPoint( -5.0, -1.0 ) );
        points.add( new XYPoint( -4.0, -1.0 ) );
        points.add( new XYPoint( -3.0, -1.0 ) );
        points.add( new XYPoint( -2.0, -1.0 ) );
        points.add( new XYPoint( -1.0, -1.0 ) );
        points.add( new XYPoint( 0.0, -1.0 ) );
        points.add( new XYPoint( 1.0, -1.0 ) );
        points.add( new XYPoint( 2.0, -1.0 ) );
        points.add( new XYPoint( 3.0, -1.0 ) );
        points.add( new XYPoint( 4.0, -1.0 ) );
        points.add( new XYPoint( 5.0, -1.0 ) );

        return new DefaultSeries( points );
    }
}
