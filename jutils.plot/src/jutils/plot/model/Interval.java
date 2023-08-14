package jutils.plot.model;

import java.util.Objects;

/*******************************************************************************
 * Defines an interval along a dimension.
 ******************************************************************************/
public class Interval
{
    /** The inclusive minimum value of the interval. */
    public final double min;
    /** The inclusive maximum value of the interval. */
    public final double max;
    /** The range of the interval */
    public final double range;

    /***************************************************************************
     * Creates a new interval with the provided bounds.
     * @param min the minimum value of the interval.
     * @param max the maximum value of the interval.
     **************************************************************************/
    public Interval( double min, double max )
    {
        if( min == -0.0 && max == 0.0 )
        {
            min = -0.0001;
            max = 0.0001;
        }

        this.min = min;
        this.max = max;
        this.range = this.max - this.min;
    }

    /***************************************************************************
     * Creates a copy of the provided interval.
     * @param span the interval to be copied.
     **************************************************************************/
    public Interval( Interval span )
    {
        this.min = span.min;
        this.max = span.max;
        this.range = span.range;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Interval zoomIn()
    {
        double min = this.min;
        double max = this.max;

        double maxTest = Math.nextUp( Math.nextUp(
            Math.nextUp( Math.nextUp( Math.nextUp( Math.nextUp( min ) ) ) ) ) );

        if( max > maxTest )
        {
            double r = range / 3.0;

            min += r;
            max -= r;
        }

        return new Interval( min, max );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Interval zoomOut()
    {
        double min = this.min;
        double max = this.max;

        double rangeTest = range * 3.0;

        if( !Double.isNaN( rangeTest ) && !Double.isInfinite( rangeTest ) )
        {
            min -= range;
            max += range;
        }

        return new Interval( min, max );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return Objects.hash( max, min );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }

        if( this == obj )
        {
            return true;
        }

        if( obj instanceof Interval )
        {
            Interval other = ( Interval )obj;

            if( Double.doubleToLongBits( max ) != Double.doubleToLongBits(
                other.max ) )
            {
                return false;
            }

            if( Double.doubleToLongBits( min ) != Double.doubleToLongBits(
                other.min ) )
            {
                return false;
            }

            return true;
        }

        return false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return "[" + min + " " + max + "]";
    }
}
