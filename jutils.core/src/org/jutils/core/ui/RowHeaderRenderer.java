package org.jutils.core.ui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RowHeaderRenderer implements ListCellRenderer<Object>
{
    /**  */
    private final JTable table;
    /**  */
    private final TableCellRenderer tcr;
    /**  */
    private final Font f;

    /**
     * @param table
     */
    public RowHeaderRenderer( JTable table )
    {
        this.table = table;
        JTableHeader header = table.getTableHeader();
        this.tcr = header.getDefaultRenderer();
        this.f = new Font( "Monospaced", Font.PLAIN, 12 );
    }

    /**
     * @return
     */
    public Font getFont()
    {
        return f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getListCellRendererComponent( JList<? extends Object> list,
        Object value, int index, boolean isSelected, boolean cellHasFocus )
    {
        Component c = tcr.getTableCellRendererComponent( table, value,
            isSelected, cellHasFocus, 0, 0 );

        c.setFont( f );

        return c;
    }
}
