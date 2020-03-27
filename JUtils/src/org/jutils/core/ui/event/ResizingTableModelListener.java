package org.jutils.core.ui.event;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ResizingTableModelListener implements TableModelListener
{
    /**  */
    private final JTable table;

    /***************************************************************************
     * @param table
     **************************************************************************/
    public ResizingTableModelListener( JTable table )
    {
        this.table = table;
    }

    /***************************************************************************
     * @param table
     **************************************************************************/
    public static void addToTable( JTable table )
    {
        TableModel model = table.getModel();
        model.addTableModelListener( new ResizingTableModelListener( table ) );
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public void tableChanged( TableModelEvent e )
    {
        SwingUtilities.invokeLater( () -> resizeTable( table ) );
    }

    /***************************************************************************
     * @param table
     **************************************************************************/
    public static void resizeTable( JTable table )
    {
        int horzSpace = 6;
        String colName;
        TableModel model = table.getModel();
        int colCount = model.getColumnCount();
        int rowCount = model.getRowCount();
        int widths[] = new int[model.getColumnCount()];
        Component cellRenderer;
        TableCellRenderer tableCellRenderer;
        int defaultWidth;

        int rrow = table.getRowCount();

        rrow = rrow == 0 ? -1 : 0;

        // ---------------------------------------------------------------------
        // Compute all widths.
        // ---------------------------------------------------------------------
        for( int col = 0; col < colCount; col++ )
        {
            colName = model.getColumnName( col );
            defaultWidth = 20;

            // -----------------------------------------------------------------
            // Compute header width.
            // -----------------------------------------------------------------
            tableCellRenderer = table.getColumnModel().getColumn(
                col ).getHeaderRenderer();
            if( tableCellRenderer == null )
            {
                tableCellRenderer = table.getTableHeader().getDefaultRenderer();
            }
            cellRenderer = tableCellRenderer.getTableCellRendererComponent(
                table, colName, false, false, -1, col );

            widths[col] = ( int )cellRenderer.getPreferredSize().getWidth() +
                horzSpace;
            widths[col] = Math.max( widths[col], defaultWidth );

            tableCellRenderer = table.getCellRenderer( rrow, col );

            // -----------------------------------------------------------------
            // check if cell values fit in their cells
            // -----------------------------------------------------------------
            for( int row = 0; row < rowCount; row++ )
            {
                Object obj = model.getValueAt( row, col );
                int width = 0;
                if( obj != null )
                {
                    tableCellRenderer = table.getCellRenderer( row, col );
                    cellRenderer = tableCellRenderer.getTableCellRendererComponent(
                        table, obj, false, false, row, col );
                    width = ( int )cellRenderer.getPreferredSize().getWidth() +
                        horzSpace;
                }
                widths[col] = Math.max( widths[col], width );
            }
        }

        TableColumnModel colModel = table.getColumnModel();

        // ---------------------------------------------------------------------
        // Set the column widths.
        // ---------------------------------------------------------------------
        for( int i = 0; i < colCount; i++ )
        {
            colModel.getColumn( i ).setPreferredWidth( widths[i] );
            // colModel.getColumn( i ).setMinWidth( widths[i] );
        }
    }
}
