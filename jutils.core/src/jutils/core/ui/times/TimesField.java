package jutils.core.ui.times;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.swing.JComponent;

import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class TimesField<T> implements IDataFormField<TimesUnion>
{
    /**  */
    private final IDataFormField<T> field;
    /**  */
    private final TimesUnion time;
    /**  */
    private Function<TimesUnion, T> getField;
    /**  */
    private BiConsumer<TimesUnion, T> timeSetter;
    /**  */
    private IUpdater<TimesUnion> updater;

    /***************************************************************************
     * @param field
     * @param time
     * @param getField
     * @param timeSetter
     **************************************************************************/
    public TimesField( IDataFormField<T> field, TimesUnion time,
        Function<TimesUnion, T> getField, BiConsumer<TimesUnion, T> timeSetter )
    {
        this.field = field;
        this.time = time;
        this.getField = getField;
        this.timeSetter = timeSetter;
        this.updater = null;

        field.setUpdater( ( d ) -> handleFieldUpdated( d ) );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void handleFieldUpdated( T data )
    {
        timeSetter.accept( time, data );

        if( updater != null )
        {
            updater.update( time );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public TimesUnion getValue()
    {
        return time;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( TimesUnion value )
    {
        time.set( value );
        field.setValue( getField.apply( time ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<TimesUnion> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<TimesUnion> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return field.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }
}
