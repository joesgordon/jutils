package jutils.core.ui.event.updater;

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

    /***************************************************************************
     * Creates a new updater that does nothing. Useful for setting instead of
     * {@code null} to guarantee non-null.
     * @param <T> type of data to be updated.
     * @return the new empty updater.
     **************************************************************************/
    public static <T> IUpdater<T> createEmptyUpdater()
    {
        return ( t ) -> {
        };
    }

    /***************************************************************************
     * Returns the provided updater if non-{@code null}; empty if {@code null}.
     * @param <T> type of data to be updated.
     * @param updater the updater to be validated.
     * @return a non-{@code null} updater.
     **************************************************************************/
    public static <T> IUpdater<T> validate( IUpdater<T> updater )
    {
        return updater != null ? updater : IUpdater.createEmptyUpdater();
    }
}
