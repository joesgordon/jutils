package org.jutils.core.ui.event;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DoubleClickListener extends MouseEventsListener
{
    /***************************************************************************
     * @param callback function called on double-click
     **************************************************************************/
    public DoubleClickListener( Consumer<Point> callback )
    {
        super( ( t, e ) -> handleEvent( t, e, callback ) );
    }

    /***************************************************************************
     * @param type
     * @param event
     * @param callback
     **************************************************************************/
    private static void handleEvent( MouseEventType type, MouseEvent event,
        Consumer<Point> callback )
    {
        if( type == MouseEventType.CLICKED &&
            SwingUtilities.isLeftMouseButton( event ) &&
            event.getClickCount() == 2 )
        {
            callback.accept( event.getPoint() );
        }
    }
}
