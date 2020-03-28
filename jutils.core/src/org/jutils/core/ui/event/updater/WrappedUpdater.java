package org.jutils.core.ui.event.updater;

/*******************************************************************************
 * 
 ******************************************************************************/
public class WrappedUpdater<T> implements IUpdater<T>
{
    /**  */
    private final IUpdater<T> baseUpdater;
    /**  */
    private final IUpdater<T> secUpdater;

    /***************************************************************************
     * @param baseUpdater
     * @param secUpdater
     **************************************************************************/
    public WrappedUpdater( IUpdater<T> baseUpdater, IUpdater<T> secUpdater )
    {
        this.baseUpdater = baseUpdater;
        this.secUpdater = secUpdater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void update( T data )
    {
        this.baseUpdater.update( data );
        this.secUpdater.update( data );
    }
}
