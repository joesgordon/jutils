package jutils.math.coords;

import jutils.math.Vector3d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LatLonAlt
{
    /**  */
    public double latitude;
    /**  */
    public double longitude;
    /**  */
    public double altitude;

    /***************************************************************************
     * 
     **************************************************************************/
    public LatLonAlt()
    {
        this( 0.0, 0.0, 0.0 );
    }

    /***************************************************************************
     * @param latitude
     * @param longitude
     * @param altitude
     **************************************************************************/
    public LatLonAlt( double latitude, double longitude, double altitude )
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Vector3d toEcef()
    {
        return toEcef( this );
    }

    /***************************************************************************
     * @param lla
     * @return
     **************************************************************************/
    public static Vector3d toEcef( LatLonAlt lla )
    {
        Vector3d ecef = new Vector3d();

        toEcef( lla, ecef );

        return ecef;
    }

    /***************************************************************************
     * @param lla
     * @param ecef
     **************************************************************************/
    private static void toEcef( LatLonAlt lla, Vector3d ecef )
    {
        double latr = Math.toRadians( lla.latitude );
        double lonr = Math.toRadians( lla.longitude );

        double slat = Math.sin( latr );
        double clat = Math.cos( latr );
        double slon = Math.sin( lonr );
        double clon = Math.cos( lonr );

        double r0 = Earth.RADIUS_EQUATOR /
            Math.sqrt( 1.0 - Earth.ECCENTRICITY * slat * slat );
        double rx = ( lla.altitude + r0 ) * clon * clat;
        double ry = ( lla.altitude + r0 ) * slon * clat;
        double rz = ( lla.altitude + r0 * ( 1.0 - Earth.ECCENTRICITY ) ) * slat;

        ecef.x = rx;
        ecef.y = ry;
        ecef.z = rz;
    }
}
