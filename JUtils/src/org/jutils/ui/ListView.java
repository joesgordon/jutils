package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.event.*;
import org.jutils.ui.model.CollectionListModel;
import org.jutils.ui.model.IDataView;

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
     * @param canAddRemove shows add/remove buttons if {@code true}.
     * @param canOrder shows order buttons if {@code true}.
     **************************************************************************/
    public ListView( IItemListModel<T> itemsModel, boolean canAddRemove,
        boolean canOrder )
    {
        this.itemsModel = itemsModel;

        this.itemsListModel = new CollectionListModel<>();
        this.itemsList = new JList<>( itemsListModel );
        this.itemsPane = new JScrollPane( itemsList );
        this.items = new ArrayList<>();
        this.addAction = canAddRemove ? createAddAction() : null;
        this.removeAction = canAddRemove ? createRemoveAction() : null;
        this.upAction = canOrder ? createUpAction() : null;
        this.downAction = canOrder ? createDownAction() : null;

        this.toolbar = createToolbar( canAddRemove, canOrder );

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
        itemsList.addListSelectionListener( new ItemSelctedListener<>( this ) );

        setItemsSize( 200, 200 );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( itemsPane, constraints );

        return panel;
    }

    /***************************************************************************
     * Creates the component that provides add/remove buttons.
     * @param canAddRemove
     * @param canOrder
     * @return
     **************************************************************************/
    private JToolBar createToolbar( boolean canAddRemove, boolean canOrder )
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        if( canAddRemove )
        {
            SwingUtils.addActionToToolbar( toolbar, addAction );
            SwingUtils.addActionToToolbar( toolbar, removeAction );
        }

        if( toolbar.getComponentCount() > 0 )
        {
            toolbar.addSeparator();
        }

        if( canOrder )
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
        ActionListener listener = new AddItemListener<T>( this );
        return new ActionAdapter( listener, "Add", icon );
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
     * @param l
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
     * Prompts for a name of an item using a {@link JOptionPane}.
     * @param message the type of item that needs a name (cat, dog, bird, etc.).
     * @return the name entered by the user or {@code null} if cancelled.
     **************************************************************************/
    public String promptForString( String message )
    {
        String name = "";
        boolean prompt = true;

        while( prompt )
        {
            name = JOptionPane.showInputDialog( view, message, name );

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
                        JOptionPane.showMessageDialog( view, "The name " +
                            name +
                            " already exists. Please Choose a different one.",
                            "Name Exists", JOptionPane.ERROR_MESSAGE );
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog( view,
                        "The name cannot be empty.", "Name Empty",
                        JOptionPane.ERROR_MESSAGE );
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
     **************************************************************************/
    public static interface IItemListModel<T>
    {
        /** Returns the string representation of the provided item. */
        public String getTitle( T item );

        /** Prompts the user for a new item. */
        public T promptForNew( ListView<T> view );
    }

    /***************************************************************************
     * Defines the listener to be called when an item is selected.
     **************************************************************************/
    private static class ItemSelctedListener<T> implements ListSelectionListener
    {
        private final ListView<T> view;

        public ItemSelctedListener( ListView<T> view )
        {
            this.view = view;
        }

        @Override
        public void valueChanged( ListSelectionEvent e )
        {
            if( !e.getValueIsAdjusting() )
            {
                T item = view.itemsList.getSelectedValue();

                view.selectedListeners.fireListeners( view, item );
            }
        }
    }

    /***************************************************************************
     * Defines the listener to be called when the add button is pressed.
     **************************************************************************/
    private static class AddItemListener<T> implements ActionListener
    {
        private final ListView<T> itemListView;

        public AddItemListener( ListView<T> itemListView )
        {
            this.itemListView = itemListView;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            T item = itemListView.itemsModel.promptForNew( itemListView );

            if( item != null )
            {
                itemListView.itemsListModel.add( item );
                // itemListView.items.add( item );
                itemListView.changeListeners.fireListeners( itemListView,
                    new ItemChange<>( ChangeType.ADDED, item ) );
            }
        }
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
        private final DefaultListCellRenderer renderer = new DefaultListCellRenderer();

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
        ADDED,
        REMOVED;
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static final class ItemChange<T>
    {
        public final ChangeType type;
        public final T item;

        public ItemChange( ChangeType type, T item )
        {
            this.type = type;
            this.item = item;
        }
    }
}
