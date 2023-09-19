package jutils.core.ui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/*******************************************************************************
 * Defines a list of listeners and provides methods to manage and fire them.
 ******************************************************************************/
public class ActionListenerList
{
    /** The list of listeners. */
    private LinkedList<ActionListener> listeners;

    /***************************************************************************
     * Creates a new empty list of listeners.
     **************************************************************************/
    public ActionListenerList()
    {
        listeners = new LinkedList<ActionListener>();
    }

    /***************************************************************************
     * Adds the listener to the beginning of the list so it is fired first.
     * @param listener the listener to be added.
     **************************************************************************/
    public void addListener( ActionListener listener )
    {
        listeners.addFirst( listener );
    }

    /***************************************************************************
     * Removes the provided listener.
     * @param listener the listener to be removed.
     **************************************************************************/
    public void removeListener( ActionListener listener )
    {
        listeners.remove( listener );
    }

    /***************************************************************************
     * Calls each listener in the reverse order in which they were added.
     * @param source the object on which the Event initially occurred; see
     * {@link ActionEvent#getSource()}.
     * @param id the event type; see {@link ActionEvent#getID()}.
     * @param command the command string associated with this action; see
     * {@link ActionEvent#getActionCommand()}.
     **************************************************************************/
    public void fireListeners( Object source, int id, String command )
    {
        ActionEvent evt = new ActionEvent( source, id, command );

        fireListeners( evt );
    }

    /***************************************************************************
     * Calls each listener in the reverse order in which they were added.
     * @param source the object on which the Event initially occurred; see
     * {@link ActionEvent#getSource()}.
     * @param id the event type; see {@link ActionEvent#getID()}.
     * @param command the command string associated with this action; see
     * {@link ActionEvent#getActionCommand()}.
     * @param modifiers the modifier keys held down during this action event;
     * see {@link ActionEvent#getModifiers()}.
     **************************************************************************/
    public void fireListeners( Object source, int id, String command,
        int modifiers )
    {
        ActionEvent evt = new ActionEvent( source, id, command, modifiers );

        fireListeners( evt );
    }

    /***************************************************************************
     * Calls each listener in the reverse order in which they were added.
     * @param evt the event to be consumed by each listener.
     **************************************************************************/
    public void fireListeners( ActionEvent evt )
    {
        for( ActionListener l : listeners )
        {
            l.actionPerformed( evt );
        }
    }

    /***************************************************************************
     * @return the number of listeners fired upon
     * {@link #fireListeners(ActionEvent)}.
     **************************************************************************/
    public int size()
    {
        return listeners.size();
    }

    /***************************************************************************
     * Removes all listeners from this list.
     **************************************************************************/
    public void removeAll()
    {
        listeners.clear();
    }
}
