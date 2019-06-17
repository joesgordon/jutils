package org.jutils.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

import org.jutils.ui.event.TabRemovedListener;

/*******************************************************************************
 * A JTabbedPane which has a close ('X') icon on each tab. To add a tab, use the
 * method addTab(String, Component) To have an extra icon on each tab use the
 * method addTab(String, Component, Icon). Only clicking the 'X' closes the tab.
 ******************************************************************************/
public class ClosableTabbedPane extends JTabbedPane
{
    /**  */
    private static final long serialVersionUID = 7373834511145425414L;

    /***************************************************************************
     *
     **************************************************************************/
    public ClosableTabbedPane()
    {
        super();
        addMouseListener( new TabClickedListener() );
        addMouseMotionListener( new TabHoverListener() );
    }

    /***************************************************************************
     * @param title String
     * @param component Component
     **************************************************************************/
    @Override
    public void addTab( String title, Component component )
    {
        this.addTab( title, component, null );
    }

    /***************************************************************************
     * @param title String
     * @param component Component
     * @param labelIcon Icon
     **************************************************************************/
    public void addTab( String title, Component component, Icon labelIcon )
    {
        super.addTab( title, new CloseTabIcon( labelIcon ), component );
    }

    /***************************************************************************
     * @param listener TabRemovedListener
     **************************************************************************/
    public void addTabRemovedListener( TabRemovedListener listener )
    {
        listenerList.add( TabRemovedListener.class, listener );
    }

    /***************************************************************************
     * @param comp Component
     **************************************************************************/
    @Override
    public void remove( Component comp )
    {
        int index = this.indexOfComponent( comp );
        super.remove( comp );
        fireTabRemoved( comp, index );
    }

    /***************************************************************************
     * @param index int
     **************************************************************************/
    @Override
    public void removeTabAt( int index )
    {
        Component comp = getComponentAt( index );
        super.removeTabAt( index );
        fireTabRemoved( comp, index );
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public void removeAll()
    {
        super.removeAll();
    }

    /***************************************************************************
     * @param comp Component
     * @param index int
     **************************************************************************/
    protected void fireTabRemoved( Component comp, int index )
    {
        // Guaranteed to return a non-null array
        Object [] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for( int i = listeners.length - 2; i >= 0; i -= 2 )
        {
            if( listeners[i] == TabRemovedListener.class )
            {
                ( ( TabRemovedListener )listeners[i + 1] ).tabRemoved( comp,
                    index );
            }
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    class TabHoverListener extends MouseMotionAdapter
    {
        @Override
        public void mouseMoved( MouseEvent e )
        {
            int tabNumber = getUI().tabForCoordinate( ClosableTabbedPane.this,
                e.getX(), e.getY() );
            if( tabNumber < 0 )
            {
                return;
            }
            CloseTabIcon icon = ( CloseTabIcon )getIconAt( tabNumber );
            Rectangle rect = ( icon ).getBounds();
            if( rect.contains( e.getX(), e.getY() ) )
            {
                icon.setHovering( true );
                icon.repaint();
            }
            else
            {
                icon.setHovering( false );
                icon.repaint();
            }
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    class TabClickedListener extends MouseAdapter
    {
        @Override
        public void mouseClicked( MouseEvent e )
        {
            int tabNumber = getUI().tabForCoordinate( ClosableTabbedPane.this,
                e.getX(), e.getY() );
            if( tabNumber < 0 )
            {
                return;
            }
            Rectangle rect = ( ( CloseTabIcon )getIconAt(
                tabNumber ) ).getBounds();
            if( rect.contains( e.getX(), e.getY() ) )
            {
                // the tab is being closed
                removeTabAt( tabNumber );
            }
        }
    }
}

/*******************************************************************************
 * The class which generates the 'X' icon for the tabs. The constructor accepts
 * an icon which is extra to the 'X' icon. This value is null if no extra icon
 * is required.
 ******************************************************************************/
class CloseTabIcon implements Icon
{
    // public static final Color highlightColor = new Color( 58, 110, 165 );
    /**  */
    public static final Color highlightColor = Color.red;

    /**  */
    private int width;
    /**  */
    private int height;
    /**  */
    private Icon fileIcon;
    /**  */
    private boolean hovering = false;
    /**  */
    private Component c;
    /**  */
    private int x_pos;
    /**  */
    private int y_pos;

    /**
     * @param fileIcon
     */
    public CloseTabIcon( Icon fileIcon )
    {
        this.fileIcon = fileIcon;
        width = 16;
        height = 16;
    }

    /**
     * @param hovering
     */
    public void setHovering( boolean hovering )
    {
        this.hovering = hovering;
    }

    /**
     * 
     */
    public void repaint()
    {
        paintIcon( c, c.getGraphics(), x_pos, y_pos );
        // c.repaint();
        // LogUtils.printDebug( "Class: " + c.getClass().toString() );
    }

    /**
     * @return
     */
    public Rectangle getBounds()
    {
        return new Rectangle( x_pos, y_pos, width, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
        this.x_pos = x;
        this.y_pos = y;
        this.c = c;

        // LogUtils.printDebug( "painting..." );

        Color col = g.getColor();
        int y_p = y + 2;

        if( hovering )
        {
            g.setColor( Color.black );
        }
        else
        {
            g.setColor( c.getBackground() );
        }

        g.drawLine( x + 1, y_p, x + 12, y_p );
        g.drawLine( x + 1, y_p + 13, x + 12, y_p + 13 );
        g.drawLine( x, y_p + 1, x, y_p + 12 );
        g.drawLine( x + 13, y_p + 1, x + 13, y_p + 12 );

        if( hovering )
        {
            g.setColor( highlightColor );
        }
        else
        {
            g.setColor( Color.black );
        }

        g.drawLine( x + 3, y_p + 3, x + 10, y_p + 10 );
        g.drawLine( x + 3, y_p + 4, x + 9, y_p + 10 );
        g.drawLine( x + 4, y_p + 3, x + 10, y_p + 9 );
        g.drawLine( x + 10, y_p + 3, x + 3, y_p + 10 );
        g.drawLine( x + 10, y_p + 4, x + 4, y_p + 10 );
        g.drawLine( x + 9, y_p + 3, x + 3, y_p + 9 );
        g.setColor( col );
        if( fileIcon != null )
        {
            fileIcon.paintIcon( c, g, x + width, y_p );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIconWidth()
    {
        return width + ( fileIcon != null ? fileIcon.getIconWidth() : 0 );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIconHeight()
    {
        return height;
    }
}
