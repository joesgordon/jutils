package org.jutils.ui;

import javax.swing.JComponent;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.IDataFormField;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

/*******************************************************************************
 * @param <F>
 * @param <T>
 ******************************************************************************/
public class ConvField<F, T> implements IDataFormField<T>
{
    /**  */
    private final IDataFormField<F> field;
    /**  */
    public final IConverter<F, T> conv;
    /**  */
    private final EnablableUpdater<T> updater;

    /**  */
    private T data;

    /***************************************************************************
     * @param field
     * @param conv
     **************************************************************************/
    public ConvField( IDataFormField<F> field, IConverter<F, T> conv )
    {
        this.field = field;
        this.conv = conv;

        this.updater = new EnablableUpdater<>();

        field.setUpdater( new FieldUpdater<F, T>( this ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T getValue()
    {
        return data;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( T value )
    {
        this.data = value;

        field.setValue( conv.convTo( value, field.getValue() ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<T> updater )
    {
        this.updater.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<T> getUpdater()
    {
        return this.updater.getUpdater();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return field.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setUpdaterEnabled( boolean enabled )
    {
        updater.setEnabled( enabled );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FieldUpdater<F, T> implements IUpdater<F>
    {
        private final ConvField<F, T> field;

        public FieldUpdater( ConvField<F, T> field )
        {
            this.field = field;
        }

        @Override
        public void update( F from )
        {
            field.data = field.conv.convFrom( from, field.data );
            field.updater.update( field.data );
        }
    }
}
