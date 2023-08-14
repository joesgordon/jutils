package jutils.core.ui;

import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import jutils.core.ui.TabularView.ITabularModel;
import jutils.core.ui.TabularView.ITabularNotifier;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class TabularItemsView<T> implements IView<JComponent>
{
    /**  */
    private final TabularView table;

    /**  */
    private ITabularItemsModel<T> itemsModel;

    /***************************************************************************
     * 
     **************************************************************************/
    public TabularItemsView()
    {
        this.itemsModel = new EmptyTabularItemsModel<>();

        this.table = new TabularView();

        table.setModel( new TabularItemsTableModel<>( itemsModel ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return table.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void resize()
    {
        table.resize();
    }

    /***************************************************************************
     * @param model
     **************************************************************************/
    public void setModel( ITabularItemsModel<T> model )
    {
        table.setModel( new TabularItemsTableModel<>( model ) );
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
        table.setSelectedRows( rowStart, rowEnd );
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
        table.setColumnRenderer( col, renderer );
    }

    /***************************************************************************
     * @param cls
     * @param renderer
     **************************************************************************/
    public void setClassRenderer( Class<?> cls, TableCellRenderer renderer )
    {
        table.setClassRenderer( cls, renderer );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTable getTable()
    {
        return table.getTable();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JScrollPane getPane()
    {
        return table.getPane();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface ITabularItemsModel<T>
    {
        /**
         * @param notifier
         */
        public void registerNotifier( ITabularNotifier notifier );

        /**
         * @return
         */
        public int getCount();

        /**
         * @param index
         * @return
         */
        public T getItem( int index );

        /**
         * Returns the text to be shown in the row header.
         * @param item
         * @param index
         * @return
         */
        public String getItemName( T item, int index );

        /**
         * @return
         */
        public int getPropertyCount();

        /**
         * @param col
         * @return
         */
        public String getPropertyName( int col );

        /**
         * @param col
         * @return
         */
        public Class<?> getPropertyClass( int col );

        /**
         * @param item
         * @param row
         * @param col
         * @return
         */
        public Object getProperty( T item, int row, int col );

        /**
         * @param row
         * @param col
         * @param value
         */
        public void setProperty( Object value, int row, int col );

        /**
         * @param item
         * @param row
         * @param col
         * @return
         */
        public boolean isPropertyEditable( T item, int row, int col );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TabularItemsTableModel<T>
        implements ITabularModel
    {
        /**  */
        private final ITabularItemsModel<T> model;

        /**
         * @param model
         */
        public TabularItemsTableModel( ITabularItemsModel<T> model )
        {
            this.model = model;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void registerNotifier( ITabularNotifier notifier )
        {
            model.registerNotifier( notifier );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getRowCount()
        {
            return model.getCount();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getRowName( int row )
        {
            return model.getItemName( model.getItem( row ), row );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getColCount()
        {
            return model.getPropertyCount();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getColName( int col )
        {
            return model.getPropertyName( col );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<?> getColClass( int col )
        {
            return model.getPropertyClass( col );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getValue( int row, int col )
        {
            T item = model.getItem( row );

            return model.getProperty( item, row, col );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final void setValue( Object value, int row, int col )
        {
            model.setProperty( value, row, col );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isCellEditable( int row, int col )
        {
            return model.isPropertyEditable( model.getItem( row ), row, col );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class EmptyTabularItemsModel<T>
        implements ITabularItemsModel<T>
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
        public int getCount()
        {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public T getItem( int index )
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getItemName( Object item, int index )
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getPropertyCount()
        {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getPropertyName( int col )
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<?> getPropertyClass( int col )
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getProperty( Object item, int row, int col )
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setProperty( Object value, int row, int col )
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isPropertyEditable( Object item, int row, int col )
        {
            return false;
        }
    }
}
