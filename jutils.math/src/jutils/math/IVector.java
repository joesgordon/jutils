package jutils.math;

/*******************************************************************************
 * 
 ******************************************************************************/
public abstract class IVector implements IMatrix
{
    /**  */
    private boolean isRow;

    /***************************************************************************
     * 
     **************************************************************************/
    protected IVector()
    {
        this.isRow = true;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public abstract int getLength();

    /***************************************************************************
     * {@inheritDoc}}
     **************************************************************************/
    @Override
    public final int getRowCount()
    {
        return isRow ? 1 : getLength();
    }

    /***************************************************************************
     * {@inheritDoc}}
     **************************************************************************/
    @Override
    public final int getColumnCount()
    {
        return isRow ? getLength() : 1;
    }

    /***************************************************************************
     * {@inheritDoc}}
     **************************************************************************/
    @Override
    public final double getValue( int row, int col )
    {
        int index = rowColToIndex( row, col );

        return getValue( index );
    }

    /***************************************************************************
     * {@inheritDoc}}
     **************************************************************************/
    @Override
    public final void setValue( int row, int col, double value )
        throws IllegalArgumentException
    {
        int index = rowColToIndex( row, col );

        setValue( index, value );
    }

    /***************************************************************************
     * @param row
     * @param col
     * @return
     * @throws IllegalArgumentException
     **************************************************************************/
    protected final int rowColToIndex( int row, int col )
        throws IllegalArgumentException
    {
        // because one of the terms is zero
        int index = ( row * col ) + row + col;

        if( index < 0 || index > getLength() )
        {
            String err = String.format( "Invalid row/col, %d/%d", row, col );
            throw new IllegalArgumentException( err );
        }

        return index;
    }

    /***************************************************************************
     * @param rows
     * @param cols
     **************************************************************************/
    protected final void setOrientation( int rows, int cols )
    {
        if( rows == 1 )
        {
            this.isRow = true;
        }
        else if( cols == 1 )
        {
            this.isRow = false;
        }
        else
        {
            String err = String.format( "Invalid row/col, %d/%d", rows, cols );
            throw new IllegalArgumentException( err );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public final boolean getOrientation()
    {
        return isRow;
    }

    /***************************************************************************
     * @param isRow
     **************************************************************************/
    public final void setOrientation( boolean isRow )
    {
        this.isRow = isRow;
    }

    /***************************************************************************
     * {@inheritDoc}}
     **************************************************************************/
    @Override
    public final void transpose()
    {
        isRow = !isRow;
    }
}
