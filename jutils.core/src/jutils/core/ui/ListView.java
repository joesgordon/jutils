package jutils.core.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import jutils.core.IconConstants;
import jutils.core.OptionUtils;
import jutils.core.SwingUtils;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.ItemActionList;
import jutils.core.ui.event.ItemActionListener;
import jutils.core.ui.fields.StringFormField;
import jutils.core.ui.model.CollectionListModel;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * Defines a view that displays a list of items to the user. The view allows for
 * additions/deletions to the list.
 * @param <T> the type of items listed.
 ******************************************************************************/
public class ListView<T> implements IDataView<List<T>>
{
    /** The main component. */
    private final JPanel view;
    /** The model of the list. */
    private final CollectionListModel<T> itemsListModel;
    /** The component to display the list. */
    private final JList<T> itemsList;
    /** The scroll pane containing the items. */
    private final JScrollPane itemsPane;
    /** The model for the items. */
    private final IItemListModel<T> itemsModel;

    /** The action used for adding items. */
    private final Action addAction;
    /** The action used for removing items. */
    private final Action removeAction;
    /** The action used for moving items up. */
    private final Action upAction;
    /** The action used for moving items down. */
    private final Action downAction;
    /** The toolbar that has all the buttons in this view. */
    private final JToolBar toolbar;

    /**  */
    private final ItemActionList<T> selectedListeners;
    /**  */
    private final ItemActionList<ItemChange<T>> changeListeners;

    /** The items to be displayed. */
    private List<T> items;

    /***************************************************************************
     * Creates a new view with the provided data view and model.
     * @param itemsModel the model for this view.
     **************************************************************************/
    public ListView( IItemListModel<T> itemsModel )
    {
        this( itemsModel, true, true );
    }

    /***************************************************************************
     * Creates a new view with the provided data view and model.
     * @param itemsModel the model for this view.
     * @param allowAddRemove shows add/remove buttons if {@code true}.
     * @param allowOrder shows order buttons if {@code true}.
     **************************************************************************/
    public ListView( IItemListModel<T> itemsModel, boolean allowAddRemove,
        boolean allowOrder )
    {
        this.itemsModel = itemsModel;

        this.itemsListModel = new CollectionListModel<>();
        this.itemsList = new JList<>( itemsListModel );
        this.itemsPane = new JScrollPane( itemsList );
        this.items = new ArrayList<>();
        this.addAction = allowAddRemove ? createAddAction() : null;
        this.removeAction = allowAddRemove ? createRemoveAction() : null;
        this.upAction = allowOrder ? createUpAction() : null;
        this.downAction = allowOrder ? createDownAction() : null;

        this.toolbar = createToolbar( allowAddRemove, allowOrder );

        this.selectedListeners = new ItemActionList<>();
        this.changeListeners = new ItemActionList<>();

        this.view = createView();

        setItemRenderer( new DefaultItemListCellRenderer<>() );

        itemsListModel.setData( items );

        if( toolbar.getComponentCount() == 0 )
        {
            toolbar.setVisible( false );
        }
    }

    /***************************************************************************
     * Creates the main view.
     * @return the panel that represents the main view.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( toolbar, constraints );

        // itemsPane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        itemsList.setSelectionMode(
            ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        itemsList.addListSelectionListener( ( e ) -> onItemSelected( e ) );

        setItemsSize( 200, 200 );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( itemsPane, constraints );

        return panel;
    }

    /***************************************************************************
     * Creates the component that provides add/remove buttons.
     * @param allowAddRemove
     * @param allowOrder
     * @return
     **************************************************************************/
    private JToolBar createToolbar( boolean allowAddRemove, boolean allowOrder )
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        if( allowAddRemove )
        {
            SwingUtils.addActionToToolbar( toolbar, addAction );
            SwingUtils.addActionToToolbar( toolbar, removeAction );
        }

        if( toolbar.getComponentCount() > 0 )
        {
            toolbar.addSeparator();
        }

