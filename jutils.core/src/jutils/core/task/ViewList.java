package jutils.core.task;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.border.LineBorder;

import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ViewList implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final JPanel itemsPanel;

    /***************************************************************************
     * 
     **************************************************************************/
    public ViewList()
    {
        this.itemsPanel = new JPanel();
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        itemsPanel.setLayout( new BoxLayout( itemsPanel, BoxLayout.Y_AXIS ) );

        JPanel panel = new OnlyVerticalScrollPanel( new BorderLayout() );

        JScrollPane scrollpane = new JScrollPane( panel );

        scrollpane.setBorder( new LineBorder( Color.gray, 1 ) );
        scrollpane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        scrollpane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );

        panel.add( itemsPanel, BorderLayout.NORTH );
        panel.add( Box.createHorizontalStrut( 0 ), BorderLayout.CENTER );

        return scrollpane;
    }

    /***************************************************************************
     * @param view
     **************************************************************************/
    public void addView( IView<? extends Component> view )
    {
        itemsPanel.add( view.getView() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getCount()
    {
        return itemsPanel.getComponentCount();
    }

    /***************************************************************************
     * @param view
     **************************************************************************/
    public void removeView( IView<? extends Component> view )
    {
        itemsPanel.remove( view.getView() );

        Container parent = itemsPanel.getParent();

        parent.invalidate();
        parent.validate();
        parent.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OnlyVerticalScrollPanel extends JPanel
        implements Scrollable
    {
        private static final long serialVersionUID = 9210703649026002790L;

        public OnlyVerticalScrollPanel( LayoutManager lm )
        {
            super( lm );
        }

        @Override
        public Dimension getPreferredScrollableViewportSize()
        {
            return ( getPreferredSize() );
        }

        @Override
        public int getScrollableUnitIncrement( Rectangle visibleRect,
            int orientation, int direction )
        {
            return ( 10 );
        }

        @Override
        public int getScrollableBlockIncrement( Rectangle visibleRect,
            int orientation, int direction )
        {
            return ( 100 );
        }

        @Override
        public boolean getScrollableTracksViewportWidth()
        {
            return ( true );
        }

        @Override
        public boolean getScrollableTracksViewportHeight()
        {
            return ( false );
        }
    }
}
