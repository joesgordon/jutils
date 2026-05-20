package jutils.strip.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataMetrics
{
    /**  */
    public final Range range;
    /**  */
    public int count;

    /***************************************************************************
     * 
     **************************************************************************/
    public DataMetrics()
    {
        this.range = new Range();
        this.count = 0;

        reset();
    }

    /***************************************************************************
     * @param m the metrics to update this with.
     **************************************************************************/
    public void update( DataMetrics m )
    {
        this.range.min = Math.min( range.min, m.range.min );
        this.range.max = Math.max( range.max, m.range.max );
        this.count = Math.max( count, m.count );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        this.range.reset();
        this.count = 0;
    }

    /***************************************************************************
     * @param value the value to update these metrics with.
     **************************************************************************/
    public void update( double value )
    {
        range.update( value );
        count++;
    }

    /***************************************************************************
     * @return the calculated range.
     **************************************************************************/
    public double getRange()
    {
        return range.getRange();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return String.format( "%s with %d points", range, count );
    }
}
