package jutils.math;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Vector3d extends IVector
{
    /**  */
    public double x;
    /**  */
    public double y;
    /**  */
    public double z;

    /***************************************************************************
     * 
     **************************************************************************/
    public Vector3d()
    {
        this( 0.0, 0.0, 0.0 );
    }

    /***************************************************************************
     * @param x
     * @param y
     * @param z
     **************************************************************************/
    public Vector3d( double x, double y, double z )
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /***************************************************************************
     * @param m
     **************************************************************************/
    public Vector3d( IMatrix m )
    {
        set( m );
    }

    /***************************************************************************
     * @param v
     **************************************************************************/
    public void set( Vector3d v )
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;

        super.setOrientation( v.getRowCount(), v.getColumnCount() );
    }

    /***************************************************************************
     * {@inheritDoc}}
     **************************************************************************/
    @Override
    public double getValue( int index )
    {
        switch( index )
        {
            case 0:
                return x;

            case 1:
                return y;

            case 2:
                return z;
        }

        throw new IllegalArgumentException(
            String.format( "Invalid vector index, %d", index ) );
    }

    /***************************************************************************
     * {@inheritDoc}}
     **************************************************************************/
    @Override
    public void set( IMatrix m )
    {
        this.x = m.getValue( 0 );
        this.y = m.getValue( 1 );
        this.z = m.getValue( 2 );

        super.setOrientation( m.getRowCount(), m.getColumnCount() );
    }

    /***************************************************************************
     * {@inheritDoc}}
     **************************************************************************/
    @Override
    public void setValue( int index, double value )
    {
        switch( index )
        {
            case 0:
                x = value;
                break;

            case 1:
                y = value;
                break;

            case 2:
                z = value;
                break;

            default:
                throw new IllegalArgumentException(
                    String.format( "Invalid vector index, %d", index ) );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getLength()
    {
        return 3;
    }
}
