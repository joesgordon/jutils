package jutils.core.ui.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import jutils.core.INamedItem;

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
    @Override
    public void mouseClicked( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.CLICKED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void mousePressed( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.PRESSED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void mouseReleased( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.RELEASED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void mouseEntered( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.ENTERED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void mouseExited( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.EXITED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void mouseWheelMoved( MouseWheelEvent e )
    {
        callback.handleEvent( MouseEventType.WHEEL_MOVED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void mouseDragged( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.DRAGGED, e );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void mouseMoved( MouseEvent e )
    {
        callback.handleEvent( MouseEventType.MOVED, e );
    }

    /***************************************************************************
     * Defines all the functions in {@link MouseAdapter}.
     **************************************************************************/
    public static enum MouseEventType implements INamedItem
    {
        /** An event from {@link MouseAdapter#mouseClicked(MouseEvent)}. */
        CLICKED( "Clicked" ),
        /** An event from {@link MouseAdapter#mousePressed(MouseEvent)}. */
        PRESSED( "Pressed" ),
        /** An event from {@link MouseAdapter#mouseReleased(MouseEvent)}. */
        RELEASED( "Released" ),
        /** An event from {@link MouseAdapter#mouseEntered(MouseEvent)}. */
        ENTERED( "Entered" ),
        /** An event from {@link MouseAdapter#mouseExited(MouseEvent)}. */
        EXITED( "Exited" ),
        /**
         * An event from {@link MouseAdapter#mouseWheelMoved(MouseWheelEvent)}.
         */
        WHEEL_MOVED( "Wheel Moved" ),
        /** An event from {@link MouseAdapter#mouseDragged(MouseEvent)}. */
        DRAGGED( "Dragged" ),
        /** An event from {@link MouseAdapter#mouseMoved(MouseEvent)}. */
        MOVED( "Moved" );

        /**  */
        private final String name;

        /**
         * @param name
         */
        private MouseEventType( String name )
        {
            this.name = name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return name;
        }
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
