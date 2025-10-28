package jutils.kairosion.ui;

import java.time.LocalDateTime;
import java.util.function.Function;

import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class TimestampField<T> implements ITimestampField<T>
{
    /**  */
    private final IDataFormField<T> field;

    /**  */
    private final Function<LocalDateTime, T> toData;
    /**  */
    private final Function<T, LocalDateTime> toLdt;

    /**  */
    private LocalDateTime time;
    /**  */
    private IUpdater<ITimestampField<?>> onUpdate;

    /***************************************************************************
     * @param field
     * @param onUpdate
     * @param toData
     * @param toLdt
     **************************************************************************/
    public TimestampField( IDataFormField<T> field,
        Function<LocalDateTime, T> toData, Function<T, LocalDateTime> toLdt )
    {
        this.field = field;
        this.toData = toData;
        this.toLdt = toLdt;

        this.time = LocalDateTime.MIN;
        this.onUpdate = null;

        field.setUpdater( ( d ) -> handleDataUpdated( d ) );
    }

    /***********************************************************************
     * @param d
     **********************************************************************/
    private void handleDataUpdated( T d )
    {
        this.time = toLdt.apply( d );

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
        T data = toData.apply( time );
        field.setValue( data );
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
    public void setUpdater( IUpdater<ITimestampField<?>> updater )
    {
        this.onUpdate = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDateTime updateDateTime( T data )
    {
        // TODO Auto-generated method stub
        return null;
    }
}
