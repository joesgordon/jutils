package jutils.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import jutils.core.SwingUtils;
import jutils.core.laf.UIProperty;
import jutils.core.ui.SplitButtonView.IListRightClickListener;
import jutils.core.ui.event.IRecentListener;
import jutils.core.ui.model.ItemsListModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ItemsListPopup<T>
{
    /**  */
    private final PopupWindow popup;
    /**  */
    private final ItemsList<T> list;
    /**  */
    private final List<IRecentListener<T>> selectedListeners;
    /**  */
    private final List<IListRightClickListener<T>> rightClickListeners;

    /***************************************************************************
     * @param items
     * @param descriptor
     **************************************************************************/
    public ItemsListPopup( List<T> items, ItemsListModel<T> descriptor )
    {
        this.list = new ItemsList<>( descriptor );
        this.selectedListeners = new ArrayList<>();
        this.rightClickListeners = new ArrayList<>();
        this.popup = new PopupWindow( list );

        list.setItems( items );

        // list.setFocusable( false );
        SwingUtils.addKeyListener( list, "ENTER",
            ( e ) -> fireSelected( list.getSelectedValue(), false ),
            "List Enter Pressed", false );
        list.setBackground( UIProperty.PANEL_BACKGROUND.getColor() );
        list.setVisibleRowCount( 10 );

        ListMouseListener<T> lml = new ListMouseListener<>( this );

        list.addMouseListener( lml );
        list.addMouseMotionListener( lml );
        list.setFixedCellHeight( 24 );

        popup.setBorder( new LineBorder( Color.gray ) );
    }

    /***************************************************************************
     * @param listener
     **************************************************************************/
    public void addRightClickListener( IListRightClickListener<T> listener )
    {
        rightClickListeners.add( listener );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void hide()
    {
        popup.hide();
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addItemSelectedListener( IRecentListener<T> l )
    {
        selectedListeners.add( l );
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public void setItems( List<T> items )
    {
        list.setItems( items );
    }

    /***************************************************************************
     * @param parent
     **************************************************************************/
    public void show( JComponent parent )
    {
        if( list.getItemCount() > 0 )
        {
            list.setSelectedValue( list.getItem( 0 ), true );
        }
        list.clearSelection();
        popup.show( parent, 0, parent.getHeight() );
        list.requestFocus();
    }

    /***************************************************************************
     * @param item
     * @param ctrlPressed
     **************************************************************************/
    public void fireSelected( T item, boolean ctrlPressed )
    {
        for( IRecentListener<T> irl : selectedListeners )
        {
            irl.selected( item, ctrlPressed );
        }
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static final class ListMouseListener<T> extends MouseAdapter
    {
        /**  */
        private final ItemsListPopup<T> popup;

        /**
         * @param popup
         */
        public ListMouseListener( ItemsListPopup<T> popup )
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
}
