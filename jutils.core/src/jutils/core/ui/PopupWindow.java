package jutils.core.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PopupWindow
{
    /**  */
    private final JPanel view;

    /**  */
    private JPopupMenu popup;

    /***************************************************************************
     * @param resizable
     * @param comp
     **************************************************************************/
    public PopupWindow( JComponent comp )
    {
        this.view = createView( comp );

        this.popup = new JPopupMenu();

        popup.setBorder( new EmptyBorder( new Insets( 0, 0, 0, 0 ) ) );
        popup.add( view );

        view.setFocusable( true );

        setBorder( new ShadowBorder() );
    }

    /***************************************************************************
     * @param resizable
     * @param comp
     * @return
     **************************************************************************/
    private JPanel createView( JComponent comp )
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( comp, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @param parent
     * @param x
     * @param y
     **************************************************************************/
    public void show( Component parent, int x, int y )
    {
        Point pos = new Point( x, y );
        SwingUtilities.convertPointToScreen( pos, parent );
        view.revalidate();

        popup.setLocation( pos.x, pos.y );
        popup.setInvoker( parent );
        popup.setVisible( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void hide()
    {
        popup.setVisible( false );
    }

    /***************************************************************************
     * @param lineBorder
     **************************************************************************/
    public void setBorder( AbstractBorder lineBorder )
    {
        view.setBorder( lineBorder );
    }
}
