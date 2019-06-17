package org.jutils.ui.event;

/*******************************************************************************
 * Listener for returning an item upon a given event.
 * @param <T> the type of the item to be returned during an event.
 ******************************************************************************/
public interface ItemActionListener<T>
{
    /***************************************************************************
     * Called when an event occurs.
     * @param event the event contained the item to be delivered.
     **************************************************************************/
    public void actionPerformed( ItemActionEvent<T> event );
}
