package jutils.core.ui;

import java.util.List;

import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;

import jutils.core.ui.ListView.SelectionMode;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.event.updater.UpdaterList;
import jutils.core.ui.model.IView;
import jutils.core.ui.model.ItemsListModel;

/*******************************************************************************
 * Defines a view that displays a list of items to the user. The view allows for
 * additions/deletions to the list.
 * @param <T> the type of items listed.
 ******************************************************************************/
public class ItemsListView<T> implements IView<ItemsList<T>>
{
    /**  */
    private final ItemsList<T> list;

    /** The item selection listeners to be called when an item is selected. */
    private final UpdaterList<T> selectedListeners;

    /***************************************************************************
     * @param itemModel
     **************************************************************************/
    public ItemsListView( ItemsListModel<T> itemModel )
    {
        this.list = new ItemsList<>( itemModel );
        this.selectedListeners = new UpdaterList<>();
    }

    /***************************************************************************
     * @param itemModel
     * @param renderer
     **************************************************************************/
    public ItemsListView( ItemsListModel<T> itemModel,
        ListCellRenderer<T> renderer )
    {
        this.list = new ItemsList<T>( itemModel, renderer );
        this.selectedListeners = new UpdaterList<T>();

        list.addListSelectionListener( ( e ) -> hanldleSelection( e ) );
    }

    /***************************************************************************
     * @param evt
     **************************************************************************/
    private void hanldleSelection( ListSelectionEvent evt )
    {
        if( !evt.getValueIsAdjusting() )
        {
            T item = list.getSelectedValue();

            selectedListeners.fire( item );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public ItemsList<T> getView()
    {
        return list;
    }

    /***************************************************************************
     * @param mode
     **************************************************************************/
    public void setSelectionMode( SelectionMode mode )
    {
        list.setSelectionMode( mode.value );
    }

    /***************************************************************************
     * Adds the provided listener and invokes it when an item is selected.
     * @param onSelected the listener invoked when an item is selected.
     **************************************************************************/
    public void addItemSelectedListener( IUpdater<T> onSelected )
    {
        selectedListeners.add( onSelected );
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    public void addItem( T item )
    {
        list.addItem( item );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<T> getItems()
    {
        return list.getItems();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getSelectedItem()
    {
        return list.getSelectedValue();
    }
}
