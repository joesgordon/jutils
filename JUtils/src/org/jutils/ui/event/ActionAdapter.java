package org.jutils.ui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/*******************************************************************************
 * Creates an action object that calls the provided action listener on
 * {@link Action#actionPerformed}
 ******************************************************************************/
public class ActionAdapter extends AbstractAction
{
    /**  */
    private static final long serialVersionUID = -6419344487512692565L;
    /**  */
    private final ActionListener listener;

    /***************************************************************************
     * @param listener
     * @param name
     * @param icon
     **************************************************************************/
    public ActionAdapter( ActionListener listener, String name, Icon icon )
    {
        super( name, icon );

        this.listener = listener;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void actionPerformed( ActionEvent e )
    {
        listener.actionPerformed( e );
    }
}
