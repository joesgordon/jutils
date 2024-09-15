package jutils.math.ui;

import jutils.core.ValidationException;
import jutils.core.io.parsers.DoubleParser;
import jutils.core.ui.TabularView.ITabularModel;
import jutils.core.ui.TabularView.ITabularNotifier;
import jutils.math.IMatrix;
import jutils.math.Matrix;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MatrixModel implements ITabularModel
{
    /**  */
    private IMatrix matrix;
    /**  */
    private boolean editable;
    /**  */
    private ITabularNotifier notifier;

    /***************************************************************************
     * 
     **************************************************************************/
    public MatrixModel()
    {
        this.matrix = new Matrix( 1, 1 );
        this.editable = true;
        this.notifier = null;
    }

    /***************************************************************************
     * @param matrix
     **************************************************************************/
    public void setData( IMatrix matrix )
    {
        this.matrix = matrix;

        if( notifier != null )
        {
            notifier.fireStructureChanged();
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IMatrix getData()
    {
        return this.matrix;
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        this.editable = editable;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isEditable()
    {
        return this.editable;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void registerNotifier( ITabularNotifier notifier )
    {
        this.notifier = notifier;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getRowCount()
    {
        return this.matrix.getRowCount();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getRowName( int row )
    {
        return "" + row;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getColCount()
    {
        return this.matrix.getColumnCount();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getColName( int col )
    {
        return "" + col;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Class<?> getColClass( int col )
    {
        return Double.class;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Object getValue( int row, int col )
    {
        return matrix.getValue( row, col );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Object value, int row, int col )
    {
        Double v = null;

        if( value != null )
        {
            if( value instanceof String )
            {
                DoubleParser p = new DoubleParser();
                try
                {
                    v = p.parse( value.toString() );
                }
                catch( ValidationException ex )
                {
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                }
            }
            else if( value instanceof Number )
            {
                Number n = ( Number )value;
                v = n.doubleValue();
            }
        }

        matrix.setValue( row, col, v );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isCellEditable( int row, int col )
    {
        return this.editable;
    }
}
