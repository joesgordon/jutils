package org.jutils.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jutils.*;
import org.jutils.io.*;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.model.*;
import org.jutils.ui.model.LabelTableCellRenderer.ITableCellLabelDecorator;
import org.jutils.ui.net.StringWriterView;

/*******************************************************************************
 * Defines UI that displays a paginated table of items.
 * @param <T> the type of item to be displayed.
 ******************************************************************************/
public class RefStreamView<T> implements IDataView<IReferenceStream<T>>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ITableItemsConfig<T> tableConfig;
    /**  */
    private final ItemsTableModel<T> tableModel;
    /**  */
    private final JTable table;
    /**  */
    private final JScrollPane tablePane;
    /**  */
    private final RowHeaderNumberView rowView;

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
    private final JButton openButton;

    /**  */
    private OkDialogView dialog;
    /**  */
    private final NavView<T> itemView;

    /**  */
    private IReferenceStream<T> stream;

    /**  */
    private int itemsPerPage;
    /**  */
    private long pageStartIndex;

    /***************************************************************************
     * @param serializer the serializer that reads/writes items.
     * @param tableConfig defines the columns to be shown.
     **************************************************************************/
    public RefStreamView( IDataSerializer<T> serializer,
        ITableItemsConfig<T> tableConfig )
    {
        this( serializer, tableConfig, null, false );
    }

    /***************************************************************************
     * @param serializer the serializer that reads/writes items.
     * @param tableConfig defines the columns to be shown.
     * @param itemWriter writes a description of an item.
     **************************************************************************/
    public RefStreamView( IDataSerializer<T> serializer,
        ITableItemsConfig<T> tableConfig, IStringWriter<T> itemWriter )
    {
        this( serializer, tableConfig, createItemWriterView( itemWriter ),
            true );
    }

    /***************************************************************************
     * @param serializer the serializer that reads/writes items.
     * @param tableConfig defines the columns to be shown.
     * @param itemView displays the contents of an item.
     * @param addScrollPane optionally adds the item view to a scroll pane.
     **************************************************************************/
    public RefStreamView( IDataSerializer<T> serializer,
        ITableItemsConfig<T> tableConfig, IDataView<T> itemView,
        boolean addScrollPane )
    {
        ReferenceStream<T> refStream = null;

        try
        {
            refStream = new ReferenceStream<T>( serializer );
        }
        catch( IOException ex )
        {
            throw new RuntimeException( "Unable to create temp files", ex );
        }

        this.stream = refStream;

        this.tableConfig = tableConfig;
        this.tableModel = new ItemsTableModel<>( tableConfig );
        this.table = new JTable( tableModel );
        this.tablePane = new JScrollPane( table );
        this.rowView = new RowHeaderNumberView( table );
        this.navFirstButton = new JButton();
        this.navPreviousButton = new JButton();
        this.navNextButton = new JButton();
        this.navLastButton = new JButton();
        this.pageLabel = new JLabel( "Page 0 of 0" );
        this.openButton = new JButton();
        this.itemView = new NavView<>( this, itemView, addScrollPane );
        this.view = createView();

        this.itemsPerPage = 500;
        this.pageStartIndex = 0L;

        setOpenVisible( false );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IReferenceStream<T> getData()
    {
        return stream;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( IReferenceStream<T> stream )
    {
        this.stream = stream;

        pageStartIndex = 0L;
        navigatePage( 0L, true );
        ResizingTableModelListener.resizeTable( table );
    }

    /***************************************************************************
     * @return the main view for this control.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        table.setDefaultRenderer( LocalDateTime.class,
            new LabelTableCellRenderer( new LocalDateTimeDecorator() ) );
        table.getTableHeader().setReorderingAllowed( false );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.addMouseListener( new ItemMouseListener<>( this ) );

        TableColumnModel colModel = table.getColumnModel();
        int lastColIdx = tableConfig.getColumnNames().length - 1;
        TableColumn column = colModel.getColumn( lastColIdx );
        LabelTableCellRenderer renderer = new LabelTableCellRenderer(
            new FontLabelTableCellRenderer( SwingUtils.getFixedFont( 12 ) ) );
        column.setCellRenderer( renderer );

        JScrollBar vScrollBar = tablePane.getVerticalScrollBar();

        vScrollBar.addAdjustmentListener( new BottomScroller( vScrollBar ) );

        tablePane.setRowHeaderView( rowView.getView() );

        tablePane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        tablePane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );

        // panel.setBorder( new TitledBorder( "Sent/Received Items" ) );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( tablePane, BorderLayout.CENTER );

        panel.setMinimumSize( new Dimension( 625, 200 ) );
        panel.setPreferredSize( new Dimension( 625, 200 ) );

        return panel;
    }

    /***************************************************************************
     * @return the toolbar displayed above the table of items
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

        SwingUtils.addActionToToolbar( toolbar, createSaveAction() );

        SwingUtils.addActionToToolbar( toolbar, createOpenAction(),
            openButton );

        toolbar.addSeparator();

        SwingUtils.addActionToToolbar( toolbar, createClearAction() );

        return toolbar;
    }

    /***************************************************************************
     * Creates an action to move forward or backward, by either 1 or to the
     * beginning/end.
     * @param absolute moves to the beginning/end if {@code true} or by 1 page
     * if {@code false}.
     * @param forward moves forward if {@code true}; backward if {@code false}.
     * @return the new action.
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
     * Creates an action that saves all the items to a file.
     * @return
     **************************************************************************/
    private Action createSaveAction()
    {
        IFileSelected ifs = ( f ) -> saveFile( f );
        FileChooserListener listener = new FileChooserListener( getView(),
            "Choose File", true, ifs );
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );

        return new ActionAdapter( listener, "Save", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createOpenAction()
    {
        IFileSelected ifs = ( f ) -> openFile( f );
        FileChooserListener listener = new FileChooserListener( getView(),
            "Choose File", false, ifs );
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );

        return new ActionAdapter( listener, "Open", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createClearAction()
    {
        ActionListener listener = ( e ) -> clearItems();
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_CLEAR_16 );

        return new ActionAdapter( listener, "Clear", icon );
    }

    /***************************************************************************
     * @param absolute
     * @param forward
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
            index = stream.getCount() - 1;
            index = index - index % itemsPerPage;
        }

        if( index > -1 && index < stream.getCount() )
        {
            navigatePage( index );
        }
    }

    /***************************************************************************
     * @param page
     **************************************************************************/
    private void navigatePage( long startIndex )
    {
        navigatePage( startIndex, false );
    }

    /***************************************************************************
     * @param startIndex
     * @param ignoreIndexCheck
     **************************************************************************/
    private void navigatePage( long startIndex, boolean ignoreIndexCheck )
    {
        if( !ignoreIndexCheck && this.pageStartIndex == startIndex )
        {
            return;
        }

        int count = ( int )Math.min( itemsPerPage,
            stream.getCount() - startIndex );

        // LogUtils.printDebug( "Setting start index to %d from %d of %d for
        // %d",
        // startIndex, pageStartIndex, stream.getCount(), count );

        if( startIndex < 0 || startIndex >= stream.getCount() )
        {
            return;
        }

        this.pageStartIndex = startIndex;
        try
        {
            List<T> items = null;
            synchronized( stream )
            {
                items = stream.read( pageStartIndex, count );
            }
            tableModel.setItems( items );
            updateRowHeader( count );
            ResizingTableModelListener.resizeTable( table );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }
        catch( ValidationException ex )
        {
            ex.printStackTrace();
        }

        setNavButtonsEnabled();
    }

    /***************************************************************************
     * @param count
     **************************************************************************/
    private void updateRowHeader( int count )
    {
        rowView.setData( pageStartIndex, count );
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

        pageLabel.setText( String.format( "Page %d of %d (%d items)", pageNum,
            pageCount, stream.getCount() ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private boolean hasPrevious()
    {
        return pageStartIndex > 0;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private boolean hasNext()
    {
        long lastItem = stream.getCount() - 1;
        long maxStartIndex = lastItem - lastItem % itemsPerPage;
        return pageStartIndex < maxStartIndex;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private int getPageCount()
    {
        long count = stream.getCount();

        int max = ( int )( ( count + itemsPerPage - 1 ) / itemsPerPage );

        return max;
    }

    /***************************************************************************
     * @param itemIndex
     **************************************************************************/
    private void showItem( long itemIndex )
    {
        if( itemIndex < 0 || itemIndex >= stream.getCount() )
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
            d.setSize( 675, 400 );
            d.setLocationRelativeTo( f );
        }

        d.setTitle( String.format( "Item %d", itemIndex + 1 ) );
        d.setVisible( true );

        table.setRowSelectionInterval( tableIndex, tableIndex );
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    private boolean isPaged( long index )
    {
        return index > pageStartIndex &&
            index < ( pageStartIndex + itemsPerPage );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void saveFile( File file )
    {
        byte [] buf = new byte[IOUtils.DEFAULT_BUF_SIZE];

        synchronized( stream )
        {
            try( FileStream stream = new FileStream( file ) )
            {
                @SuppressWarnings( "resource")
                IStream input = this.stream.getItemsStream();

                input.seek( 0L );

                long length = input.getLength();
                long written = 0;

                while( written < length )
                {
                    int count = input.read( buf );

                    stream.write( buf, 0, count );

                    written += count;
                }
            }
            catch( FileNotFoundException ex )
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            catch( IOException ex )
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
        }
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void openFile( File file )
    {
        clearItems();

        try
        {
            synchronized( stream )
            {
                stream.setItemsFile( file );
            }

            setData( stream );
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
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
     * @param item
     **************************************************************************/
    public void addItem( T item )
    {
        long count = stream.getCount();
        long lastStartIndex = Math.max( count - 1, 0 );
        lastStartIndex = lastStartIndex - lastStartIndex % itemsPerPage;

        try
        {
            synchronized( stream )
            {
                stream.write( item );
            }
            count++;
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }

        long nextPageStartIndex = pageStartIndex + itemsPerPage;

        // LogUtils.printDebug(
        // "Adding item; last start = %d, next start = %d, current start =
        // %d, count = %d",
        // lastStartIndex, nextIndex, pageStartIndex, stream.getCount() );

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
     * @return
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
     * 
     **************************************************************************/
    public void clearItems()
    {
        tableModel.clearItems();

        try
        {
            synchronized( stream )
            {
                stream.removeAll();
            }
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
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
     * @param visible
     **************************************************************************/
    public void setOpenVisible( boolean visible )
    {
        openButton.setVisible( visible );
    }

    /***************************************************************************
     * @param itemWriter
     * @return
     **************************************************************************/
    private static <T> StringWriterView<T> createItemWriterView(
        IStringWriter<T> itemWriter )
    {
        return itemWriter == null ? null : new StringWriterView<>( itemWriter );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ItemMouseListener<T> extends MouseAdapter
    {
        /**  */
        private final RefStreamView<T> view;

        /**
         * @param view
         */
        public ItemMouseListener( RefStreamView<T> view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( SwingUtilities.isLeftMouseButton( e ) &&
                e.getClickCount() == 2 )
            {
                long index = view.table.rowAtPoint( e.getPoint() ) +
                    view.pageStartIndex;

                view.showItem( index );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class LocalDateTimeDecorator
        implements ITableCellLabelDecorator
    {
        /**  */
        private final DateTimeFormatter dtf;

        /**
         * 
         */
        public LocalDateTimeDecorator()
        {
            this.dtf = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss.SSS" );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col )
        {
            String text = "";

            if( value != null )
            {
                LocalDateTime ldt = ( LocalDateTime )value;
                text = ldt.format( dtf );
            }

            label.setText( text );
        }
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static final class NavView<T> implements IDataView<T>
    {
        /**  */
        private final RefStreamView<T> itemsView;

        /**  */
        private final JPanel view;
        /**  */
        private final IDataView<T> itemView;

        /**  */
        private final JButton prevButton;
        /**  */
        private final JButton nextButton;

        /**
         * @param itemsView
         * @param itemView
         * @param addScrollPane
         */
        public NavView( RefStreamView<T> itemsView, IDataView<T> itemView,
            boolean addScrollPane )
        {
            this.itemsView = itemsView;
            this.itemView = itemView;
            this.prevButton = new JButton();
            this.nextButton = new JButton();

            this.view = createView( addScrollPane );

            setButtonsEnabled();
        }

        /**
         * @param addScrollPane
         * @return
         */
        private JPanel createView( boolean addScrollPane )
        {
            JPanel panel = new JPanel( new BorderLayout() );
            Component centerComp = itemView.getView();

            if( addScrollPane )
            {
                JScrollPane pane = new JScrollPane( centerComp );

                pane.getVerticalScrollBar().setUnitIncrement( 10 );

                centerComp = pane;
            }

            panel.add( createToolbar(), BorderLayout.NORTH );
            panel.add( centerComp, BorderLayout.CENTER );

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
            String name = forward ? "Next" : "Previous";

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
            long maxRow = itemsView.stream.getCount() - 1;

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

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FontLabelTableCellRenderer
        implements ITableCellLabelDecorator
    {
        /**  */
        private final Font font;

        /**
         * @param font
         */
        public FontLabelTableCellRenderer( Font font )
        {
            this.font = font;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col )
        {
            label.setFont( font );
        }
    }
}
