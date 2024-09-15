package jutils.core.ui.validation;

import jutils.core.ui.event.ItemActionEvent;
import jutils.core.ui.event.ItemActionListener;
import jutils.core.ui.event.updater.IUpdater;

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
