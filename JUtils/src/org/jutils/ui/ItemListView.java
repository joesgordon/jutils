package org.jutils.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.ListView.ItemListCellRenderer;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * Defines a view that displays a list of items to the user. The view allows for
 * additions/deletions/reordering of the list. Each item in the list is
 * displayed with the {@link IDataView} provided.
 ******************************************************************************/
public class ItemListView<T> implements IDataView<List<T>>
{
    /** The main component. */
    private final JPanel view;
    /** The model of the list. */
    private final ListView<T> itemsView;
    /** The view to display each item. */
    private final IDataView<T> dataView;
    /** The scroll pane containing the individual item view. */
    private final JScrollPane itempane;
    /** The component to be displayed when no item is selected. */
    private final JPanel nullSelectionPanel;

    /** The item selection listeners to be called when an item is selected. */
    private final List<IUpdater<T>> selectedListeners;

    /** The items to be displayed. */
    private List<T> items;

    /***************************************************************************
     * Creates a new view with the provided data view and model.
     * @param dataView the view that displays an individual item when selected.
     * @param itemsModel the model for this view.
     **************************************************************************/
    public ItemListView( IDataView<T> dataView, IItemListModel<T> itemsModel )
    {
        this( dataView, itemsModel, true, true );
    }

    /***************************************************************************
     * Creates a new view with the provided data view and model.
     * @param dataView the view that displays an individual item when selected.
     * @param itemsModel the model for this view.
     * @param canAddRemove shows add/remove buttons if {@code true}.
     * @param canOrder shows order buttons if {@code true}.
     **************************************************************************/
    public ItemListView( IDataView<T> dataView, IItemListModel<T> itemsModel,
        boolean canAddRemove, boolean canOrder )
    {
        this.dataView = dataView;

        this.itemsView = new ListView<T>( itemsModel, canAddRemove, canOrder );
        this.items = new ArrayList<>();
        this.nullSelectionPanel = createNullSelectionPanel();
        this.itempane = new JScrollPane( nullSelectionPanel );

        this.view = createView();

        this.selectedListeners = new ArrayList<>();

        itemsView.addSelectedListener( ( item ) -> itemSelected( item ) );
    }

    /***************************************************************************
     * Creates the component that is displayed when no item is selected.
     **************************************************************************/
    private static JPanel createNullSelectionPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JLabel label = new JLabel( "No item selected" );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( label, constraints );

        return panel;
    }

    /***************************************************************************
     * Creates the main view.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        itemsView.addSelectedListener( new ItemSelected<T>( this ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 8, 8, 8 ), 0, 0 );
        panel.add( itemsView.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 8, 0, 8, 8 ), 0, 0 );
        panel.add( itempane, constraints );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<T> getData()
    {
        ArrayList<T> data = new ArrayList<>( items );
        return data;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( List<T> data )
    {
        items = data;

        itemsView.setData( data );
    }

    /***************************************************************************
     * Sets the renderer for the list.
     * @param renderer the list cell renderer.
     **************************************************************************/
    public void setItemRenderer( ItemListCellRenderer<T> renderer )
    {
        itemsView.setItemRenderer( renderer );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addItemSelectedListener( IUpdater<T> l )
    {
        selectedListeners.add( l );
    }

    /***************************************************************************
     * @param button
     **************************************************************************/
    public void addToToolbar( JButton button )
    {
        itemsView.addToToolbar( button );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void addSeparatorToToolbar()
    {
        itemsView.addSeparatorToToolbar();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getSelected()
    {
        return itemsView.getSelected();
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    public void setSelected( T item )
    {
        itemsView.setSelected( item );
    }

    /***************************************************************************
     * @param width
     * @param height
     **************************************************************************/
    public void setItemsSize( int width, int height )
    {
        itemsView.setItemsSize( width, height );
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        itemsView.setSelected( null );
        itemsView.setEnabled( enabled );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void refreshSelected()
    {
        itemsView.refreshSelected();
    }

    /***************************************************************************
     * @param evt
     **************************************************************************/
    private void itemSelected( ItemActionEvent<T> evt )
    {
        for( IUpdater<T> u : selectedListeners )
        {
            u.update( evt.getItem() );
        }
    }

    /***************************************************************************
     * @param <E>
     **************************************************************************/
    private static class ItemSelected<E> implements ItemActionListener<E>
    {
        private final ItemListView<E> view;

        public ItemSelected( ItemListView<E> view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<E> event )
        {
            E item = event.getItem();
            Component currentComp = view.itempane.getViewport().getView();
            Component comp = view.nullSelectionPanel;

            if( item != null )
            {
                view.dataView.setData( item );
                comp = view.dataView.getView();
            }

            if( comp != currentComp )
            {
                view.itempane.setViewportView( comp );
            }
        }
    }
}