        if( allowOrder )
        {
            SwingUtils.addActionToToolbar( toolbar, upAction );
            SwingUtils.addActionToToolbar( toolbar, downAction );
        }

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createAddAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_ADD_16 );
        ActionListener listener = ( e ) -> onAddPressed();
        return new ActionAdapter( listener, "Add", icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void onAddPressed()
    {
        T item = itemsModel.promptForNew( this );

        if( item != null )
        {
            itemsListModel.add( item );
            // items.add( item );

            ItemChange<T> itemChange = new ItemChange<>( ChangeType.ADDED,
                item );

            changeListeners.fireListeners( this, itemChange );
        }
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void onItemSelected( ListSelectionEvent e )
    {
        if( !e.getValueIsAdjusting() )
        {
            T item = itemsList.getSelectedValue();

            selectedListeners.fireListeners( this, item );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createRemoveAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_DELETE_16 );
        ActionListener listener = ( e ) -> invokeDeleteClicked();
        return new ActionAdapter( listener, "Delete", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createUpAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.NAV_UP_16 );
        ActionListener listener = ( e ) -> invokeUpClicked();
        return new ActionAdapter( listener, "Move Up", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createDownAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.NAV_DOWN_16 );
        ActionListener listener = ( e ) -> invokeDownClicked();
        return new ActionAdapter( listener, "Move Down", icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void invokeUpClicked()
    {
        int idx = itemsList.getSelectedIndex();

        if( idx > 0 )
        {
            // T item = items.remove( idx );
            // items.add( idx - 1, item );

            T di = itemsListModel.remove( idx );
            itemsListModel.add( di, idx - 1 );

            itemsList.setSelectedIndex( idx - 1 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void invokeDownClicked()
    {
        int idx = itemsList.getSelectedIndex();

        if( idx > -1 && idx < ( items.size() - 1 ) )
        {
            // T item = items.remove( idx );
            // items.add( idx + 1, item );

            T di = itemsListModel.remove( idx );
            itemsListModel.add( di, idx + 1 );

            itemsList.setSelectedIndex( idx + 1 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void invokeDeleteClicked()
    {
        int [] indexes = itemsList.getSelectedIndices();

        Arrays.sort( indexes );

        for( int i = indexes.length - 1; i > -1; i-- )
        {
            int index = indexes[i];

            T item = items.get( index );
            itemsListModel.remove( index );
            changeListeners.fireListeners( this,
                new ItemChange<>( ChangeType.REMOVED, item ) );
        }
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    private boolean containsItemWithName( String name )
    {
        for( T t : itemsListModel )
        {
            if( name.equals( t.toString() ) )
            {
                return true;
            }
        }
        return false;
    }

    /***************************************************************************
     * Adds the provided listener and invokes it when an item is selected.
     * @param l the listener invoked when an item is selected.
     **************************************************************************/
    public void addSelectedListener( ItemActionListener<T> l )
    {
        selectedListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addListChangedListener( ItemActionListener<ItemChange<T>> l )
    {
        changeListeners.addListener( l );
    }

    /***************************************************************************
     * @param button
     **************************************************************************/
    public void addToToolbar( JButton button )
    {
        toolbar.add( button );
        if( toolbar.isVisible() )
        {
            toolbar.setVisible( true );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void addSeparatorToToolbar()
    {
        toolbar.addSeparator();
        if( toolbar.isVisible() )
        {
            toolbar.setVisible( true );
        }
    }

    /***************************************************************************
     * Sets the width and height of the items scroll pane.
     * @param width the width of the scroll pane.
     * @param height the height of the scroll pane.
     **************************************************************************/
    public void setItemsSize( int width, int height )
    {
        Dimension dim = new Dimension( width, height );

        itemsPane.setMinimumSize( dim );
        itemsPane.setPreferredSize( dim );
        itemsPane.setMaximumSize( dim );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
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
        this.items = data;

        itemsListModel.setData( data );
        itemsList.clearSelection();
    }

    /***************************************************************************
     * Sets the renderer for the list.
     * @param renderer the list cell renderer.
     **************************************************************************/
    public void setItemRenderer( ItemListCellRenderer<T> renderer )
    {
        itemsList.setCellRenderer(
            new DisplayItemRenderer<T>( renderer, this.itemsModel ) );
    }

    /***************************************************************************
     * @param type
     * @return
     **************************************************************************/
    public String promptForName( String type )
    {
        String message = "Enter " + type + " name";

        return promptForString( message );
    }

    /***************************************************************************
     * Prompts for a name of an item using a option pane.
     * @param message the type of item that needs a name (cat, dog, bird, etc.).
     * @return the name entered by the user or {@code null} if cancelled.
     **************************************************************************/
    public String promptForString( String message )
    {
        String name = "";
        boolean prompt = true;

        while( prompt )
        {
            StringFormField nameField = new StringFormField( "Name" );
            name = OptionUtils.showQuestionField( view, message, "Input Name",
                nameField );

            if( name != null )
            {
                if( !name.isEmpty() )
                {
                    if( !containsItemWithName( name ) )
                    {
                        prompt = false;
                    }
                    else
                    {
                        OptionUtils.showErrorMessage( view, "The name " + name +
                            " already exists. Please Choose a different one.",
                            "Name Exists" );
                    }
                }
                else
                {
                    OptionUtils.showErrorMessage( view,
                        "The name cannot be empty.", "Name Empty" );
                }
            }
            else
            {
                prompt = false;
            }
        }

        return name;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getSelected()
    {
        return itemsList.getSelectedValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        itemsListModel.clear();
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        itemsList.setEnabled( enabled );
        addAction.setEnabled( enabled );
        removeAction.setEnabled( enabled );
        upAction.setEnabled( enabled );
        downAction.setEnabled( enabled );
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    public void setSelected( T item )
    {
        if( item != null )
        {
            itemsList.setSelectedValue( item, false );
        }
        else
        {
            itemsList.clearSelection();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void refreshSelected()
    {
        int index = itemsList.getSelectedIndex();

        if( index > -1 )
        {
            itemsListModel.refreshIndex( index );
        }
    }

    /***************************************************************************
     * The model used for this view. This model does not provide the data, but
     * provides methods of accessing said data.
     * @param <T>
     **************************************************************************/
    public static interface IItemListModel<T>
    {
        /**
         * Returns the string representation of the provided item.
         * @param item
         * @return
         */
        public String getTitle( T item );

        /**
         * Prompts the user for a new item.
         * @param view
         * @return
         */
        public T promptForNew( ListView<T> view );
    }

    /***************************************************************************
     * Defines a cell renderer.
     * @param <T> The type of item to be added to the list.
     **************************************************************************/
    public static interface ItemListCellRenderer<T>
    {
        /**
         * @param list
         * @param value
         * @param index
         * @param isSelected
         * @param cellHasFocus
         * @param text
         * @return
         */
        public Component getListCellRendererComponent( JList<? extends T> list,
            T value, int index, boolean isSelected, boolean cellHasFocus,
            String text );
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static final class DefaultItemListCellRenderer<T>
        implements ItemListCellRenderer<T>
    {
        /**  */
        private final DefaultListCellRenderer renderer = new DefaultListCellRenderer();

        /**
         * {@inheritDoc}
         */
        @Override
        public Component getListCellRendererComponent( JList<? extends T> list,
            T value, int index, boolean isSelected, boolean cellHasFocus,
            String text )
        {
            Component c = renderer.getListCellRendererComponent( list, value,
                index, isSelected, cellHasFocus );

            renderer.setText( text );

            return c;
        }
    }

    /***************************************************************************
     * Defines an Adapter to be a renderer for the DisplayItem<T> list that uses
     * a {@link ListView.ItemListCellRenderer} to render the cell.
     * @param <T> The type of item to be added to the list.
     **************************************************************************/
    private static class DisplayItemRenderer<T> implements ListCellRenderer<T>
    {
        /**  */
        private final ItemListCellRenderer<T> renderer;
        /**  */
        private final IItemListModel<T> model;

        /**
         * @param renderer
         * @param model
         */
        public DisplayItemRenderer( ItemListCellRenderer<T> renderer,
            IItemListModel<T> model )
        {
            this.renderer = renderer;
            this.model = model;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Component getListCellRendererComponent( JList<? extends T> list,
            T value, int index, boolean isSelected, boolean cellHasFocus )
        {
            String text = null;

            if( value != null )
            {
                text = model.getTitle( value );
            }

            return renderer.getListCellRendererComponent( list, value, index,
                isSelected, cellHasFocus, text );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum ChangeType
    {
        /**  */
        ADDED,
        /**  */
        REMOVED;
    }

    /***************************************************************************
     * @param <T> the type of item listed.
     **************************************************************************/
    public static final class ItemChange<T>
    {
        /** How the item changed. */
        public final ChangeType type;
        /** The item that changed. */
        public final T item;

        /**
         * @param type how the item changed.
         * @param item the item that changed.
         */
        public ItemChange( ChangeType type, T item )
        {
            this.type = type;
            this.item = item;
        }
    }
}
