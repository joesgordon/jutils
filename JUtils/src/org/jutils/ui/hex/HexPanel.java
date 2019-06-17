package org.jutils.ui.hex;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;

import org.jutils.Utils;
import org.jutils.data.UIProperty;
import org.jutils.ui.RowHeaderRenderer;
import org.jutils.ui.hex.HexTable.IRangeSelectedListener;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexPanel implements IView<JComponent>
{
    /**  */
    private final JPanel panel;
    /**  */
    private final HexTable table;
    /**  */
    private final HexRowListModel rowListModel;
    /**  */
    private final JList<String> rowHeader;
    /**  */
    private final int rowHeaderFontWidth;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexPanel()
    {
        this.panel = new JPanel( new GridBagLayout() );
        this.rowListModel = new HexRowListModel();
        this.table = new HexTable();
        this.rowHeader = new JList<String>( rowListModel );

        JScrollPane scrollPane = new JScrollPane( table );

        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        rowHeader.setBackground( UIProperty.PANEL_BACKGROUND.getColor() );
        rowHeader.setFixedCellWidth( 50 );

        // TODO select row when row header is selected

        rowHeader.setFocusable( true );
        rowHeader.setSelectionMode(
            ListSelectionModel.SINGLE_INTERVAL_SELECTION );
        rowHeader.setFixedCellHeight( table.getRowHeight() );

        // ---------------------------------------------------------------------
        // The calculation for the row header's fixed cell height was the row
        // height + the row margin. This made the rows not line up though. I
        // think it would have worked if the cells in the row header did not
        // have a border.
        // ---------------------------------------------------------------------

        rowHeader.setCellRenderer( new RowHeaderRenderer( table ) );
        rowHeader.setMinimumSize( new Dimension( 50, 5 ) );

        scrollPane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        scrollPane.setRowHeaderView( rowHeader );

        panel.add( scrollPane,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        Dimension dim = panel.getMinimumSize();
        dim.width = 600;
        panel.setMinimumSize( dim );

        FontMetrics fm = rowHeader.getFontMetrics( rowHeader.getFont() );
        rowHeaderFontWidth = fm.charWidth( '0' );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addRangeSelectedListener( IRangeSelectedListener l )
    {
        table.addRangeSelectedListener( l );
    }

    /***************************************************************************
     * @param c
     **************************************************************************/
    public void setHightlightColor( Color c )
    {
        table.setHightlightColor( c );
    }

    /***************************************************************************
     * @param length
     **************************************************************************/
    public void setHighlightLength( int length )
    {
        table.setHighlightLength( length );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return panel;
    }

    /***************************************************************************
     * @param address
     **************************************************************************/
    public void setStartingAddress( long address )
    {
        rowListModel.setStartingAddress( address );

        refreshRowHeader();
    }

    /***************************************************************************
     * @param bytes
     **************************************************************************/
    public void setBuffer( IByteBuffer bytes )
    {
        table.setBuffer( bytes );
        rowListModel.setSize( table.getRowCount() );

        refreshRowHeader();
    }

    /***************************************************************************
     * @param start
     * @param end
     **************************************************************************/
    public void setSelected( int start, int end )
    {
        int row0 = start / 16;
        int col0 = start % 16;
        int row1 = end / 16;
        int col1 = end % 16;

        TableCellEditor editor = table.getCellEditor();

        if( editor != null )
        {
            editor.stopCellEditing();
        }
        table.changeSelection( row0, col0, false, false );
        table.changeSelection( row1, col1, false, true );

        Utils.scrollToVisible( table, row1, col1 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void refreshRowHeader()
    {
        int len = rowListModel.getStringSize();

        len = rowHeaderFontWidth * ( len + 2 );

        rowHeader.setFixedCellWidth( len );
        rowHeader.repaint();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IByteBuffer getBuffer()
    {
        return table.getBuffer();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSelectedByte()
    {
        return table.selection.start;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class HexRowListModel extends AbstractListModel<String>
    {
        /**  */
        private static final long serialVersionUID = -5271777526267037957L;
        /**  */
        private long startingAddress;
        /**  */
        private int rowCount;
        /**  */
        private String formatString;
        /**  */
        private int stringSize;

        public HexRowListModel()
        {
            startingAddress = 0;
            rowCount = 1;
            stringSize = 1;
            formatString = "%X0";
        }

        @Override
        public int getSize()
        {
            return rowCount;
        }

        public int getStringSize()
        {
            return stringSize;
        }

        public void setSize( int count )
        {
            rowCount = count;
            int w = Long.toHexString(
                startingAddress + count * 16L - 1 ).length();

            if( w > 0 )
            {
                formatString = "%0" + w + "X";
            }
            else
            {
                formatString = "%X";
            }

            stringSize = w + 1;
        }

        public void setStartingAddress( long address )
        {
            startingAddress = address;
        }

        @Override
        public String getElementAt( int index )
        {
            return String.format( formatString, startingAddress + index * 16L );
        }
    }
}
