package jutils.math.coords;

import jutils.math.IMatrix;
import jutils.math.Matrix;
import jutils.math.Vector3d;

/*******************************************************************************
 * Utility class for earth related functions.
 ******************************************************************************/
public final class Earth
{
    /** Radians per second. */
    public static final double WGS84_ROTATION = 7.292115E-05;
    /**  */
    public static final Vector3d ROTATION;
    /**
     * The <a
     * href="https://en.wikipedia.org/wiki/Eccentricity_(mathematics)">eccentricity</a>
     * of the WGS84 elipse.
     */
    public static final double ECCENTRICITY = 6.69437999014E-3;
    /** Radius of the WGS84 ellipse at the equator. */
    public static final double RADIUS_EQUATOR = 6378137.0;
    /** Radius of the WGS84 ellipse at the pole. */
    public static final double RADIUS_POLE = 6356752.3;

    static
    {
        ROTATION = new Vector3d( 0.0, 0.0, WGS84_ROTATION );
    }

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private Earth()
    {
    }

    /***************************************************************************
     * @param time
     * @return
     **************************************************************************/
    public static double getRotationalAngle( double time )
    {
        return WGS84_ROTATION * time;
    }

    /***************************************************************************
     * @param angle
     * @return
     **************************************************************************/
    public static IMatrix createEciToEcefRotationMatrix( double angle )
    {
        Matrix m = new Matrix( 3, 3 );

        m.setValue( 0, 0, Math.cos( angle ) );
        m.setValue( 0, 1, -Math.sin( angle ) );
        m.setValue( 0, 2, 0 );

        m.setValue( 1, 0, Math.sin( angle ) );
        m.setValue( 1, 1, Math.cos( angle ) );
        m.setValue( 1, 2, 0 );

        m.setValue( 2, 0, 0 );
        m.setValue( 2, 1, 0 );
        m.setValue( 2, 2, 1 );

        return m;
    }
}
