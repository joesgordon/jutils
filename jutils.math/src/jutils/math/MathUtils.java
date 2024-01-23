package jutils.math;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class MathUtils
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private MathUtils()
    {
    }

    /***************************************************************************
     * @param x1
     * @param v1
     * @param x2
     * @param v2
     * @param x
     * @param v
     **************************************************************************/
    public static void interp( double x1, Vector3d v1, double x2, Vector3d v2,
        double x, Vector3d v )
    {
        v.x = interp( x1, v1.x, x2, v2.x, x );
        v.y = interp( x1, v1.y, x2, v2.y, x );
        v.z = interp( x1, v1.z, x2, v2.z, x );
    }

    /***************************************************************************
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x
     * @return
     **************************************************************************/
    public static double interp( double x1, double y1, double x2, double y2,
        double x )
    {
        return y1 + ( x - x1 ) * ( y2 - y1 ) / ( x2 - x1 );
    }

    /***************************************************************************
     * @param m
     **************************************************************************/
    public static void radToDeg( IMatrix m )
    {
        m.translate( ( d ) -> Math.toDegrees( d ) );
    }

    /***************************************************************************
     * @param m
     **************************************************************************/
    public static void degToRad( IMatrix m )
    {
        m.translate( ( d ) -> Math.toRadians( d ) );
    }

    /***************************************************************************
     * @param x
     * @param y
     * @return
     **************************************************************************/
    public static double rss( double x, double y )
    {
        return Math.sqrt( x * x + y * y );
    }
}
