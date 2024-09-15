package jutils.math;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class Quaternion
{
    /** The real value. */
    public double r;
    /** The i-direction value. */
    public double i;
    /** The j-direction value. */
    public double j;
    /** The k-direction value. */
    public double k;

    /***************************************************************************
     * 
     **************************************************************************/
    public Quaternion()
    {
        this( 0., 0., 0., 0. );
    }

    /***************************************************************************
     * @param q
     **************************************************************************/
    public Quaternion( Quaternion q )
    {
        this( q.r, q.i, q.j, q.k );
    }

    /***************************************************************************
     * @param r
     * @param i
     * @param j
     * @param k
     **************************************************************************/
    public Quaternion( double r, double i, double j, double k )
    {
        this.r = r;
        this.i = i;
        this.j = j;
        this.k = k;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        this.r = 0.0;
        this.i = 0.0;
        this.j = 0.0;
        this.k = 0.0;
    }

    /***************************************************************************
     * @param m the multiplicand.
     * @return
     **************************************************************************/
    public double dotSum( Quaternion m )
    {
        return m.r * r + m.i * i + m.j * j + m.k * k;
    }

    /***************************************************************************
     * @param scaler
     **************************************************************************/
    public void mult( double scaler )
    {
        r = scaler * r;
        i = scaler * i;
        j = scaler * j;
        k = scaler * k;
    }

    /***************************************************************************
     * @param addend
     **************************************************************************/
    public void add( Quaternion addend )
    {
        r = addend.r + r;
        i = addend.i + i;
        j = addend.j + j;
        k = addend.k + k;
    }

    /***************************************************************************
     * @param q
     **************************************************************************/
    public void set( Quaternion q )
    {
        this.i = q.i;
        this.k = q.k;
        this.j = q.j;
        this.r = q.r;
    }

    /***************************************************************************
     * @param q1
     * @param q2
     * @param t
     * @param v
     * @param reduceTo360
     **************************************************************************/
    public static void slerp( Quaternion q1, Quaternion q2, double t,
        Quaternion v, boolean reduceTo360 )
    {
        double w1;
        double w2;
        boolean flip = false;

        double cosTheta = q1.dotSum( q2 );

        if( reduceTo360 && cosTheta < 0.0f )
        {
            // We need to flip a quaternion for shortest path interpolation
            cosTheta = -cosTheta;
            flip = true;
        }

        double theta = Math.acos( cosTheta );
        double sinTheta = Math.sin( theta );

        if( sinTheta > 0.005f )
        {
            w1 = Math.sin( ( 1.0f - t ) * theta ) / sinTheta;
            w2 = Math.sin( t * theta ) / sinTheta;
        }
        else
        {
            // They're almost the same quaternion
            w1 = 1.0 - t;
            w2 = t;
        }

        if( flip )
        {
            w2 = -w2;
        }

        // v = a * w1 + b * w2
        Quaternion aw1 = new Quaternion( q1 );
        Quaternion bw2 = new Quaternion( q2 );

        aw1.mult( w1 );
        bw2.mult( w2 );

        v.reset();
        v.add( aw1 );
        v.add( bw2 );
    }
}
