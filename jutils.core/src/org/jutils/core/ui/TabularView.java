package org.jutils.core.ui;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.jutils.core.ui.RowHeaderView.IRowHeaderModel;
import org.jutils.core.ui.event.ResizingTableModelListener;
import org.jutils.core.ui.event.RunnableList;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TabularView implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final JTable table;
    /**  */
    private final TabularTableModel tableModel;
    /**  */
    private final TabularRowHeaderModel rowModel;

    /***************************************************************************
     * 
     **************************************************************************/
    public TabularView()
    {
        this.tableModel = new TabularTableModel();
        this.rowModel = new TabularRowHeaderModel();

        this.table = new JTable( tableModel );

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        // TODO expose these properties individually

        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION );
        table.setCellSelectionEnabled( true );

        JScrollPane pane = new JScrollPane( table );

        RowHeaderView rowHeader = new RowHeaderView( table );

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

        tableModel.fireUpdates();
        rowModel.fireUpdates();
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
    public static interface ITabularModel
    {
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
    public static final class EmptyTabularModel implements ITabularModel
    {
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
