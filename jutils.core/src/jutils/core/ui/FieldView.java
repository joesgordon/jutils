package jutils.core.ui;

import java.awt.Component;

import javax.swing.JComponent;

import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * @param <D>
 ******************************************************************************/
public class FieldView<D> implements IDataView<D>
{
    /**  */
    private final JComponent view;
    /**  */
    private final IDataFormField<D> field;

    /**  */
    private D data;

    /***************************************************************************
     * @param field
     **************************************************************************/
    public FieldView( IDataFormField<D> field )
    {
        this.field = field;
        this.view = createView();
        this.data = null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        StandardFormView form = new StandardFormView();

        form.addField( field );

        return form.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public D getData()
    {
        return data;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( D data )
    {
        this.data = data;

        field.setValue( data );
    }
}
