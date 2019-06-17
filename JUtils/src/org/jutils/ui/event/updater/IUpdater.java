package org.jutils.ui.event.updater;

/*******************************************************************************
 * Defines a method of updating an object with new data. Basically a fancy-named
 * setter.
 * @param <T> the type of data to be updated.
 ******************************************************************************/
public interface IUpdater<T>
{
    /***************************************************************************
     * Updates the object with the provided data.
     * @param data the latest data.
     **************************************************************************/
    public void update( T data );
}
