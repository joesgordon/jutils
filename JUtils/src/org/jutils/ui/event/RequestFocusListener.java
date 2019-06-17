package org.jutils.ui.event;

import javax.swing.JComponent;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class RequestFocusListener implements AncestorListener
{
    public RequestFocusListener()
    {
    }

    @Override
    public void ancestorAdded( AncestorEvent e )
    {
        JComponent component = e.getComponent();

        component.requestFocusInWindow();

        component.removeAncestorListener( this );
    }

    @Override
    public void ancestorMoved( AncestorEvent e )
    {
    }

    @Override
    public void ancestorRemoved( AncestorEvent e )
    {
    }
}
