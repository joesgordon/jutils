package jutils.core.ui;

import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import jutils.core.ui.RowHeaderView.IRowHeaderModel;
import jutils.core.ui.RowHeaderView.IRowListModel;
import jutils.core.ui.event.ResizingTableModelListener;
import jutils.core.ui.event.RunnableList;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TabularView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final JScrollPane pane;
    /**  */
    private final JTable table;
    /**  */
    private final TabularTableModel tableModel;
    /**  */
    private final RowHeaderView rowHeader;
    /**  */
    private final TabularRowHeaderModel rowModel;

    /***************************************************************************
     * 
     **************************************************************************/
    public TabularView()
    {
        this.tableModel = new TabularTableModel();
        this.table = new JTable( tableModel );
        this.pane = new JScrollPane( table );

        this.rowHeader = new RowHeaderView( table );
        this.rowModel = new TabularRowHeaderModel();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION );
        table.setCellSelectionEnabled( true );

        rowHeader.setModel( rowModel );

        pane.getVerticalScrollBar().setUnitIncrement( 10 );
        pane.setRowHeaderView( rowHeader.getView() );

        return pane;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void resize()
    {
        ResizingTableModelListener.resizeTable( table );
    }

    /***************************************************************************
     * @param model
     **************************************************************************/
    public void setModel( ITabularModel model )
    {
        tableModel.setModel( model );
        rowModel.setModel( model );

        IRowListModel listModel = rowHeader.getListModel();

        model.registerNotifier(
            new AbstractTableNotifier( tableModel, listModel ) );

        tableModel.fireUpdates();
        rowModel.fireUpdates();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSelectedRow()
    {
        return table.getSelectedRow();
    }

    /***************************************************************************
     * @param row
     **************************************************************************/
    public void setSelectedRow( int row )
    {
        setSelectedRows( row, row );
    }

    /***************************************************************************
     * @param rowStart
     * @param rowEnd
     **************************************************************************/
    public void setSelectedRows( int rowStart, int rowEnd )
    {
        table.getSelectionModel().setSelectionInterval( rowStart, rowEnd );
        scrollToRow( rowStart );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int [] getSelectedRows()
    {
        return table.getSelectedRows();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSelectedIndex()
    {
        int colCount = tableModel.getColumnCount();
        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();
        return row * colCount + col;
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public void setSelectedIndex( int index )
    {
        table.getSelectionModel().setSelectionInterval( index, index );
    }

    /***************************************************************************
     * @param row
     **************************************************************************/
    private void scrollToRow( int row )
    {
        Rectangle rect = table.getCellRect( row, 0, true );

        rect = new Rectangle( rect );
        table.scrollRectToVisible( rect );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Font getFont()
    {
        return table.getFont();
    }

    /***************************************************************************
     * @param f
     **************************************************************************/
    public void setFont( Font f )
    {
        table.setFont( f );
    }

    /***************************************************************************
     * @param height
     **************************************************************************/
    public void setRowHeight( int height )
    {
        table.setRowHeight( height );
        rowHeader.setRowHeight( 36 );
    }

    /***************************************************************************
     * @param showGrid
     **************************************************************************/
    public void setShowGrid( boolean showGrid )
    {
        table.setShowGrid( showGrid );
    }

    /***************************************************************************
     * @param col
     * @param renderer
     **************************************************************************/
    public void setColumnRenderer( int col, TableCellRenderer renderer )
    {
        table.getColumnModel().getColumn( col ).setCellRenderer( renderer );
    }

    /***************************************************************************
     * @param cls
     * @param renderer
     **************************************************************************/
    public void setClassRenderer( Class<?> cls, TableCellRenderer renderer )
    {
        table.setDefaultRenderer( cls, renderer );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTable getTable()
    {
        return table;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JScrollPane getPane()
    {
        return pane;
    }

    /***************************************************************************
     *
     **************************************************************************/
    public static interface ITabularNotifier
    {
        /**
         * @param row
         * @param col
         */
        public void fireCellUpdated( int row, int col );

        /**
         * 
         */
        public void fireDataChanged();

        /**
         * @param rowStart
         * @param rowEnd
         */
        public void fireRowsDeleted( int rowStart, int rowEnd );

        /**
         * Notifies all listeners that rows in the range [rowStart, rowEnd],
         * inclusive, have been inserted.
         * @param rowStart
         * @param rowEnd
         */
        public void fireRowsInserted( int rowStart, int rowEnd );

        /**
         * @param rowStart
         * @param rowEnd
         */
        public void fireRowsUpdated( int rowStart, int rowEnd );

        /**
         * 
         */
        public void fireStructureChanged();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface ITabularModel
    {
        /**
         * @param notifier
         */
        public void registerNotifier( ITabularNotifier notifier );

        /**
         * @return
         */
        public int getRowCount();

        /**
         * @param row
         * @return
         */
        public String getRowName( int row );

        /**
         * @return
         */
        public int getColCount();

        /**
         * @param col
         * @return
         */
        public String getColName( int col );

        /**
         * @param col
         * @return
         */
        public Class<?> getColClass( int col );

        /**
         * @param row
         * @param col
         * @return
         */
        public Object getValue( int row, int col );

        /**
         * @param row
         * @param col
         * @param value
         */
        public void setValue( Object value, int row, int col );

        /**
         * @param row
         * @param col
         * @return
         */
        public boolean isCellEditable( int row, int col );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TabularTableModel extends AbstractTableModel
    {
        /**  */
        private static final long serialVersionUID = -1L;

        /**  */
        private ITabularModel model;

        /**
         * 
         */
        public TabularTableModel()
        {
            this.model = new EmptyTabularModel();

            setModel( model );
        }

        /**
         * @param model
         */
        public void setModel( ITabularModel model )
        {
            this.model = model;
        }

        /**
         * 
         */
        public void fireUpdates()
        {
            super.fireTableStructureChanged();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getRowCount()
        {
            return model.getRowCount();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getColumnCount()
        {
            return model.getColCount();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getColumnName( int col )
        {
            return model.getColName( col );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getValueAt( int row, int col )
        {
            return model.getValue( row, col );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final void setValueAt( Object value, int row, int col )
        {
            model.setValue( value, row, col );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isCellEditable( int row, int col )
        {
            return model.isCellEditable( row, col );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class AbstractTableNotifier implements ITabularNotifier
    {
        /**  */
        private final AbstractTableModel tableModel;
        /**  */
        private final IRowListModel listModel;

        /**
         * @param tableModel
         * @param listModel
         */
        public AbstractTableNotifier( AbstractTableModel tableModel,
            IRowListModel listModel )
        {
            this.tableModel = tableModel;
            this.listModel = listModel;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void fireCellUpdated( int row, int col )
        {
            tableModel.fireTableCellUpdated( row, col );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void fireDataChanged()
        {
            int maxRow = tableModel.getRowCount() - 1;

            tableModel.fireTableDataChanged();
            if( maxRow > -1 )
            {
                listModel.fireRowsInserted( 0, maxRow );
            }
            else
            {
                listModel.fireRowsDeleted( 0, Integer.MAX_VALUE );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void fireRowsDeleted( int rowStart, int rowEnd )
        {
            tableModel.fireTableRowsDeleted( rowStart, rowEnd );
            listModel.fireRowsDeleted( rowStart, rowEnd );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void fireRowsInserted( int rowStart, int rowEnd )
        {
            tableModel.fireTableRowsInserted( rowStart, rowEnd );
            listModel.fireRowsInserted( rowStart, rowEnd );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void fireRowsUpdated( int rowStart, int rowEnd )
        {
            tableModel.fireTableRowsUpdated( rowStart, rowEnd );
            listModel.fireRowsUpdated( rowStart, rowEnd );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void fireStructureChanged()
        {
            tableModel.fireTableStructureChanged();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TabularRowHeaderModel implements IRowHeaderModel
    {
        /**  */
        private final RunnableList updaters;

        /**  */
        private ITabularModel model;

        /**
         * 
         */
        public TabularRowHeaderModel()
        {
            this.updaters = new RunnableList();
            this.model = new EmptyTabularModel();

            setModel( model );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getHeader( int row )
        {
            return model.getRowName( row );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getCount()
        {
            return model.getRowCount();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void addUpdatedListener( Runnable callback )
        {
            updaters.addListener( callback );
        }

        /**
         * @param model
         */
        public void setModel( ITabularModel model )
        {
            this.model = model;
        }

        /**
         * 
         */
        public void fireUpdates()
        {
            updaters.fireListeners();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class EmptyTabularModel implements ITabularModel
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void registerNotifier( ITabularNotifier notifier )
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getRowCount()
        {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getRowName( int row )
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getColCount()
        {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getColName( int col )
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getValue( int row, int col )
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setValue( Object value, int row, int col )
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isCellEditable( int row, int col )
        {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<?> getColClass( int col )
        {
            return String.class;
        }
    }
}
