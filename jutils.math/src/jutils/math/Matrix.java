package jutils.math;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Matrix implements IMatrix
{
    /**  */
    private final DimensionalIndexer indexer;
    /**  */
    private final double [] vals;

    /***************************************************************************
     * @param rows
     * @param cols
     **************************************************************************/
    public Matrix( int rows, int cols )
    {
        if( rows == 0 )
        {
            throw new IllegalArgumentException(
                "Matrix cannot have zero rows." );
        }

        if( cols == 0 )
        {
            throw new IllegalArgumentException(
                "Matrix cannot have zero columns." );
        }

        this.indexer = new DimensionalIndexer( rows, cols );
        this.vals = new double[indexer.getSize()];
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getRowCount()
    {
        return indexer.getSize( 0 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getColumnCount()
    {
        return indexer.getSize( 1 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public double getValue( int row, int col )
    {
        int idx = indexer.toIndex( row, col );
        return vals[idx];
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( int row, int col, double value )
    {
        int idx = indexer.toIndex( row, col );
        vals[idx] = value;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public double getValue( int index )
    {
        int cols = getColumnCount();
        int row = index / cols;
        int col = index % cols;
        return getValue( row, col );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( int index, double value )
    {
        int cols = getColumnCount();
        int row = index / cols;
        int col = index % cols;
        setValue( row, col, value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void transpose()
    {
        indexer.transpose();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder();

        b.append( "[ " );

        for( int r = 0; r < getRowCount(); r++ )
        {
            for( int c = 0; c < getColumnCount(); c++ )
            {
                b.append( getValue( r, c ) );
                if( c < getColumnCount() - 1 )
                {
                    b.append( ' ' );
                }
            }
            if( r < getRowCount() - 1 )
            {
                b.append( ", " );
            }
        }

        b.append( " ]" );

        return b.toString();
    }
}
