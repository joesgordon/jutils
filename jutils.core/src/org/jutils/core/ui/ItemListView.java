package org.jutils.core.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jutils.core.ui.ListView.IItemListModel;
import org.jutils.core.ui.ListView.ItemListCellRenderer;
import org.jutils.core.ui.event.ItemActionEvent;
import org.jutils.core.ui.event.updater.IUpdater;
import org.jutils.core.ui.model.IDataView;

/*******************************************************************************
 * Defines a view that displays a list of items to the user. The view allows for
 * additions/deletions/reordering of the list. Each item in the list is
 * displayed with the {@link IDataView} provided.
 * @param <T> the type of item to be listed.
 ******************************************************************************/
public class ItemListView<T> implements IDataView<List<T>>
{
    /** The main component. */
    private final JPanel view;
    /** The model of the list. */
    private final ListView<T> itemsView;

    /**  */
    private final ComponentView compView;
    /**  */
    private final JScrollPane scrollPane;
    /** The component to be displayed when no item is selected. */
    private final JPanel nullSelectionPanel;

    /** The view to display each item. */
    private final IDataView<T> dataView;

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
     * @param allowAddRemove shows add/remove buttons if {@code true}.
     * @param allowReorder shows order buttons if {@code true}.
     **************************************************************************/
    public ItemListView( IDataView<T> dataView, IItemListModel<T> itemsModel,
        boolean allowAddRemove, boolean allowReorder )
    {
        this( dataView, itemsModel, allowAddRemove, allowReorder, true );
    }

    /***************************************************************************
     * Creates a new view with the provided data view and model.
     * @param dataView the view that displays an individual item when selected.
     * @param itemsModel the model for this view.
     * @param allowAddRemove shows add/remove buttons if {@code true}.
     * @param allowReorder shows order buttons if {@code true}.
     * @param useScrollPane
     **************************************************************************/
    public ItemListView( IDataView<T> dataView, IItemListModel<T> itemsModel,
        boolean allowAddRemove, boolean allowReorder, boolean useScrollPane )
    {
        this.dataView = dataView;

        this.itemsView = new ListView<T>( itemsModel, allowAddRemove,
            allowReorder );

        this.compView = new ComponentView();
        this.scrollPane = useScrollPane ? new JScrollPane( dataView.getView() )
            : null;
        this.nullSelectionPanel = createNullSelectionPanel();

        this.items = new ArrayList<>();

        this.view = createView();

        this.selectedListeners = new ArrayList<>();

        itemsView.addSelectedListener( ( item ) -> invokeItemSelected( item ) );
    }

    /***************************************************************************
     * Creates the component that is displayed when no item is selected.
     * @return the panel displayed when no item is selected.
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
     * @return the panel that contians the main view.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        itemsView.addSelectedListener( ( e ) -> onItemSelected( e.getItem() ) );

        if( scrollPane != null )
        {
            scrollPane.getVerticalScrollBar().setUnitIncrement( 10 );
        }

        compView.setComponent( nullSelectionPanel );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 8, 8, 8 ), 0, 0 );
        panel.add( itemsView.getView(), constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 8, 0, 8, 8 ), 0, 0 );
        panel.add( compView.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    private void onItemSelected( T item )
    {
        Component currentComp = compView.getComponent();
        Component comp = nullSelectionPanel;

        if( item != null )
        {
            dataView.setData( item );
            comp = scrollPane != null ? scrollPane : dataView.getView();
        }

        if( comp != currentComp )
        {
            compView.setComponent( comp );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
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
     * Adds the provided listener and invokes it when an item is selected.
     * @param l the listener invoked when an item is selected.
     **************************************************************************/
    public void addItemSelectedListener( IUpdater<T> l )
    {
        selectedListeners.add( l );
    }

    /***************************************************************************
     * @param button the button to be added to the toolbar.
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
    private void invokeItemSelected( ItemActionEvent<T> evt )
    {
        for( IUpdater<T> u : selectedListeners )
        {
            u.update( evt.getItem() );
        }
    }
}
