package org.jutils.ui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/***************************************************************************
 * 
 **************************************************************************/
public class ActionListenerList
{
    /**  */
    private LinkedList<ActionListener> listeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public ActionListenerList()
    {
        listeners = new LinkedList<ActionListener>();
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addListener( ActionListener l )
    {
        listeners.addFirst( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void removeListener( ActionListener l )
    {
        listeners.remove( l );
    }

    /***************************************************************************
     * @param source
     * @param id
     * @param command
     **************************************************************************/
    public void fireListeners( Object source, int id, String command )
    {
        ActionEvent evt = new ActionEvent( source, id, command );

        fireListeners( evt );
    }

    /***************************************************************************
     * @param source
     * @param id
     * @param command
     * @param modifiers
     **************************************************************************/
    public void fireListeners( Object source, int id, String command,
        int modifiers )
    {
        ActionEvent evt = new ActionEvent( source, id, command, modifiers );

        fireListeners( evt );
    }

    /***************************************************************************
     * @param evt
     **************************************************************************/
    public void fireListeners( ActionEvent evt )
    {
        for( ActionListener l : listeners )
        {
            l.actionPerformed( evt );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int size()
    {
        return listeners.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void removeAll()
    {
        listeners.clear();
    }
}
