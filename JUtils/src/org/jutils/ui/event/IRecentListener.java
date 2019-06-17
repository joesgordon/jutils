package org.jutils.ui.event;

/*******************************************************************************
 * Defines a callback when a recent item was selected and whether control was
 * pressed during the selection.
 * @param <T> the type of the item selected.
 ******************************************************************************/
public interface IRecentListener<T>
{
    /***************************************************************************
     * Callback invoked when the provided item is selected.
     * @param item the item selected.
     * @param ctrlPressed {@code true} if control was pressed during the
     * selection.
     **************************************************************************/
    public void selected( T item, boolean ctrlPressed );
}
