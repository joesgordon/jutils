package org.jutils.core.ui.model;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/*******************************************************************************
 * A {@link TableCellRenderer} that uses the provided decorator to render cells
 * as a {@link JLabel}.
 ******************************************************************************/
public class LabelTableCellRenderer extends DefaultTableCellRenderer
{
    /**  */
    private static final long serialVersionUID = -2822514015162778807L;
    /** The decorator to be used to render cells. */
    private final ITableCellLabelDecorator decorator;

    /***************************************************************************
     * @param decorator the decorator used to render cells.
     **************************************************************************/
    public LabelTableCellRenderer( ITableCellLabelDecorator decorator )
    {
        this.decorator = decorator;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col )
    {
        super.getTableCellRendererComponent( table, value, isSelected, hasFocus,
            row, col );

        decorator.decorate( this, table, value, isSelected, hasFocus, row,
            col );

        return this;
    }

    /***************************************************************************
     * A decorator used to render a cell of a {@link JTable} as a label.
     **************************************************************************/
    public static interface ITableCellLabelDecorator
    {
        /***********************************************************************
         * Decorates the provided label with the value of the table.
         * @param label the label to be decorated.
         * @param table the table in which the label is rendered.
         * @param value the value in the table that used to decorate the label.
         * @param index the index to be rendered.
         * @param isSelected {@code true} if the cell is selected.
         * @param cellHasFocus {@code true} if the cell has focus.
         * @param row the row of the cell to be rendered.
         * @param col the column of the cell to be rendered.
         **********************************************************************/
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col );
    }
}
