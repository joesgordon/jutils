package org.jutils.core.ui.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/*******************************************************************************
 * Defines a mouse listener with single callback because {@link MouseAdapter} is
 * dumb.
 ******************************************************************************/
public class MouseEventsListener extends MouseAdapter
{
    /** The function called upon any mouse event. */
    private final IMouseCallback callback;

    /***************************************************************************
     * Constructs a {@code MouseEventsListener}.
     * @param callback the function called upon any mouse event.
     **************************************************************************/
    public MouseEventsListener( IMouseCallback callback )
    {
        this.callback = callback;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public void mouseClicked( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.CLICKED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public void mousePressed( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.PRESSED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public void mouseReleased( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.RELEASED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public void mouseEntered( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.ENTERED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public void mouseExited( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.EXITED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public void mouseWheelMoved( MouseWheelEvent e )
    {
        callback.handleEvent( MouseEventType.WHEEL_MOVED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public void mouseDragged( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.DRAGGED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public void mouseMoved( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.MOVED, e );
    }

    /***************************************************************************
     * Defines all the functions in {@link MouseAdapter}.
     **************************************************************************/
    public static enum MouseEventType
    {
        /** An event from {@link MouseAdapter#mouseClicked(MouseEvent)}. */
        CLICKED,
        /** An event from {@link MouseAdapter#mousePressed(MouseEvent)}. */
        PRESSED,
        /** An event from {@link MouseAdapter#mouseReleased(MouseEvent)}. */
        RELEASED,
        /** An event from {@link MouseAdapter#mouseEntered(MouseEvent)}. */
        ENTERED,
        /** An event from {@link MouseAdapter#mouseExited(MouseEvent)}. */
        EXITED,
        /**
         * An event from {@link MouseAdapter#mouseWheelMoved(MouseWheelEvent)}.
         */
        WHEEL_MOVED,
        /** An event from {@link MouseAdapter#mouseDragged(MouseEvent)}. */
        DRAGGED,
        /** An event from {@link MouseAdapter#mouseMoved(MouseEvent)}. */
        MOVED;
    }

    /***************************************************************************
     * Defines a single function called upon any mouse event.
     **************************************************************************/
    public static interface IMouseCallback
    {
        /**
         * @param type the type of mouse event from a {@link MouseAdapter}.
         * @param event the event invoked.
         */
        public void handleEvent( MouseEventType type, MouseEvent event );
    }
}
