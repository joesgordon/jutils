package jutils.core.ui.event;

import java.awt.Point;
import java.awt.event.MouseEvent;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MouseClickListener extends MouseEventsListener
{
    /***************************************************************************
     * @param isPrimary
     * @param handler
     **************************************************************************/
    public MouseClickListener( IClickHandler handler )
    {
        super( ( met, evt ) -> handleClick( handler, met, evt ) );
    }

    /***************************************************************************
     * @param handler
     * @param type
     * @param event
     **************************************************************************/
    private static void handleClick( IClickHandler handler, MouseEventType type,
        MouseEvent event )
    {
        if( type != MouseEventType.CLICKED )
        {
            return;
        }

        int button = event.getButton();
        boolean isLr = button == MouseEvent.BUTTON1 ||
            button == MouseEvent.BUTTON2;
        boolean notPopupButton = !event.isPopupTrigger();
        boolean primaryClicked = notPopupButton && isLr;
        int count = event.getClickCount();
        Point point = event.getPoint();
        int modifiers = event.getModifiersEx();

        handler.handleClick( primaryClicked, count, point, modifiers );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IClickHandler
    {
        /**
         * @param primaryClicked
         * @param count
         * @param point
         * @param modifiers
         */
        public void handleClick( boolean primaryClicked, int count, Point point,
            int modifiers );
    }
}
