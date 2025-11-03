package jutils.kairosion.ui;

import java.time.LocalDateTime;

import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LocalDateTimeField implements ITimeField<LocalDateTime>
{
    /**  */
    private final IDataFormField<LocalDateTime> field;
    /**  */
    private IUpdater<ITimeField<?>> onUpdate;

    /***************************************************************************
     * @param field
     **************************************************************************/
    public LocalDateTimeField( IDataFormField<LocalDateTime> field )
    {
        this.field = field;
        this.onUpdate = null;

        field.setUpdater( ( d ) -> handleDataUpdated( d ) );
    }

    /***************************************************************************
     * @param d
     **************************************************************************/
    private void handleDataUpdated( LocalDateTime d )
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
        field.setValue( time );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDateTime updateDateTime( LocalDateTime time )
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IDataFormField<LocalDateTime> getField()
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
