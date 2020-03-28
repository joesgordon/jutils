package org.duak.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.duak.data.FileInfo;
import org.duak.utils.FileSize;
import org.jutils.core.IconConstants;
import org.jutils.core.ui.MessageExceptionView;
import org.jutils.core.ui.event.*;
import org.jutils.core.ui.model.IDataView;
import org.jutils.core.ui.model.ItemsTableModel;

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

        action = new ActionAdapter( new OpenFileListener( this ), "Open File",
            IconConstants.getIcon( IconConstants.OPEN_FILE_16 ) );
        menu.add( action );

        action = new ActionAdapter( new OpenLocationListener( this ),
            "Open Location",
            IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        menu.add( action );

        return menu;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addFolderOpenedListener( ItemActionListener<FileInfo> l )
    {
        folderOpenedListeners.addListener( l );
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
    public FileInfo getData()
    {
        return info;
    }

    /***************************************************************************
     * 
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
        private final JPopupMenu menu;

        public FolderOpenedListener()
        {
            JPopupMenu popup = null;
            if( Desktop.isDesktopSupported() )
            {
                popup = createPopup();
            }
            this.menu = popup;
        }

        @Override
        public void mousePressed( MouseEvent e )
        {
            if( SwingUtilities.isRightMouseButton( e ) )
            {
                selectRowAt( e.getPoint() );
            }
        }

        @Override
        public void mouseReleased( MouseEvent e )
        {
            if( SwingUtilities.isRightMouseButton( e ) )
            {
                selectRowAt( e.getPoint() );
            }
        }

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
    private static class OpenFileListener implements ActionListener
    {
        private final DuakPanel view;
        private final Desktop desktop;

        public OpenFileListener( DuakPanel panel )
        {
            this.view = panel;
            this.desktop = Desktop.getDesktop();
        }

        @Override
        public void actionPerformed( ActionEvent event )
        {
            int row = view.table.getSelectedRow();

            if( row > -1 )
            {
                FileInfo fi = view.tableModel.getItem( row );

                try
                {
                    desktop.open( fi.getFile() );
                }
                catch( IOException ex )
                {
                    MessageExceptionView.showExceptionDialog( view.getView(),
                        "Cannot open file " + fi.getFile().getName(),
                        "I/O Error", ex );
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenLocationListener implements ActionListener
    {
        private final DuakPanel panel;
        private final Desktop desktop;

        public OpenLocationListener( DuakPanel panel )
        {
            this.panel = panel;
            this.desktop = Desktop.getDesktop();
        }

        @Override
        public void actionPerformed( ActionEvent event )
        {
            int row = panel.table.getSelectedRow();

            if( row > -1 )
            {
                FileInfo fi = panel.tableModel.getItem( row );

                try
                {
                    desktop.open(
                        fi.getFile().getAbsoluteFile().getParentFile() );
                }
                catch( IOException ex )
                {
                    MessageExceptionView.showExceptionDialog( panel.getView(),
                        "Cannot open file " + fi.getFile().getName(),
                        "I/O Error", ex );
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
        private static final long serialVersionUID = 1L;
        private final FileIconLoader iconLoader;

        public FileResultTableCellRenderer()
        {
            iconLoader = new FileIconLoader();
        }

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
        private final FileSystemView fsv;
        private final Map<File, Icon> iconMap;
        private final Icon defaultIcon;

        public FileIconLoader()
        {
            fsv = FileSystemView.getFileSystemView();
            iconMap = new HashMap<File, Icon>();
            defaultIcon = IconConstants.getIcon( IconConstants.OPEN_FILE_16 );
        }

        public String getSystemName( File file )
        {
            return fsv.getSystemDisplayName( file );
        }

        public Icon getSystemIcon( File file )
        {
            Icon icon = iconMap.get( file );

            if( icon == null )
            {
                icon = fsv.getSystemIcon( file );

                if( icon == null )
                {
                    icon = defaultIcon;
                }
                iconMap.put( file, icon );
            }

            return icon;
        }
    }
}
