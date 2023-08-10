package org.jutils.core.ui;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jutils.core.IconConstants;
import org.jutils.core.SwingUtils;
import org.jutils.core.io.IItemStream;
import org.jutils.core.io.IStringWriter;
import org.jutils.core.ui.OkDialogView.OkDialogButtons;
import org.jutils.core.ui.RowHeaderView.PaginatedNumberRowHeaderModel;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.BottomScroller;
import org.jutils.core.ui.event.DoubleClickListener;
import org.jutils.core.ui.event.ResizingTableModelListener;
import org.jutils.core.ui.model.IDataView;
import org.jutils.core.ui.model.ITableConfig;
import org.jutils.core.ui.model.IView;
import org.jutils.core.ui.model.ItemsTableModel;
import org.jutils.core.ui.model.LabelTableCellRenderer;
import org.jutils.core.ui.model.LabelTableCellRenderer.ITableCellLabelDecorator;
import org.jutils.core.ui.net.StringWriterView;

/*******************************************************************************
 * Defines UI that displays a paginated table of objects of a particular type.
 * @param <T> the type of item to be displayed.
 ******************************************************************************/
public class PaginatedTableView<T> implements IView<JPanel>
{
    /** This class's main view. */
    private final JPanel view;
    /** The toolbar for this view. */
    private final JToolBar toolbar;
    /**  */
    private final ItemsTableModel<T> tableModel;
    /**  */
    private final JTable table;
    /**  */
    private final JScrollPane tablePane;
    /**  */
    private final PaginatedNumberRowHeaderModel headerModel;

    /**  */
    private final JButton navFirstButton;
    /**  */
    private final JButton navPreviousButton;
    /**  */
    private final JButton navNextButton;
    /**  */
    private final JButton navLastButton;
    /**  */
    private final JLabel pageLabel;

    /**  */
    private OkDialogView dialog;
    /**  */
    private final IDataView<T> itemView;

    /**  */
    public final Object itemsLock;
    /**  */
    public IItemStream<T> itemsStream;

    /**  */
    private int itemsPerPage;
    /** The index of the item at the top of this page. */
    private long pageStartIndex;

    /***************************************************************************
     * @param tableCfg the configuration of the table to be displayed.
     * @param itemsStream
     **************************************************************************/
    public PaginatedTableView( ITableConfig<T> tableCfg,
        IItemStream<T> itemsStream )
    {
        this( tableCfg, itemsStream, ( IDataView<T> )null );
    }

    /***************************************************************************
     * @param tableCfg the configuration of the table to be displayed.
     * @param itemsStream
     * @param itemWriter the method of creating a string that represents an
     * item.
     **************************************************************************/
    public PaginatedTableView( ITableConfig<T> tableCfg,
        IItemStream<T> itemsStream, IStringWriter<T> itemWriter )
    {
        this( tableCfg, itemsStream, createItemWriterView( itemWriter ) );
    }

    /***************************************************************************
     * @param tableCfg the configuration of the table to be displayed.
     * @param itemsStream
     * @param itemView a view that will display an item.
     **************************************************************************/
    public PaginatedTableView( ITableConfig<T> tableCfg,
        IItemStream<T> itemsStream, IDataView<T> itemView )
    {
        this.tableModel = new ItemsTableModel<>( tableCfg );
        this.table = new JTable( tableModel );
        this.tablePane = new JScrollPane( table );
        this.headerModel = new PaginatedNumberRowHeaderModel();
        this.navFirstButton = new JButton();
        this.navPreviousButton = new JButton();
        this.navNextButton = new JButton();
        this.navLastButton = new JButton();
        this.pageLabel = new JLabel( "Page 0 of 0 (0)" );
        this.dialog = null;
        this.itemsLock = new Object();
        this.itemsStream = itemsStream;
        this.toolbar = createToolbar();
        this.view = createView();

        this.itemsPerPage = 500;
        this.pageStartIndex = 0L;

        this.itemView = new ItemNavigationView<>( this, itemView );
    }

    /***************************************************************************
     * Creates this class's main view.
     * @return this class's main view.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        table.getTableHeader().setReorderingAllowed( false );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.addMouseListener(
            new DoubleClickListener( ( p ) -> handleDoubleClick( p ) ) );

        JScrollBar vScrollBar = tablePane.getVerticalScrollBar();

        vScrollBar.addAdjustmentListener( new BottomScroller( vScrollBar ) );

        RowHeaderView rowView = new RowHeaderView( table );

        rowView.setModel( headerModel );

        tablePane.setRowHeaderView( rowView.getView() );

        tablePane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        tablePane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );

        panel.add( toolbar, BorderLayout.NORTH );
        panel.add( tablePane, BorderLayout.CENTER );

        panel.setMinimumSize( new Dimension( 625, 200 ) );
        panel.setPreferredSize( new Dimension( 625, 200 ) );

        return panel;
    }

    /***************************************************************************
     * @param p the table relative point that was double-clicked.
     **************************************************************************/
    private void handleDoubleClick( Point p )
    {
        long index = table.rowAtPoint( p ) + pageStartIndex;

        showItem( index );
    }

