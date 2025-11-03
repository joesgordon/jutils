package jutils.kairosion.ui;

import java.time.LocalDateTime;

import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public interface ITimeField<T>
{
    /***************************************************************************
     * @param time
     **************************************************************************/
    public void setDateTime( LocalDateTime time );

    /***************************************************************************
     * @param time
     * @return
     **************************************************************************/
    public LocalDateTime updateDateTime( LocalDateTime time );

    /***************************************************************************
     * @return
     **************************************************************************/
    public IDataFormField<T> getField();

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void setUpdater( IUpdater<ITimeField<?>> updater );
}
