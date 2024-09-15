package jutils.core.ui.hex;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import jutils.core.laf.UIProperty;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ByteCellRenderer implements TableCellRenderer
{
    /**  */
    public static final Color DEFAULT_ALT_ROW_COLOR = new Color( 210, 225,
        240 );

    /**  */
    private final DefaultTableCellRenderer renderer;
    /**  */
    private final Color nullColor;

    /**  */
    private Color altRowColor;
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

        this.altRowColor = UIProperty.CONTROL.getColor();
        this.highlightColor = Color.yellow;
        this.offset = -1;
        this.len = -1;

        renderer.setHorizontalAlignment( SwingConstants.CENTER );
    }

    /***************************************************************************
     * {@inheritDoc}
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
                    renderer.setBackground( altRowColor );
                }
                else
                {
                    renderer.setBackground( null );
                }
            }
        }

        return renderer;
    }

    /**
     * @param c
     */
    public void setHightlightColor( Color c )
    {
        this.highlightColor = c;
    }

    /**
     * @param length
     */
    public void setHighlightLength( int length )
    {
        this.len = length;
    }

    /**
     * @param offset
     */
    public void setHighlightOffset( int offset )
    {
        this.offset = offset;
    }
}
