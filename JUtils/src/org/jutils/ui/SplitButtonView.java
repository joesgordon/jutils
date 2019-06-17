package org.jutils.ui;

import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jutils.SwingUtils;
import org.jutils.data.UIProperty;
import org.jutils.ui.event.IRecentListener;
import org.jutils.ui.model.*;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

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
        fireListeners( item, ctrlPressed );
    }

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
     * 
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
     * @param files
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
     * 
     **************************************************************************/
    private class ArrowIcon implements Icon
    {
        private final Color shadow;
        private final Color highlight;
        private final Color darkShadow;

        private static final int SIZE = 5;

        public ArrowIcon()
        {
            this.shadow = UIManager.getColor( "controlShadow" );
            this.darkShadow = Color.black;
            // this.darkShadow = UIManager.getColor( "controlDkShadow" );
            this.highlight = UIManager.getColor( "controlLtHighlight" );
        }

        @Override
        public void paintIcon( Component c, Graphics g, int x, int y )
        {
            int x1 = 1 + c.getWidth() / 2 - SIZE / 2;
            int y1 = c.getHeight() / 2 - SIZE / 2;

            paintTriangle( g, x1, y1, SIZE, true );
        }

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

        @Override
        public int getIconWidth()
        {
            return SIZE;
        }

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
        private final PopupWindow popup;
        private final CollectionListModel<T> model;
        private final JList<T> list;
        private final List<IRecentListener<T>> selectedListeners;

        /**
         * @param items
         * @param descriptor
         */
        public ListPopup( List<T> items, IListItemModel<T> descriptor )
        {
            this.model = new CollectionListModel<>();
            this.list = new JList<>( model );
            this.selectedListeners = new ArrayList<>();

            model.setData( items );

            // list.setFocusable( false );
            SwingUtils.addKeyListener( list, "ENTER",
                ( e ) -> fireSelected( list.getSelectedValue(), false ),
                "List Enter Pressed", false );
            list.setBackground(
                UIProperty.TEXTFIELD_INACTIVEBACKGROUND.getColor() );
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

            this.popup = new PopupWindow( true, list );

            popup.setBorder( new LineBorder( Color.black ) );
        }

        public void hide()
        {
            // LogUtils.printDebug( "Hiding" );
            popup.hide();
        }

        public void addItemSelectedListener( IRecentListener<T> l )
        {
            selectedListeners.add( l );
        }

        public void setItems( List<T> items )
        {
            model.setData( items );
        }

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
        private final ListPopup<T> popup;

        public ListMouseListener( ListPopup<T> popup )
        {
            this.popup = popup;
        }

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

                        popup.fireSelected( item, ctrlPressed );
                    }
                }
            }
        }

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
        private final IListItemModel<T> descriptor;

        public DescriptorListCellLabelDecorator( IListItemModel<T> descriptor )
        {
            this.descriptor = descriptor;
        }

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

    public static interface IListItemModel<T>
    {
        public String getName( T item );

        public String getTooltip( T item );

        public Icon getIcon( T item );
    }

    public static class DefaultListItemModel<T> implements IListItemModel<T>
    {
        @Override
        public String getName( T item )
        {
            return item.toString();
        }

        @Override
        public String getTooltip( T item )
        {
            return "";
        }

        @Override
        public Icon getIcon( T item )
        {
            return null;
        }
    }
}
