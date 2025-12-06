package jutils.math;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RangeMapper
{
    /** Minimum of range 1. */
    public float r1min;
    /** Maximum of range 1. */
    public float r1max;
    /** Minimum of range 2. */
    public float r2min;
    /** Maximum of range 2. */
    public float r2max;

    /***************************************************************************
     * 
     **************************************************************************/
    public RangeMapper()
    {
        this( 0.f, 1.f, -10.f, 10.f );
    }

    /***************************************************************************
     * @param r1n
     * @param r1x
     * @param r2n
     * @param r2x
     **************************************************************************/
    public RangeMapper( float r1n, float r1x, float r2n, float r2x )
    {
        this.r1min = r1n;
        this.r1max = r1x;
        this.r2min = r2n;
        this.r2max = r2x;
    }

    /***************************************************************************
     * @param r1n
     * @param r1x
     * @param r2n
     * @param r2x
     **************************************************************************/
    public void set( float r1n, float r1x, float r2n, float r2x )
    {
        this.r1min = r1n;
        this.r1max = r1x;
        this.r2min = r2n;
        this.r2max = r2x;
    }

    /***************************************************************************
     * @param r1
     * @return
     **************************************************************************/
    public float map( float r1 )
    {
        return r2min + ( r1 - r1min ) * ( r2max - r2min ) / ( r1max - r1min );
    }
}
