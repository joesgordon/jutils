package jutils.kairosion.ui;

import java.time.LocalDateTime;

import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public interface ITimestampField<T>
{
    /***************************************************************************
     * @param time
     **************************************************************************/
    public void setDateTime( LocalDateTime time );

    /***************************************************************************
     * @param data
     * @return
     **************************************************************************/
    public LocalDateTime updateDateTime( T data );

    /***************************************************************************
     * @return
     **************************************************************************/
    public IDataFormField<T> getField();

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void setUpdater( IUpdater<ITimestampField<?>> updater );
}
