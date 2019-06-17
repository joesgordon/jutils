package org.jutils.ui.event;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/*******************************************************************************
 * This class is useful for allowing some mouse events to go through a glass
 * pane and interact with components below.
 ******************************************************************************/
public class GlassPaneMouseListener extends MouseInputAdapter
{
    private Container underPane;
    private Component glassPane;
    private List<Component> liveComponents;

    /***************************************************************************
     * Constructor
     * @param underPane the component under the glass pane
     * @param glassPane the glass pane itself
     **************************************************************************/
    public GlassPaneMouseListener( Container underPane, Component glassPane )
    {
        this.underPane = underPane;
        this.glassPane = glassPane;
        this.liveComponents = new Vector<Component>();
    }

    /***************************************************************************
     * Adds a "live" component to this listener. When a mouse event occurs on a
     * live component, it is forwarded through the glass pane. All other events
     * are trapped and discarded.
     * @param c
     **************************************************************************/
    public void addLiveComponent( Component c )
    {
        liveComponents.add( c );
    }

    /***************************************************************************
     * Removes a "live" component from this listener.
     * @param c
     **************************************************************************/
    public void removeLiveComponent( Component c )
    {
        liveComponents.remove( c );
    }

    /***************************************************************************
     * This method determines which mouse events should get forwarded through
     * the glass pane.
     * @param e
     **************************************************************************/
    private void dispatchMouseEvent( MouseEvent e )
    {
        Point glassPanePoint = e.getPoint();
        Point containerPoint = SwingUtilities.convertPoint( glassPane,
            glassPanePoint, underPane );
        if( containerPoint.y >= 0 )
        {
            // find out which component the mouse is over
            Component component = SwingUtilities.getDeepestComponentAt(
                underPane, containerPoint.x, containerPoint.y );

            if( ( component != null ) &&
                ( liveComponents.contains( component ) ) )
            {
                // forward event to component
                Point componentPoint = SwingUtilities.convertPoint( glassPane,
                    glassPanePoint, component );
                component.dispatchEvent( new MouseEvent( component, e.getID(),
                    e.getWhen(), e.getModifiersEx(), componentPoint.x,
                    componentPoint.y, e.getClickCount(),
                    SwingUtilities.isRightMouseButton( e ) ) );
            }
        }

        glassPane.repaint();
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved( MouseEvent e )
    {
        dispatchMouseEvent( e );
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged( MouseEvent e )
    {
        dispatchMouseEvent( e );
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked( MouseEvent e )
    {
        dispatchMouseEvent( e );
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered( MouseEvent e )
    {
        dispatchMouseEvent( e );
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited( MouseEvent e )
    {
        dispatchMouseEvent( e );
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed( MouseEvent e )
    {
        dispatchMouseEvent( e );
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased( MouseEvent e )
    {
        dispatchMouseEvent( e );
    }
}
