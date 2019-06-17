package org.jutils.ui.explorer;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.*;

import org.jutils.ui.FileContextMenu;
import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class ExplorerTable implements IView<JTable>
{
    /**  */
    private final JTable table;
    /**  */
    private final ExplorerTableModel model;

    /***************************************************************************
     *
     **************************************************************************/
    public ExplorerTable()
    {
        this( true );
    }

    /***************************************************************************
    *
    **************************************************************************/
    public ExplorerTable( boolean showPath )
    {
        this.model = new ExplorerTableModel( showPath );
        this.table = new JTable( model );

        table.setShowGrid( false );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        // this.setAutoCreateRowSorter( true );
        table.setRowHeight( 21 );

        table.getTableHeader().setReorderingAllowed( false );

        table.addMouseListener( new TableMouseListener( this ) );

        clearTable();
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public JTable getView()
    {
        return table;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ExplorerTableModel getExplorerTableModel()
    {
        return model;
    }

    /***************************************************************************
     *
     **************************************************************************/
    public void clearTable()
    {
        TableColumnModel columnModel = table.getColumnModel();
        int idx = 0;
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();

        rightRenderer.setHorizontalAlignment( SwingConstants.RIGHT );

        columnModel.getColumn( idx ).setCellRenderer( new FilenameRenderer() );
        columnModel.getColumn( idx++ ).setPreferredWidth( 200 );

        if( model.isShowingPath() )
        {
            columnModel.getColumn( idx++ ).setPreferredWidth( 200 );
        }

        columnModel.getColumn( idx ).setPreferredWidth( 50 );
        columnModel.getColumn( idx++ ).setCellRenderer( new SizeRenderer() );

        columnModel.getColumn( idx++ ).setPreferredWidth( 150 );

        columnModel.getColumn( idx ).setPreferredWidth( 115 );
        columnModel.getColumn( idx++ ).setCellRenderer( rightRenderer );

        model.clearModel();
    }

    /***************************************************************************
     * @param file File
     **************************************************************************/
    public void addFile( File file )
    {
        model.addFile( file );
    }

    /***************************************************************************
     * @param files File[]
     **************************************************************************/
    public void addFiles( List<? extends IExplorerItem> files )
    {
        model.addFiles( files );
    }

    /***************************************************************************
     * @return File
     **************************************************************************/
    public File getSelectedFile()
    {
        File file = null;
        int row = table.getSelectedRow();

        if( row > -1 && row < model.getRowCount() )
        {
            row = table.convertRowIndexToModel( row );
            IExplorerItem item = model.getExplorerItem( row );
            file = item.getFile();
        }

        return file;
    }

    /***************************************************************************
     * @return ExplorerItem
     **************************************************************************/
    public IExplorerItem getSelectedItem()
    {
        IExplorerItem item = null;
        int row = table.getSelectedRow();

        if( row > -1 && row < model.getRowCount() )
        {
            row = table.convertRowIndexToModel( row );
            item = model.getExplorerItem( row );
        }

        return item;
    }

    private static final class SizeRenderer implements TableCellRenderer
    {
        private final DefaultTableCellRenderer renderer;

        public SizeRenderer()
        {
            this.renderer = new DefaultTableCellRenderer();
            renderer.setHorizontalAlignment( SwingConstants.RIGHT );
        }

        @Override
        public Component getTableCellRendererComponent( JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column )
        {
            renderer.getTableCellRendererComponent( table, value, isSelected,
                hasFocus, row, column );

            Long val = ( Long )value;

            if( val < 0 )
            {
                renderer.setText( "" );
            }

            return renderer;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FilenameRenderer implements TableCellRenderer
    {
        private static final FileSystemView view = FileSystemView.getFileSystemView();
        private final DefaultTableCellRenderer renderer;

        public FilenameRenderer()
        {
            this.renderer = new DefaultTableCellRenderer();
        }

        @Override
        public Component getTableCellRendererComponent( JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column )
        {
            renderer.getTableCellRendererComponent( table, value, isSelected,
                hasFocus, row, column );

            if( value instanceof IExplorerItem )
            {
                File file = ( ( IExplorerItem )value ).getFile();
                if( file != null && file.exists() )
                {
                    Icon icon = view.getSystemIcon( file );
                    renderer.setIcon( icon );
                }
                else
                {
                    renderer.setIcon( null );
                }
            }
            else
            {
                renderer.setIcon( null );
            }

            return renderer;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TableMouseListener extends MouseAdapter
    {
        private final ExplorerTable etable;
        private final FileContextMenu menu;

        public TableMouseListener( ExplorerTable etable )
        {
            this.etable = etable;
            this.menu = new FileContextMenu( etable.table );
        }

        @Override
        public void mouseReleased( MouseEvent e )
        {
            int r = etable.table.rowAtPoint( e.getPoint() );

            if( r > -1 && r < etable.table.getRowCount() )
            {
                etable.table.setRowSelectionInterval( r, r );

                r = etable.table.convertRowIndexToModel( r );

                if( SwingUtilities.isRightMouseButton( e ) &&
                    e.getComponent() instanceof JTable )
                {
                    IExplorerItem iei = etable.model.getExplorerItem( r );
                    menu.show( iei.getFile(), e.getComponent(), e.getX(),
                        e.getY() );
                }
            }
            // else
            // {
            // etable.table.clearSelection();
            // }
        }
    }
}
