package org.jutils.ui.hex;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.jutils.data.UIProperty;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ByteCellRenderer implements TableCellRenderer
{
    /**  */
    public static final Color ALTERNATING_ROW_COLOR = new Color( 210, 225,
        240 );

    /**  */
    private final DefaultTableCellRenderer renderer;
    /**  */
    private final Color nullColor;

    /**  */
    private Color highlightColor;
    /**  */
    private int offset;
    /**  */
    private int len;

    /***************************************************************************
     * 
     **************************************************************************/
    public ByteCellRenderer()
    {
        super();

        this.renderer = new DefaultTableCellRenderer();
        this.nullColor = UIProperty.PANEL_BACKGROUND.getColor();

        this.highlightColor = Color.yellow;
        this.offset = -1;
        this.len = -1;

        renderer.setHorizontalAlignment( SwingConstants.CENTER );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col )
    {
        renderer.getTableCellRendererComponent( table, value, isSelected,
            hasFocus, row, col );

        int off = row * 16 + col;
        boolean isHighlighted = len > -1 && off >= offset && off < offset + len;
        boolean isAltRow = row % 2 != 0;

        if( value == null )
        {
            renderer.setBackground( nullColor );
            renderer.setBorder( null );
        }
        else
        {
            if( isHighlighted )
            {
                renderer.setBackground( highlightColor );
                renderer.setForeground( Color.black );
            }
            else if( !isSelected )
            {
                if( isAltRow )
                {
                    renderer.setBackground( ALTERNATING_ROW_COLOR );
                }
                else
                {
                    renderer.setBackground( null );
                }
            }
        }

        return renderer;
    }

    public void setHightlightColor( Color c )
    {
        this.highlightColor = c;
    }

    public void setHighlightLength( int length )
    {
        this.len = length;
    }

    public void setHighlightOffset( int offset )
    {
        this.offset = offset;
    }
}
