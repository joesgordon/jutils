package org.jutils.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import org.jutils.core.SwingUtils;
import org.jutils.core.data.UIProperty;
import org.jutils.core.ui.event.IRecentListener;
import org.jutils.core.ui.model.CollectionListModel;
import org.jutils.core.ui.model.IView;
import org.jutils.core.ui.model.LabelListCellRenderer;
import org.jutils.core.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

/*******************************************************************************
 * @param <T>
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
    private final ListPopup<T> popup;
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
        IListItemModel<T> descriptor )
    {
        this.button = new JButton( text, icon );
        this.arrowButton = new JButton( new ArrowIcon() );
        this.popup = new ListPopup<>( items, descriptor );
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
     * 
     **************************************************************************/
    public void hidePopup()
    {
        popup.hide();
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
     * 
     **************************************************************************/
    private static final class ListPopup<T>
    {
        /**  */
        private final PopupWindow popup;
        /**  */
        private final CollectionListModel<T> model;
        /**  */
        private final JList<T> list;
        /**  */
        private final List<IRecentListener<T>> selectedListeners;
        /**  */
        private final List<IListRightClickListener<T>> rightClickListeners;

        /**
         * @param items
         * @param descriptor
         */
        public ListPopup( List<T> items, IListItemModel<T> descriptor )
        {
            this.model = new CollectionListModel<>();
            this.list = new JList<>( model );
            this.selectedListeners = new ArrayList<>();
            this.rightClickListeners = new ArrayList<>();
            this.popup = new PopupWindow( true, list );

            model.setData( items );

            // list.setFocusable( false );
            SwingUtils.addKeyListener( list, "ENTER",
                ( e ) -> fireSelected( list.getSelectedValue(), false ),
                "List Enter Pressed", false );
            list.setBackground( UIProperty.PANEL_BACKGROUND.getColor() );
            list.setVisibleRowCount( 10 );

            ListMouseListener<T> lml = new ListMouseListener<>( this );

            list.addMouseListener( lml );
            list.addMouseMotionListener( lml );
            list.setCellRenderer( new LabelListCellRenderer<T>(
                new DescriptorListCellLabelDecorator<T>( descriptor ) ) );
            list.setFixedCellHeight( 24 );

            // JScrollPane pane = new JScrollPane( list );
            //
            // pane.setHorizontalScrollBarPolicy(
            // ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
            // pane.getVerticalScrollBar().setUnitIncrement( 12 );
            // pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

            // popup.setPreferredSize( dim );

            popup.setBorder( new LineBorder( Color.black ) );
        }

        /**
         * @param listener
         */
        public void addRightClickListener( IListRightClickListener<T> listener )
        {
            rightClickListeners.add( listener );
        }

        /**
         * 
         */
        public void hide()
        {
            // LogUtils.printDebug( "Hiding" );
            popup.hide();
        }

        /**
         * @param l
         */
        public void addItemSelectedListener( IRecentListener<T> l )
        {
            selectedListeners.add( l );
        }

        /**
         * @param items
         */
        public void setItems( List<T> items )
        {
            model.setData( items );
        }

        /**
         * @param parent
         */
        public void show( JComponent parent )
        {
            if( model.getSize() > 0 )
            {
                list.setSelectedValue( model.get( 0 ), true );
            }
            list.clearSelection();
            popup.show( parent, 0, parent.getHeight() );
            list.requestFocus();
        }

        /**
         * @param item
         * @param ctrlPressed
         */
        public void fireSelected( T item, boolean ctrlPressed )
        {
            for( IRecentListener<T> irl : selectedListeners )
            {
                irl.selected( item, ctrlPressed );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ListMouseListener<T> extends MouseAdapter
    {
        /**  */
        private final ListPopup<T> popup;

        /**
         * @param popup
         */
        public ListMouseListener( ListPopup<T> popup )
        {
            this.popup = popup;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mousePressed( MouseEvent e )
        {
            int modifiers = e.getModifiersEx();
            boolean ctrlPressed = ( ActionEvent.CTRL_MASK &
                modifiers ) == ActionEvent.CTRL_MASK;

            @SuppressWarnings( "unchecked")
            JList<T> list = ( JList<T> )e.getSource();
            if( e.getClickCount() == 1 )
            {
                Point ept = e.getPoint();
                int index = list.locationToIndex( ept );

                if( index > -1 )
                {
                    Rectangle rect = list.getCellBounds( index, index );

                    if( rect.contains( ept ) )
                    {
                        T item = list.getModel().getElementAt( index );

                        if( SwingUtilities.isLeftMouseButton( e ) )
                        {
                            popup.fireSelected( item, ctrlPressed );
                        }
                        else if( SwingUtilities.isRightMouseButton( e ) )
                        {
                            Component c = e.getComponent();
                            int x = e.getX();
                            int y = e.getY();
                            popup.rightClickListeners.forEach(
                                ( l ) -> l.rightClicked( item, c, x, y ) );
                        }
                    }
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseMoved( MouseEvent e )
        {
            @SuppressWarnings( "unchecked")
            JList<T> list = ( JList<T> )e.getSource();
            int index = list.locationToIndex( e.getPoint() );
            if( index > -1 )
            {
                list.setSelectedIndex( index );
            }
            else
            {
                list.clearSelection();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseExited( MouseEvent e )
        {
            @SuppressWarnings( "unchecked")
            JList<T> list = ( JList<T> )e.getSource();
            list.clearSelection();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class DescriptorListCellLabelDecorator<T>
        implements IListCellLabelDecorator<T>
    {
        /**  */
        private final IListItemModel<T> descriptor;

        /**
         * @param descriptor
         */
        public DescriptorListCellLabelDecorator( IListItemModel<T> descriptor )
        {
            this.descriptor = descriptor;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JList<? extends T> list, T value,
            int index, boolean isSelected, boolean cellHasFocus )
        {
            Icon icon = null;
            String text = "";

            if( value != null )
            {
                icon = descriptor.getIcon( value );
                text = descriptor.getName( value );
            }

            label.setIcon( icon );
            label.setText( text );
        }
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static interface IListItemModel<T>
    {
        /**
         * @param item
         * @return
         */
        public String getName( T item );

        /**
         * @param item
         * @return
         */
        public String getTooltip( T item );

        /**
         * @param item
         * @return
         */
        public Icon getIcon( T item );
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
    public static class DefaultListItemModel<T> implements IListItemModel<T>
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
