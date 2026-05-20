package jutils.kairosion.ui;

import java.time.LocalDateTime;

import jutils.core.timestamps.ITimestamp;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class TimestampField<T extends ITimestamp> implements ITimeField<T>
{
    /**  */
    private final IDataFormField<T> field;

    /**  */
    private IUpdater<ITimeField<?>> onUpdate;

    /***************************************************************************
     * @param field
     * @param onUpdate
     * @param toData
     * @param toLdt
     **************************************************************************/
    public TimestampField( IDataFormField<T> field )
    {
        this.field = field;
        this.onUpdate = null;

        field.setUpdater( ( d ) -> handleDataUpdated( d ) );
    }

    /***************************************************************************
     * @param d
     **************************************************************************/
    private void handleDataUpdated( T d )
    {
        if( onUpdate != null )
        {
            onUpdate.update( this );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setDateTime( LocalDateTime time )
    {
        T data = field.getValue();

        if( data == null )
        {
            throw new NullPointerException(
                "No default value set for " + field.getClass() );
        }

        data.fromDateTime( time );
        field.setValue( data );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDateTime updateDateTime( LocalDateTime time )
    {
        return field.getValue().toDateTime();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IDataFormField<T> getField()
    {
        return field;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<ITimeField<?>> updater )
    {
        this.onUpdate = updater;
    }
}
