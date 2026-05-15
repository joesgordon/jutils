package jutils.core.ui;

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

    /***************************************************************************
     * @param itemModel
     **************************************************************************/
    public ItemsListView( ItemsListModel<T> itemModel )
    {
        this.list = new ItemsList<>( itemModel );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public ItemsList<T> getView()
    {
        return list;
    }
}
