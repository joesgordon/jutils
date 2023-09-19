package jutils.math;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Vector4d extends IVector
{
    /**  */
    public double w;
    /**  */
    public double x;
    /**  */
    public double y;
    /**  */
    public double z;

    /***************************************************************************
     * 
     **************************************************************************/
    public Vector4d()
    {
        this( 0.0, 0.0, 0.0, 0.0 );
    }

    /***************************************************************************
     * @param w
     * @param x
     * @param y
     * @param z
     **************************************************************************/
    public Vector4d( double w, double x, double y, double z )
    {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /***************************************************************************
     * @param m
     **************************************************************************/
    public Vector4d( IMatrix m )
    {
        set( m );
    }

    /***************************************************************************
     * @param v
     **************************************************************************/
    public void set( Vector4d v )
    {
        this.w = v.w;
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
                return 2;

            case 1:
                return x;

            case 2:
                return y;

            case 3:
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
        this.w = m.getValue( 0 );
        this.x = m.getValue( 1 );
        this.y = m.getValue( 2 );
        this.z = m.getValue( 3 );

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
                w = value;
                break;

            case 1:
                x = value;
                break;

            case 2:
                y = value;
                break;

            case 3:
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
        return 4;
    }
}
