package jutils.core.ui.explorer;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import jutils.core.OptionUtils;
import jutils.core.Utils;
import jutils.core.io.FileComparator;
import jutils.core.io.LogUtils;
import jutils.core.ui.FileContextMenu;
import jutils.core.ui.model.IView;
import jutils.core.ui.model.LabelTableCellRenderer;
import jutils.core.ui.model.LabelTableCellRenderer.ITableCellLabelDecorator;

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
     * @param showPath
     **************************************************************************/
    public ExplorerTable( boolean showPath )
    {
        this.model = new ExplorerTableModel( showPath );
        this.table = new JTable( model );

        table.setShowGrid( false );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        // this.setAutoCreateRowSorter( true );
        table.setRowHeight( 24 );

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

        columnModel.getColumn( idx ).setCellRenderer(
            new LabelTableCellRenderer( new FilenameRenderer() ) );
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

    /***************************************************************************
     * @param dir
     **************************************************************************/
    public void setDirectory( File dir )
    {
        List<DefaultExplorerItem> list = new ArrayList<>();
        File [] children = dir.listFiles();

        LogUtils.printDebug( "Setting directory to " + dir );

        if( children != null )
        {
            Arrays.sort( children, new FileComparator() );

            for( int i = 0; i < children.length; i++ )
            {
                list.add( new DefaultExplorerItem( children[i] ) );
            }

            clearTable();
            addFiles( list );
        }
        else
        {
            OptionUtils.showErrorMessage( getView(),
                "User does not have permissions to view: " + Utils.NEW_LINE +
                    dir.getAbsolutePath(),
                "ERROR" );
        }

        // {
        // File file = new File( dir, ".project" );
        // Icon icon = FilenameRenderer.view.getSystemIcon( file );
        // JLabel label = new JLabel( "<- Icon!!" );
        // label.setIcon( icon );
        // OptionUtils.showInfoMessage( getView(), label, "Here's the icon" );
        // }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class SizeRenderer implements TableCellRenderer
    {
        /**  */
        private final DefaultTableCellRenderer renderer;

        /**
         * 
         */
        public SizeRenderer()
        {
            this.renderer = new DefaultTableCellRenderer();
            renderer.setHorizontalAlignment( SwingConstants.RIGHT );
        }

        /**
         * {@inheritDoc}
         */
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
    private static final class FilenameRenderer
        implements ITableCellLabelDecorator
    {
        /**  */
        private static final FileSystemView view = FileSystemView.getFileSystemView();

        /**
         * 
         */
        public FilenameRenderer()
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col )
        {
            Icon icon = null;

            if( value instanceof IExplorerItem )
            {
                IExplorerItem item = ( IExplorerItem )value;
                icon = item.getIcon( view );
            }

            label.setIcon( icon );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TableMouseListener extends MouseAdapter
    {
        /**  */
        private final ExplorerTable etable;
        /**  */
        private final FileContextMenu menu;

        /**
         * @param etable
         */
        public TableMouseListener( ExplorerTable etable )
        {
            this.etable = etable;
            this.menu = new FileContextMenu( etable.table );
        }

        /**
         * {@inheritDoc}
         */
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
