package org.jutils.plot.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.jutils.core.SwingUtils;
import org.jutils.core.data.UIProperty;
import org.jutils.core.ui.model.IDataView;
import org.jutils.core.ui.model.LabelTableCellRenderer;
import org.jutils.core.ui.model.LabelTableCellRenderer.ITableCellLabelDecorator;
import org.jutils.plot.model.ISeriesData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SeriesDataView implements IDataView<ISeriesData<?>>
{
    /**  */
    private final JPanel view;
    /**  */
    private final SeriesTableModel tableModel;
    /**  */
    private final JTable table;
    /**  */
    private final DataCellRenderer cellRenderer;

    /**  */
    private ISeriesData<?> series;

    /***************************************************************************
     * 
     **************************************************************************/
    public SeriesDataView()
    {
        this.tableModel = new SeriesTableModel();
        this.table = new JTable( tableModel );
        this.cellRenderer = new DataCellRenderer();

        this.view = createView();

        this.series = null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JScrollPane scrollPane = new JScrollPane( table );

        scrollPane.setPreferredSize( new Dimension( 300, 200 ) );
        scrollPane.setMinimumSize( new Dimension( 300, 200 ) );

        table.setDefaultRenderer( Double.class,
            new LabelTableCellRenderer( this.cellRenderer ) );

        panel.add( scrollPane, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ISeriesData<?> getData()
    {
        return series;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( ISeriesData<?> series )
    {
        this.series = series;

        cellRenderer.setSeries( series );
        tableModel.setSeries( series );
    }

    /***************************************************************************
     * @param pointIdx
     **************************************************************************/
    public void setSelected( int pointIdx )
    {
        table.getSelectionModel().setSelectionInterval( pointIdx, pointIdx );
        SwingUtils.scrollToVisible( table, pointIdx, 0 );
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
    private static class SeriesTableModel extends AbstractTableModel
    {
        /**  */
        private static final long serialVersionUID = 1L;

        /**  */
        private ISeriesData<?> series;

        /**
         * 
         */
        public SeriesTableModel()
        {
            series = null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getRowCount()
        {
            return series.getCount();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getColumnCount()
        {
            return 3;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<?> getColumnClass( int columnIndex )
        {
            return Double.class;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getColumnName( int col )
        {
            switch( col )
            {
                case 0:
                    return "Index";

                case 1:
                    return "X";

                case 2:
                    return "Y";

                default:
                    throw new IllegalArgumentException(
                        "Column is out of bounds: " + col );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getValueAt( int row, int col )
        {
            switch( col )
            {
                case 0:
                    return row;

                case 1:
                    return series.getX( row );

                case 2:
                    return series.getY( row );

                default:
                    throw new IllegalArgumentException(
                        "Column is out of bounds: " + col );
            }
        }

        /**
         * @param series
         */
        public void setSeries( ISeriesData<?> series )
        {
            this.series = series;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DataCellRenderer implements ITableCellLabelDecorator
    {
        /**  */
        private final Color defaultBackground;

        /**  */
        private ISeriesData<?> series;

        /**
         * 
         */
        public DataCellRenderer()
        {
            this.defaultBackground = UIProperty.LABEL_BACKGROUND.getColor();
        }

        /**
         * @param series
         */
        public void setSeries( ISeriesData<?> series )
        {
            this.series = series;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col )
        {
            if( !isSelected )
            {
                Color bg = defaultBackground;
                if( series.isHidden( row ) )
                {
                    bg = Color.LIGHT_GRAY;
                    label.setBackground( bg );
                }
                label.setBackground( bg );
            }
        }
    }
}
