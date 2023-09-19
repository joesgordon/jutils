package jutils.math.coords;

import jutils.math.Vector3d;

public class EastNorthUp
{
    /**  */
    public final LatLonAlt position;
    /**  */
    public final Vector3d velocity;
    /**  */
    public final Vector3d acceleration;

    /***************************************************************************
     * 
     **************************************************************************/
    public EastNorthUp()
    {
        this.position = new LatLonAlt();
        this.velocity = new Vector3d();
        this.acceleration = new Vector3d();
    }
}
