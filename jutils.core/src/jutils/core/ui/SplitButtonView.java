package jutils.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import jutils.core.ui.event.IRecentListener;
import jutils.core.ui.model.IView;
import jutils.core.ui.model.ItemsListModel;

/*******************************************************************************
 * Defines a button with a drop-down menu.
 * @param <T> the type of item to be shown in the drop-down menu.
 ******************************************************************************/
public class SplitButtonView<T> implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JButton button;
    /**  */
    private final JButton arrowButton;
    /**  */
    private final ItemsListPopup<T> popup;
    /**  */
    private final List<IRecentListener<T>> selectedListeners;

    /***************************************************************************
     * @param text
     **************************************************************************/
    public SplitButtonView( String text )
    {
        this( text, null, new ArrayList<>() );
    }

    /***************************************************************************
     * @param text
     * @param icon
     * @param items
     **************************************************************************/
    public SplitButtonView( String text, Icon icon, List<T> items )
    {
        this( text, icon, items, new DefaultListItemModel<>() );
    }

    /***************************************************************************
     * @param text
     * @param icon
     * @param items
     * @param descriptor
     **************************************************************************/
    public SplitButtonView( String text, Icon icon, List<T> items,
        ItemsListModel<T> descriptor )
    {
        this.button = new JButton( text, icon );
        this.arrowButton = new JButton( new ArrowIcon() );
        this.popup = new ItemsListPopup<>( items, descriptor );
        this.view = createView();
        this.selectedListeners = new ArrayList<>();

        arrowButton.addActionListener( ( e ) -> togglePopup() );
        // arrowButton.addMouseListener( new ArrowButtonMouseListener( this ) );

        popup.addItemSelectedListener( ( i, c ) -> fireItemSelected( i, c ) );

        setData( items );

        Dimension dim;

        dim = button.getPreferredSize();

        arrowButton.setPreferredSize( new Dimension( 15, dim.height ) );
        arrowButton.setMinimumSize( arrowButton.getPreferredSize() );
        arrowButton.setMaximumSize( arrowButton.getPreferredSize() );

        view.setMaximumSize( view.getPreferredSize() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void togglePopup()
    {
        popup.show( arrowButton );
    }

    /***************************************************************************
     * @param item
     * @param ctrlPressed
     **************************************************************************/
    private void fireItemSelected( T item, boolean ctrlPressed )
    {
        popup.hide();

        // LogUtils.printDebug( "%s selected", item );
        SwingUtilities.invokeLater( () -> fireListeners( item, ctrlPressed ) );
    }

    /***************************************************************************
     * @param item
     * @param ctrlPressed
     **************************************************************************/
    private void fireListeners( T item, boolean ctrlPressed )
    {
        for( IRecentListener<T> irl : selectedListeners )
        {
            irl.selected( item, ctrlPressed );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        // comboField.setPreferredSize( new Dimension( 15, dim.height ) );
        // comboField.setMaximumSize( comboField.getPreferredSize() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( button, constraints );

        // constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
        // GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
        // new Insets( 0, 0, 0, 0 ), 0, 0 );
        // panel.add( comboField, constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( arrowButton, constraints );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @param toolbar
     **************************************************************************/
    public void install( JToolBar toolbar )
    {
        toolbar.add( button );
        // toolbar.add( comboField );
        toolbar.add( arrowButton );

        button.setFocusable( false );
        arrowButton.setFocusable( false );

        Dimension dim;

        dim = button.getPreferredSize();

        arrowButton.setPreferredSize( new Dimension( 15, dim.height ) );
        arrowButton.setMinimumSize( arrowButton.getPreferredSize() );
        arrowButton.setMaximumSize( arrowButton.getPreferredSize() );
    }

    /***************************************************************************
     * @param selectedListener
     **************************************************************************/
    public void addItemSelected( IRecentListener<T> selectedListener )
    {
        selectedListeners.add( selectedListener );
    }

    /***************************************************************************
     * @param listener
     **************************************************************************/
    public void addButtonListener( ActionListener listener )
    {
        button.addActionListener( listener );
    }

    /***************************************************************************
     * @param listener
     **************************************************************************/
    public void addRightClickListener( IListRightClickListener<T> listener )
    {
        popup.addRightClickListener( listener );
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public void setData( List<T> items )
    {
        popup.setItems( items );
    }

    /***************************************************************************
     * @param icon
     **************************************************************************/
    public void setIcon( Icon icon )
    {
        button.setIcon( icon );
    }

    /***************************************************************************
     * @param target
     **************************************************************************/
    public void setDropTarget( DropTarget target )
    {
        button.setDropTarget( target );
    }

    /***************************************************************************
     * @param tooltip
     **************************************************************************/
    public void setButtonTooltip( String tooltip )
    {
        button.setToolTipText( tooltip );
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setButtonText( String text )
    {
        button.setText( text );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void hidePopup()
    {
        popup.hide();
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        button.setEnabled( enabled );
        arrowButton.setEnabled( enabled );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ArrowIcon implements Icon
    {
        /**  */
        private final Color shadow;
        /**  */
        private final Color highlight;
        /**  */
        private final Color darkShadow;

        /**  */
        private static final int SIZE = 5;

        /**
         * 
         */
        public ArrowIcon()
        {
            this.shadow = UIManager.getColor( "controlShadow" );
            this.darkShadow = Color.black;
            // this.darkShadow = UIManager.getColor( "controlDkShadow" );
            this.highlight = UIManager.getColor( "controlLtHighlight" );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void paintIcon( Component c, Graphics g, int x, int y )
        {
            int x1 = 1 + c.getWidth() / 2 - SIZE / 2;
            int y1 = c.getHeight() / 2 - SIZE / 2;

            paintTriangle( g, x1, y1, SIZE, true );
        }

        /**
         * @param g
         * @param x
         * @param y
         * @param size
         * @param isEnabled
         */
        public void paintTriangle( Graphics g, int x, int y, int size,
            boolean isEnabled )
        {
            Color oldColor = g.getColor();
            int mid, i, j;

            j = 0;
            size = Math.max( size, 2 );
            mid = ( size / 2 ) - 1;

            g.translate( x, y );
            if( isEnabled )
            {
                g.setColor( darkShadow );
            }
            else
            {
                g.setColor( shadow );
            }

            if( !isEnabled )
            {
                g.translate( 1, 1 );
                g.setColor( highlight );
                for( i = size - 1; i >= 0; i-- )
                {
                    g.drawLine( mid - i, j, mid + i, j );
                    j++;
                }
                g.translate( -1, -1 );
                g.setColor( shadow );
            }

            j = 0;
            for( i = size - 1; i >= 0; i-- )
            {
                g.drawLine( mid - i, j, mid + i, j );
                j++;
            }

            g.translate( -x, -y );
            g.setColor( oldColor );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIconWidth()
        {
            return SIZE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIconHeight()
        {
            return SIZE;
        }
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static interface IListRightClickListener<T>
    {
        /**
         * @param item
         * @param c
         * @param x
         * @param y
         */
        public void rightClicked( T item, Component c, int x, int y );
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static class DefaultListItemModel<T> implements ItemsListModel<T>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getName( T item )
        {
            return item.toString();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTooltip( T item )
        {
            return "";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Icon getIcon( T item )
        {
            return null;
        }
    }
}
