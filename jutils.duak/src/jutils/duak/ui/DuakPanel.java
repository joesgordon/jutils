package jutils.duak.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.ui.MessageExceptionView;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.ItemActionList;
import jutils.core.ui.event.ItemActionListener;
import jutils.core.ui.event.ResizingTableModelListener;
import jutils.core.ui.model.IDataView;
import jutils.core.ui.model.ItemsTableModel;
import jutils.duak.data.FileInfo;
import jutils.duak.utils.FileSize;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DuakPanel implements IDataView<FileInfo>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JLabel locationLabel;
    /**  */
    private final JLabel totalLabel;
    /**  */
    private final FileResultsTableConfig tableConfig;
    /**  */
    private final ItemsTableModel<FileInfo> tableModel;
    /**  */
    private final JTable table;
    /**  */
    private final ItemActionList<FileInfo> folderOpenedListeners;

    /**  */
    private FileInfo info;

    /***************************************************************************
     * 
     **************************************************************************/
    public DuakPanel()
    {
        this.view = new JPanel( new GridBagLayout() );

        this.folderOpenedListeners = new ItemActionList<FileInfo>();

        this.locationLabel = new JLabel();
        this.totalLabel = new JLabel();
        this.tableConfig = new FileResultsTableConfig();

        this.tableModel = new ItemsTableModel<>( tableConfig );

        this.table = new JTable( tableModel );

        ResizingTableModelListener.addToTable( table );

        JScrollPane tableScrollPane = new JScrollPane( table );

        tableScrollPane.getViewport().setBackground( Color.white );

        TableRowSorter<ItemsTableModel<FileInfo>> sorter;

        sorter = new TableRowSorter<>( tableModel );
        // sorter.setComparator( 1, new FileResultsComparer() );
        RowSorter.SortKey [] keys = new RowSorter.SortKey[] {
            new RowSorter.SortKey( 1, SortOrder.DESCENDING ) };
        sorter.setSortKeys( Arrays.asList( keys ) );
        // sorter.toggleSortOrder( 1 );

        table.setRowSorter( sorter );
        table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        table.setShowGrid( false );
        // table.setBackground( getBackground() );
        table.getTableHeader().setReorderingAllowed( false );
        table.setDefaultRenderer( FileInfo.class,
            new FileResultTableCellRenderer() );
        table.addMouseListener( new FolderOpenedListener() );

        view.add( locationLabel,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 4, 4, 2, 4 ), 0, 0 ) );

        view.add( totalLabel,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 4, 4, 2, 4 ), 0, 0 ) );

        view.add( tableScrollPane,
            new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPopupMenu createPopup()
    {
        JPopupMenu menu = new JPopupMenu();
        Action action;

        action = new ActionAdapter( ( e ) -> handleOpenFile(), "Open File",
            IconConstants.getIcon( IconConstants.OPEN_FILE_16 ) );
        menu.add( action );

        action = new ActionAdapter( ( e ) -> handleOpenLocation(),
            "Open Location",
            IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        menu.add( action );

        return menu;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleOpenFile()
    {
        int row = table.getSelectedRow();

        if( row > -1 )
        {
            FileInfo fi = tableModel.getItem( row );

            try
            {
                Desktop desktop = Desktop.getDesktop();
                desktop.open( fi.getFile() );
            }
            catch( IOException ex )
            {
                MessageExceptionView.showExceptionDialog( getView(),
                    "Cannot open file " + fi.getFile().getName(), "I/O Error",
                    ex );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleOpenLocation()
    {
        int row = table.getSelectedRow();

        if( row > -1 )
        {
            FileInfo fi = tableModel.getItem( row );

            try
            {
                Desktop desktop = Desktop.getDesktop();
                desktop.open( fi.getFile().getAbsoluteFile().getParentFile() );
            }
            catch( IOException ex )
            {
                MessageExceptionView.showExceptionDialog( getView(),
                    "Cannot open file " + fi.getFile().getName(), "I/O Error",
                    ex );
            }
        }

    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addFolderOpenedListener( ItemActionListener<FileInfo> l )
    {
        folderOpenedListeners.addListener( l );
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public FileInfo getData()
    {
        return info;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( FileInfo results )
    {
        this.info = results;

        locationLabel.setText( results.getFile().getAbsolutePath() );

        totalLabel.setText( new FileSize( results.getSize() ).toString() );

        tableModel.setItems( results.getChildren() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class FolderOpenedListener extends MouseAdapter
    {
        /**  */
        private final JPopupMenu menu;

        /**
         * 
         */
        public FolderOpenedListener()
        {
            JPopupMenu popup = null;
            if( Desktop.isDesktopSupported() )
            {
                popup = createPopup();
            }
            this.menu = popup;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mousePressed( MouseEvent e )
        {
            if( SwingUtilities.isRightMouseButton( e ) )
            {
                selectRowAt( e.getPoint() );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased( MouseEvent e )
        {
            if( SwingUtilities.isRightMouseButton( e ) )
            {
                selectRowAt( e.getPoint() );
            }
        }

        /**
         * @param p
         */
        private void selectRowAt( Point p )
        {
            int row = table.rowAtPoint( p );

            if( row > -1 )
            {
                table.setRowSelectionInterval( row, row );
                menu.show( table, ( int )p.getX(), ( int )p.getY() );
            }
            else
            {
                table.clearSelection();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked( MouseEvent e )
        {
            int row = table.rowAtPoint( e.getPoint() );

            if( e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 )
            {
                if( row > -1 )
                {
                    row = table.convertRowIndexToModel( row );
                    FileInfo fi = tableModel.getItem( row );
                    File f = fi.getFile();

                    if( f.isFile() )
                    {
                        try
                        {
                            Desktop.getDesktop().open( f );
                        }
                        catch( IOException ex )
                        {
                            MessageExceptionView.showExceptionDialog( table,
                                "Cannot open file: " + f.getName(), "I/O Error",
                                ex );
                        }
                    }
                    else
                    {
                        folderOpenedListeners.fireListeners( DuakPanel.this,
                            fi );
                    }
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileResultTableCellRenderer
        extends DefaultTableCellRenderer
    {
        /**  */
        private static final long serialVersionUID = 1L;

        /**  */
        private final FileIconLoader iconLoader;

        /**
         * 
         */
        public FileResultTableCellRenderer()
        {
            iconLoader = new FileIconLoader();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Component getTableCellRendererComponent( JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column )
        {
            Component c = super.getTableCellRendererComponent( table, value,
                isSelected, hasFocus, row, column );

            if( !( value instanceof FileInfo ) )
            {
                setText( value.toString() );
            }
            else
            {
                FileInfo fileResource = ( FileInfo )value;
                File file = fileResource.getFile();

                setIcon( iconLoader.getSystemIcon( file ) );
                setText( iconLoader.getSystemName( file ) );
            }

            return c;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileIconLoader
    {
        /**  */
        private final FileSystemView fsv;
        /**  */
        private final Map<File, Icon> iconMap;

        /**
         * 
         */
        public FileIconLoader()
        {
            this.fsv = FileSystemView.getFileSystemView();
            this.iconMap = new HashMap<File, Icon>();
        }

        /**
         * @param file
         * @return
         */
        public String getSystemName( File file )
        {
            return fsv.getSystemDisplayName( file );
        }

        /**
         * @param file
         * @return
         */
        public Icon getSystemIcon( File file )
        {
            Icon icon = iconMap.get( file );

            if( icon == null )
            {
                icon = SwingUtils.getFileIcon( fsv, file );
            }

            return icon;
        }
    }
}