    /***************************************************************************
     * Creates the toolbar for this view.
     * @return the toolbar for this view.
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( true, false ),
            navFirstButton );
        navFirstButton.setText( "" );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( false, false ),
            navPreviousButton );
        navPreviousButton.setText( "" );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( false, true ),
            navNextButton );
        navNextButton.setText( "" );

        SwingUtils.addActionToToolbar( toolbar, createNavAction( true, true ),
            navLastButton );
        navLastButton.setText( "" );

        toolbar.addSeparator();

        toolbar.add( pageLabel );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, createClearAction() );

        toolbar.addSeparator();

        return toolbar;
    }

    /***************************************************************************
     * @param absolute if this action navigates to an end of the pages.
     * @param forward if this action navigates forward.
     * @return the action that navigates.
     **************************************************************************/
    private Action createNavAction( boolean absolute, boolean forward )
    {
        ActionListener listener = ( e ) -> navigatePage( absolute, forward );
        String iconName;
        Icon icon;
        String actionName;

        if( absolute && !forward )
        {
            iconName = IconConstants.NAV_FIRST_16;
            actionName = "First Page";
        }
        else if( !absolute && !forward )
        {
            iconName = IconConstants.NAV_PREVIOUS_16;
            actionName = "Previous Page";
        }
        else if( !absolute && forward )
        {
            iconName = IconConstants.NAV_NEXT_16;
            actionName = "Next Page";
        }
        else
        {
            iconName = IconConstants.NAV_LAST_16;
            actionName = "Last Page";
        }

        icon = IconConstants.getIcon( iconName );

        return new ActionAdapter( listener, actionName, icon );
    }

    /***************************************************************************
     * @return an action that clears all items.
     **************************************************************************/
    private Action createClearAction()
    {
        ActionListener listener = ( e ) -> clearItems();
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_CLEAR_16 );

