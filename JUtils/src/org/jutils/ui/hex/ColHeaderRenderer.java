package org.jutils.ui.hex;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/***************************************************************************
 * 
 **************************************************************************/
public class ColHeaderRenderer implements TableCellRenderer
{
    /**  */
    private final TableCellRenderer renderer;
    /**  */
    private final Font font;

    /***************************************************************************
     * 
     **************************************************************************/
    public ColHeaderRenderer( TableCellRenderer defaultRenderer )
    {
        renderer = defaultRenderer;
        font = new Font( "Monospaced", Font.PLAIN, 12 );

        DefaultTableCellRenderer r = ( DefaultTableCellRenderer )renderer;
        r.setHorizontalAlignment( SwingConstants.CENTER );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col )
    {
        Component c = renderer.getTableCellRendererComponent( table, value,
            isSelected, hasFocus, row, col );

        c.setFont( font );

        return c;
    }
}
