package jutils.math.coords;

import jutils.math.IMatrix;
import jutils.math.Vector3d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Eci
{
    /**  */
    public final Vector3d position;
    /**  */
    public final Vector3d velocity;
    /**  */
    public final Vector3d acceleration;

    /***************************************************************************
     * 
     **************************************************************************/
    public Eci()
    {
        this.position = new Vector3d();
        this.velocity = new Vector3d();
        this.acceleration = new Vector3d();
    }

    /***************************************************************************
     * @param time
     * @return
     **************************************************************************/
    public Ecef toEcef( double time )
    {
        Ecef ecef = new Ecef();

        toEcef( ecef, time );

        return ecef;
    }

    /***************************************************************************
     * @param ecef
     * @param time
     **************************************************************************/
    public void toEcef( Ecef ecef, double time )
    {
        toEcef( this, ecef, time );
    }

    /***************************************************************************
     * @param eci
     * @param ecef
     * @param time
     **************************************************************************/
    private static void toEcef( Eci eci, Ecef ecef, double time )
    {
        Vector3d v1 = new Vector3d();
        Vector3d v2 = new Vector3d();
        Vector3d ecefPosCross = new Vector3d();
        Vector3d ecefVelCross = new Vector3d();

        double angle = Earth.getRotationalAngle( time );
        IMatrix rot = Earth.createEciToEcefRotationMatrix( angle );

        Earth.ROTATION.crossProduct( ecef.position, ecefPosCross );
        Earth.ROTATION.crossProduct( ecef.velocity, ecefVelCross );

        // ---------------------------------------------------------------------
        // Translate position.
        // ---------------------------------------------------------------------

        eci.position.mult( rot, ecef.position );

        // ---------------------------------------------------------------------
        // Translate velocity
        // ---------------------------------------------------------------------

        eci.velocity.mult( rot, ecef.velocity );
        ecef.velocity.sub( ecefPosCross );

        // ---------------------------------------------------------------------
        // Translate acceleration
        // ---------------------------------------------------------------------

        Earth.ROTATION.crossProduct( ecefPosCross, v1 );

        v2.set( ecefVelCross );
        v2.scale( 2.0 );

        v2.set( ecefPosCross );

        eci.acceleration.mult( rot, ecef.acceleration );
        eci.acceleration.sub( v1 );
        eci.acceleration.sub( v2 );
    }
}
