package jutils.math.coords;

import jutils.math.IMatrix;
import jutils.math.Vector3d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ecef
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
    public Ecef()
    {
        this.position = new Vector3d();
        this.velocity = new Vector3d();
        this.acceleration = new Vector3d();
    }

    /***************************************************************************
     * @param time
     * @param eci2
     * @return
     **************************************************************************/
    public Eci toEci( double time )
    {
        Eci eci = new Eci();

        toEci( eci, time );

        return eci;
    }

    /***************************************************************************
     * @param time
     * @param eci
     **************************************************************************/
    public void toEci( Eci eci, double time )
    {
        double angle = Earth.getRotationalAngle( time );
        IMatrix rot = Earth.createEciToEcefRotationMatrix( angle );

        rot.transpose();

        Vector3d t1;
        Vector3d t2;

        eci.velocity.set( velocity );
        eci.acceleration.set( acceleration );

        // ---------------------------------------------------------------------
        // Position
        // ---------------------------------------------------------------------
        position.mult( rot, eci.position );

        // ---------------------------------------------------------------------
        // Velocity
        // ---------------------------------------------------------------------
        t1 = new Vector3d();
        t2 = new Vector3d();
        Earth.ROTATION.crossProduct( eci.position, t1 );
        velocity.mult( rot, t2 );
        t1.add( t2, eci.velocity );

        // ---------------------------------------------------------------------
        // Acceleration
        // ---------------------------------------------------------------------
        // TODO actually rotate acceleration
        eci.acceleration.set( acceleration );
    }
}
