package jutils.math.ui;

import java.awt.Component;

import jutils.core.ui.TabularView;
import jutils.core.ui.model.IDataView;
import jutils.math.IMatrix;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MatrixView implements IDataView<IMatrix>
{
    /**  */
    private final MatrixModel model;
    /**  */
    private final TabularView view;

    /***************************************************************************
     * 
     **************************************************************************/
    public MatrixView()
    {
        this.model = new MatrixModel();
        this.view = new TabularView();

        view.setModel( model );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IMatrix getData()
    {
        return model.getData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( IMatrix data )
    {
        model.setData( data );
    }
}
