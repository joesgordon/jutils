package org.jutils.ui.validation;

import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.event.updater.IUpdater;

public class UpdaterItemListener<T> implements ItemActionListener<T>
{
    private final IUpdater<T> updater;

    public UpdaterItemListener( IUpdater<T> updater )
    {
        this.updater = updater;
    }

    @Override
    public void actionPerformed( ItemActionEvent<T> event )
    {
        updater.update( event.getItem() );
    }
}
