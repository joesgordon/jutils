package jutils.math;

import java.util.function.Function;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IMatrix
{
    /***************************************************************************
     * @return
     **************************************************************************/
    public int getRowCount();

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getColumnCount();

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public double getValue( int index );

    /***************************************************************************
     * @param index
     * @param value
     * @return
     **************************************************************************/
    public void setValue( int index, double value );

    /***************************************************************************
     * @param row
     * @param col
     * @return
     **************************************************************************/
    public double getValue( int row, int col );

    /***************************************************************************
     * Sets the values of this matrix to the values of the provided one.
     * @param m
     **************************************************************************/
    public default void set( IMatrix m )
    {
        for( int r = 0; r < m.getRowCount(); r++ )
        {
            for( int c = 0; c < m.getColumnCount(); c++ )
            {
                setValue( r, c, m.getValue( r, c ) );
            }
        }
    }

    /***************************************************************************
     * @param row
     * @param col
     * @param value
     **************************************************************************/
    public void setValue( int row, int col, double value );

    // ----------------

    /***************************************************************************
     * 
     **************************************************************************/
    public void transpose();

    /***************************************************************************
     * @param addend
     * @throws IllegalArgumentException
     **************************************************************************/
    public default void add( IMatrix addend ) throws IllegalArgumentException
    {
        this.add( addend, this );
    }

    /***************************************************************************
     * @param addend
     * @param sum
     * @throws IllegalArgumentException
     **************************************************************************/
    public default void add( IMatrix addend, IMatrix sum )
        throws IllegalArgumentException
    {
        if( addend.getRowCount() != getRowCount() )
        {
            String err = String.format(
                "Unable to add matrices; The addended row count %d must be equal to %d",
                addend.getRowCount(), getRowCount() );
            throw new IllegalArgumentException( err );
        }

        if( addend.getColumnCount() != getColumnCount() )
        {
            String err = String.format(
                "Unable to add matrices; The addended column count %d must be equal to %d",
                addend.getColumnCount(), getColumnCount() );
            throw new IllegalArgumentException( err );
        }

        if( sum.getRowCount() != getRowCount() )
        {
            String err = String.format(
                "Unable to add matrices; The sum row count %d must be equal to %d",
                sum.getRowCount(), getRowCount() );
            throw new IllegalArgumentException( err );
        }

        if( sum.getColumnCount() != getColumnCount() )
        {
            String err = String.format(
                "Unable to add matrices; The sum column count %d must be equal to %d",
                sum.getColumnCount(), getColumnCount() );
            throw new IllegalArgumentException( err );
        }

        for( int r = 0; r < addend.getRowCount(); r++ )
        {
            for( int c = 0; c < addend.getColumnCount(); c++ )
            {
                double v1 = getValue( r, c );
                double v2 = addend.getValue( r, c );

                sum.setValue( r, c, v1 + v2 );
            }
        }
    }

    /***************************************************************************
     * @param subtrahend
     * @throws IllegalArgumentException
     **************************************************************************/
    public default void sub( IMatrix subtrahend )
        throws IllegalArgumentException
    {
        this.sub( subtrahend, this );
    }

    /***************************************************************************
     * @param subtrahend
     * @param difference
     * @throws IllegalArgumentException
     **************************************************************************/
    public default void sub( IMatrix subtrahend, IMatrix difference )
        throws IllegalArgumentException
    {
        if( subtrahend.getRowCount() != getRowCount() )
        {
            String err = String.format(
                "Unable to add matrices; The subtrahend row count %d must be equal to %d",
                subtrahend.getRowCount(), getRowCount() );
            throw new IllegalArgumentException( err );
        }

        if( subtrahend.getColumnCount() != getColumnCount() )
        {
            String err = String.format(
                "Unable to add matrices; The subtrahend column count %d must be equal to %d",
                subtrahend.getColumnCount(), getColumnCount() );
            throw new IllegalArgumentException( err );
        }

        if( difference.getRowCount() != getRowCount() )
        {
            String err = String.format(
                "Unable to add matrices; The difference row count %d must be equal to %d",
                difference.getRowCount(), getRowCount() );
            throw new IllegalArgumentException( err );
        }

        if( difference.getColumnCount() != getColumnCount() )
        {
            String err = String.format(
                "Unable to add matrices; The difference column count %d must be equal to %d",
                difference.getColumnCount(), getColumnCount() );
            throw new IllegalArgumentException( err );
        }

        for( int r = 0; r < subtrahend.getRowCount(); r++ )
        {
            for( int c = 0; c < subtrahend.getColumnCount(); c++ )
            {
                double v1 = getValue( r, c );
                double v2 = subtrahend.getValue( r, c );

                difference.setValue( r, c, v1 - v2 );
            }
        }
    }

    /***************************************************************************
     * @param multiplicand
     * @param product
     * @throws IllegalArgumentException
     **************************************************************************/
    public default void dot( IMatrix multiplicand, IMatrix product )
        throws IllegalArgumentException
    {
        if( multiplicand.getRowCount() != getRowCount() )
        {
            String err = String.format(
                "Unable to dot matrices; The multiplicand row count %d must be equal to %d",
                multiplicand.getRowCount(), getRowCount() );
            throw new IllegalArgumentException( err );
        }

        if( multiplicand.getColumnCount() != getColumnCount() )
        {
            String err = String.format(
                "Unable to dot matrices; The multiplicand column count %d must be equal to %d",
                multiplicand.getColumnCount(), getColumnCount() );
            throw new IllegalArgumentException( err );
        }

        if( product.getRowCount() != getRowCount() )
        {
            String err = String.format(
                "Unable to dot matrices; The product row count %d must be equal to %d",
                product.getRowCount(), getRowCount() );
            throw new IllegalArgumentException( err );
        }

        if( product.getColumnCount() != getColumnCount() )
        {
            String err = String.format(
                "Unable to dot matrices; The product column count %d must be equal to %d",
                product.getColumnCount(), getColumnCount() );
            throw new IllegalArgumentException( err );
        }

        for( int r = 0; r < multiplicand.getRowCount(); r++ )
        {
            for( int c = 0; c < multiplicand.getColumnCount(); c++ )
            {
                double v1 = multiplicand.getValue( r, c );
                double v2 = getValue( r, c );

                product.setValue( r, c, v1 * v2 );
            }
        }
    }

    /***************************************************************************
     * @param s
     **************************************************************************/
    public default void scale( double s )
    {
        translate( ( d ) -> s * d );
    }

    /***************************************************************************
     * @param m the multiplicand.
     * @param p the product.
     * @throws IllegalArgumentException
     **************************************************************************/
    public default void mult( IMatrix m, IMatrix p )
        throws IllegalArgumentException
    {
        if( getColumnCount() != m.getRowCount() )
        {
            throw new IllegalArgumentException(
                "Columns in a, " + getColumnCount() +
                    ", do not equal rows in b, " + m.getRowCount() + "." );
        }

        int count = getColumnCount();

        for( int r = 0; r < getRowCount(); r++ )
        {
            for( int c = 0; c < m.getColumnCount(); c++ )
            {
                double sum = 0.0;

                for( int i = 0; i < count; i++ )
                {
                    sum += getValue( r, i ) * m.getValue( i, c );
                }

                p.setValue( r, c, sum );
            }
        }
    }

    /***************************************************************************
     * @param m the multiplicand.
     * @param p the product.
     * @return
     **************************************************************************/
    public default void crossProduct( IMatrix m, IMatrix p )
    {
        if( getRowCount() != 1 && getColumnCount() != 3 )
        {
            throw new IllegalArgumentException();
        }

        if( m.getRowCount() != 1 && m.getColumnCount() != 3 )
        {
            throw new IllegalArgumentException();
        }

        if( p.getRowCount() != 1 && p.getColumnCount() != 3 )
        {
            throw new IllegalArgumentException();
        }

        double v;

        // System.out.println( "a = " + a );
        // System.out.println( "b = " + b );

        v = getValue( 0, 1 ) * m.getValue( 0, 2 ) -
            getValue( 0, 2 ) * m.getValue( 0, 1 );
        p.setValue( 0, 0, v );

        v = -getValue( 0, 0 ) * m.getValue( 0, 2 ) +
            getValue( 0, 2 ) * m.getValue( 0, 0 );
        p.setValue( 0, 1, v );

        v = getValue( 0, 0 ) * m.getValue( 0, 1 ) -
            getValue( 0, 1 ) * m.getValue( 0, 0 );
        p.setValue( 0, 2, v );
    }

    /***************************************************************************
     * @throws IllegalArgumentException
     **************************************************************************/
    public default void fillIdentity() throws IllegalArgumentException
    {
        if( getRowCount() != getColumnCount() )
        {
            throw new IllegalArgumentException();
        }

        for( int r = 0; r < getRowCount(); r++ )
        {
            for( int c = 0; c < getColumnCount(); c++ )
            {
                if( r == c )
                {
                    setValue( r, c, 1.0 );
                }
                else
                {
                    setValue( r, c, 0.0 );
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public default void fillZeros()
    {
        fill( 0.0 );
    }

    /***************************************************************************
     * @param value
     **************************************************************************/
    private void fill( double value )
    {
        translate( ( d ) -> value );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public default double rss()
    {
        double sum = 0.0;
        double value;

        for( int r = 0; r < getRowCount(); r++ )
        {
            for( int c = 0; c < getColumnCount(); c++ )
            {
                value = getValue( r, c );
                sum += value * value;
            }
        }

        return Math.sqrt( sum );
    }

    /***************************************************************************
     * @param f
     **************************************************************************/
    public default void translate( Function<Double, Double> f )
    {
        for( int r = 0; r < getRowCount(); r++ )
        {
            for( int c = 0; c < getColumnCount(); c++ )
            {
                double v = getValue( r, c );
                double fofv = f.apply( v );
                setValue( r, c, fofv );
            }
        }
    }
}
