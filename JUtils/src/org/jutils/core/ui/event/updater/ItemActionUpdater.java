package org.jutils.core.ui.event.updater;

import org.jutils.core.ui.event.ItemActionEvent;
import org.jutils.core.ui.event.ItemActionListener;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ItemActionUpdater<T> implements ItemActionListener<T>
{
    /**  */
    private final IUpdater<T> updater;

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public ItemActionUpdater( IUpdater<T> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void actionPerformed( ItemActionEvent<T> event )
    {
        updater.update( event.getItem() );
    }
}
