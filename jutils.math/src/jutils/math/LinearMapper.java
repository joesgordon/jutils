package jutils.math;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LinearMapper
{
    /** Source domain. */
    public final Domain source;
    /** Destination domain. */
    public final Domain dest;

    /***************************************************************************
     * 
     **************************************************************************/
    public LinearMapper()
    {
        this( 0.f, 1.f, -10.f, 10.f );
    }

    /***************************************************************************
     * @param srcMin
     * @param srcMax
     * @param destMin
     * @param destMax
     **************************************************************************/
    public LinearMapper( float srcMin, float srcMax, float destMin,
        float destMax )
    {
        this.source = new Domain( srcMin, srcMax );
        this.dest = new Domain( destMin, destMax );
    }

    /***************************************************************************
     * @param r1n
     * @param r1x
     * @param r2n
     * @param r2x
     **************************************************************************/
    public void set( float srcMin, float srcMax, float destMin, float destMax )
    {
        this.source.set( srcMin, srcMax );
        this.dest.set( destMin, destMax );
    }

    /***************************************************************************
     * @param s
     * @return
     **************************************************************************/
    public float mapSourceToDestination( float s )
    {
        float r2 = dest.min + ( s - source.min ) * ( dest.max - dest.min ) /
            ( source.max - source.min );
        return r2;
    }

    /***************************************************************************
     * @param d
     * @return
     **************************************************************************/
    public float mapDestinationToSource( float d )
    {
        float r1 = source.min + ( d - dest.min ) * ( source.max - source.min ) /
            ( dest.max - dest.min );
        return r1;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class Domain
    {
        /**  */
        public float min;
        /**  */
        public float max;

        /**
         * 
         */
        public Domain()
        {
            this( 0.f, 100.f );
        }

        /**
         * @param min
         * @param max
         */
        public Domain( float min, float max )
        {
            this.min = min;
            this.max = max;
        }

        /**
         * @return
         */
        public float getRange()
        {
            return Math.abs( max - min );
        }

        /**
         * @param min
         * @param max
         */
        public void set( float min, float max )
        {
            this.min = min;
            this.max = max;
        }
    }
}