        return new ActionAdapter( listener, "Clear", icon );
    }

    /***************************************************************************
     * @param absolute if the navigation is to an end of the pages.
     * @param forward if the navigation is forward (+1).
     **************************************************************************/
    private void navigatePage( boolean absolute, boolean forward )
    {
        long index = -1;

        if( absolute && !forward )
        {
            index = 0;
        }
        else if( !absolute && !forward )
        {
            index = pageStartIndex - itemsPerPage;
        }
        else if( !absolute && forward )
        {
            index = pageStartIndex + itemsPerPage;
        }
        else
        {
            index = itemsStream.getCount() - 1;
            index = index - index % itemsPerPage;
        }

        if( index > -1 && index < itemsStream.getCount() )
        {
            navigatePage( index );
        }
    }

    /***************************************************************************
     * @param startIndex the item index the page will start.
     **************************************************************************/
    private void navigatePage( long startIndex )
    {
        navigatePage( startIndex, false );
    }

    /***************************************************************************
     * @param startIndex the item index the page will start.
     * @param ignoreIndexCheck if {@code true} reloads even if the index doesn't
     * change.
     **************************************************************************/
    private void navigatePage( long startIndex, boolean ignoreIndexCheck )
    {
        if( !ignoreIndexCheck && this.pageStartIndex == startIndex )
        {
            return;
        }

        int count = ( int )Math.min( itemsPerPage,
            itemsStream.getCount() - startIndex );

        // LogUtils.printDebug( "Setting start index to %d from %d of %d for
        // %d",
        // startIndex, pageStartIndex, itemsStream.getCount(), count );

        if( startIndex < 0 || startIndex >= itemsStream.getCount() )
        {
            return;
        }

        this.pageStartIndex = startIndex;

        List<T> items = null;
        synchronized( itemsLock )
        {
            items = itemsStream.get( pageStartIndex, count );
        }
        tableModel.setItems( items );
        updateRowHeader( count );
        ResizingTableModelListener.resizeTable( table );

        setNavButtonsEnabled();
    }

    /***************************************************************************
     * @param count the number of rows in the row header.
     **************************************************************************/
    private void updateRowHeader( int count )
    {
        headerModel.setData( pageStartIndex + 1, count );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setNavButtonsEnabled()
    {
        boolean hasPrev = hasPrevious();
        boolean hasNext = hasNext();

        navFirstButton.setEnabled( hasPrev );
        navPreviousButton.setEnabled( hasPrev );
        navNextButton.setEnabled( hasNext );
        navLastButton.setEnabled( hasNext );

        int pageCount = getPageCount();
        int pageIndex = ( int )( ( pageStartIndex + itemsPerPage - 1 ) /
            itemsPerPage );
        int pageNum = pageCount == 0 ? 0 : ( pageIndex + 1 );

        pageLabel.setText( String.format( "Page %d of %d (%d)", pageNum,
            pageCount, itemsStream.getCount() ) );
    }

    /***************************************************************************
     * @return the number of pages needed to navigate the items.
     **************************************************************************/
    private int getPageCount()
    {
        long count = itemsStream.getCount();

        int max = ( int )( ( count + itemsPerPage - 1 ) / itemsPerPage );

        return max;
    }

    /***************************************************************************
     * @return {@code true} if there is a previous page.
     **************************************************************************/
    private boolean hasPrevious()
    {
        return pageStartIndex > 0;
    }

    /***************************************************************************
     * @return {@code true} if there is a next page.
     **************************************************************************/
    private boolean hasNext()
    {
        long lastItem = itemsStream.getCount() - 1;
        long maxStartIndex = lastItem - lastItem % itemsPerPage;
        return pageStartIndex < maxStartIndex;
    }

    /***************************************************************************
     * @param index the index of an item.
     * @return {@code true} if the provided index is in the current page.
     **************************************************************************/
    private boolean isPaged( long index )
    {
        return index >= pageStartIndex &&
            index < ( pageStartIndex + itemsPerPage );
    }

    /***************************************************************************
     * @return {@code true} if the scrollbar is already at the bottom.
     **************************************************************************/
    private boolean isAtBottom()
    {
        JScrollBar bar = tablePane.getVerticalScrollBar();
        int value = bar.getValue();
        int extent = bar.getModel().getExtent();
        int max = bar.getMaximum() - extent;

        return value >= max;
    }

    /***************************************************************************
     * @param <F> the type of item to be represented as a string.
     * @param itemWriter the method of representing an item as a string.
     * @return the view that will display a string representation of an item.
     **************************************************************************/
    public static <F> StringWriterView<F> createItemWriterView(
        IStringWriter<F> itemWriter )
    {
        return itemWriter == null ? null : new StringWriterView<>( itemWriter );
    }

    /***************************************************************************
     * @param itemIndex the index of the item to be shown.
     **************************************************************************/
    public void showItem( long itemIndex )
    {
        if( itemIndex < 0 || itemIndex >= itemsStream.getCount() )
        {
            return;
        }

        if( !isPaged( itemIndex ) )
        {
            long start = itemIndex - itemIndex % itemsPerPage;
            navigatePage( start );
        }

        int tableIndex = ( int )( itemIndex - pageStartIndex );

        Frame f = SwingUtils.getComponentsFrame( table );
        T item = tableModel.getItem( tableIndex );

        itemView.setData( item );

        if( this.dialog == null )
        {
            this.dialog = new OkDialogView( getView(), itemView.getView(),
                ModalityType.MODELESS, OkDialogButtons.OK_ONLY );
        }

        JDialog d = dialog.getView();

        if( d.isVisible() )
        {
            d.toFront();
        }
        else
        {
            d.setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
            d.setSize( 910, 520 );
            d.setLocationRelativeTo( f );
        }

        d.setTitle( String.format( "Item %d", itemIndex + 1 ) );
        d.setVisible( true );

        table.setRowSelectionInterval( tableIndex, tableIndex );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param item the item to be added.
     **************************************************************************/
    public void addItem( T item )
    {
        long count = itemsStream.getCount();
        long lastStartIndex = Math.max( count - 1, 0 );
        lastStartIndex = lastStartIndex - lastStartIndex % itemsPerPage;

        synchronized( itemsLock )
        {
            itemsStream.add( item );
        }
        count++;

        long nextPageStartIndex = pageStartIndex + itemsPerPage;

        // LogUtils.printDebug(
        // "Adding message; last start = %d, next start = %d, current start =
        // %d, count = %d",
        // lastStartIndex, nextIndex, pageStartIndex, itemsStream.getCount() );

        if( lastStartIndex == pageStartIndex )
        {
            if( count > nextPageStartIndex )
            {
                if( isAtBottom() )
                {
                    navigatePage( count - 1 );
                }
            }
            else
            {
                tableModel.addItem( item );
                updateRowHeader( tableModel.getRowCount() );
                ResizingTableModelListener.resizeTable( table );
            }
        }

        setNavButtonsEnabled();
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public void setItems( IItemStream<T> items )
    {
        clearItems();

        synchronized( itemsLock )
        {
            this.itemsStream = items;
        }

        pageStartIndex = 0L;
        navigatePage( 0L, true );
        ResizingTableModelListener.resizeTable( table );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearItems()
    {
        tableModel.clearItems();

        synchronized( itemsLock )
        {
            itemsStream.removeAll();
        }

        pageStartIndex = 0L;
        setNavButtonsEnabled();
        updateRowHeader( 0 );

        if( dialog != null && dialog.getView().isVisible() )
        {
            dialog.getView().setVisible( false );
        }
    }

    /***************************************************************************
     * @param a the action to be added.
     * @return the button created from adding the provided action.
     **************************************************************************/
    public JButton addToToolbar( Action a )
    {
        return SwingUtils.addActionToToolbar( toolbar, a );
    }

    /***************************************************************************
     * Adds a separator to the toolbar.
     **************************************************************************/
    public void addToToolbar()
    {
        toolbar.addSeparator();
    }

    /***************************************************************************
     * @return a new iterator that visits every item.
     **************************************************************************/
    public Iterator<T> getItemIterator()
    {
        return itemsStream.iterator();
    }

    /***************************************************************************
     * @return the index of the selected item.
     **************************************************************************/
    public long getSelectedIndex()
    {
        return pageStartIndex + table.getSelectedRow();
    }

    /***************************************************************************
     * @return the total number of items.
     **************************************************************************/
    public long getItemCount()
    {
        return itemsStream.getCount();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void updateTable()
    {
        tableModel.fireTableDataChanged();

        ResizingTableModelListener.resizeTable( table );
    }

    /***************************************************************************
     * @param itemsPerPage
     **************************************************************************/
    public void setItemsPerPage( int itemsPerPage )
    {
        this.itemsPerPage = itemsPerPage;

        updateTable();
    }

    /***************************************************************************
     * @param clazz
     * @param renderer
     **************************************************************************/
    public void setDefaultRenderer( Class<?> clazz,
        ITableCellLabelDecorator renderer )
    {
        table.setDefaultRenderer( clazz,
            new LabelTableCellRenderer( renderer ) );
    }

    /***************************************************************************
     * @param column
     * @param renderer
     **************************************************************************/
    public void setCellRenderer( int column, ITableCellLabelDecorator renderer )
    {
        TableColumnModel colModel = table.getColumnModel();
        TableColumn c = colModel.getColumn( column );
        LabelTableCellRenderer r = new LabelTableCellRenderer( renderer );
        c.setCellRenderer( r );
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static final class ItemNavigationView<T> implements IDataView<T>
    {
        /**  */
        private final PaginatedTableView<T> itemsView;
        /**  */
        private final IDataView<T> itemView;

        /**  */
        private final JPanel view;

        /**  */
        private final JButton prevButton;
        /**  */
        private final JButton nextButton;

        /**
         * @param msgsView
         * @param msgView
         * @param addScrollPane
         */
        public ItemNavigationView( PaginatedTableView<T> msgsView,
            IDataView<T> itemView )
        {
            this.itemsView = msgsView;
            this.itemView = itemView;
            this.prevButton = new JButton();
            this.nextButton = new JButton();

            this.view = createView();

            setButtonsEnabled();
        }

        /**
         * @return
         */
        private JPanel createView()
        {
            JPanel panel = new JPanel( new BorderLayout() );

            panel.add( createToolbar(), BorderLayout.NORTH );
            panel.add( itemView.getView(), BorderLayout.CENTER );

            return panel;
        }

        /**
         * @return
         */
        private JToolBar createToolbar()
        {
            JToolBar toolbar = new JToolBar();

            SwingUtils.setToolbarDefaults( toolbar );

            SwingUtils.addActionToToolbar( toolbar, createNavAction( false ),
                prevButton );

            SwingUtils.addActionToToolbar( toolbar, createNavAction( true ),
                nextButton );

            return toolbar;
        }

        /**
         * @param forward
         * @return
         */
        private Action createNavAction( boolean forward )
        {
            ActionListener listener = ( e ) -> navigate( forward );
            Icon icon = IconConstants.getIcon(
                forward ? IconConstants.NAV_NEXT_16
                    : IconConstants.NAV_PREVIOUS_16 );
            String name = forward ? "Next Message" : "Previous Message";

            return new ActionAdapter( listener, name, icon );
        }

        /**
         * @param forward
         */
        private void navigate( boolean forward )
        {
            int inc = forward ? 1 : -1;
            long index = itemsView.table.getSelectedRow() +
                itemsView.pageStartIndex;
            long nextIndex = index + inc;

            itemsView.showItem( nextIndex );

            setButtonsEnabled();
        }

        /**
         * 
         */
        private void setButtonsEnabled()
        {
            long index = itemsView.table.getSelectedRow() +
                itemsView.pageStartIndex;
            long maxRow = itemsView.getItemCount() - 1;

            prevButton.setEnabled( index > 0 );
            nextButton.setEnabled( index > -1 && index < maxRow );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JPanel getView()
        {
            return view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public T getData()
        {
            return itemView.getData();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setData( T data )
        {
            itemView.setData( data );

            setButtonsEnabled();
        }
    }
}
