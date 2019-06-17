package org.jutils.ui.hex;

import java.nio.charset.Charset;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/*******************************************************************************
 * Implements a {@link TableModel} to display the hexadecimal values and their
 * corresponding ASCII representations.
 ******************************************************************************/
public class HexTableModel extends AbstractTableModel
{
    /**  */
    private static final long serialVersionUID = 3798740609336860057L;

    /** The buffer to be displayed */
    private IByteBuffer buffer;
    /**  */
    private final byte [] asciiBuffer;
    /**  */
    private final Charset utf8;

    /***************************************************************************
     * Creates the table model.
     **************************************************************************/
    public HexTableModel()
    {
        this.asciiBuffer = new byte[16];
        this.utf8 = Charset.forName( "UTF-8" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Class<?> getColumnClass( int col )
    {
        return String.class;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isCellEditable( int row, int col )
    {
        return col < 16;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getColumnName( int col )
    {
        if( col < 16 )
        {
            return HexUtils.toHexString( col );
        }

        return "Ascii";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValueAt( Object val, int row, int col )
    {
        int i = Integer.parseInt( val.toString(), 16 );

        if( i < 0 || i > 255 )
        {
            throw new NumberFormatException(
                "Number outside range of byte: " + Integer.toHexString( i ) );
        }

        buffer.set( getOffset( row, col ), ( byte )i );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object getValueAt( int row, int col )
    {
        String str = null;

        if( buffer == null )
        {
            return null;
        }

        int index = getOffset( row, col );

        if( col < 16 )
        {
            if( index < buffer.size() )
            {
                str = HexUtils.toHexString( buffer.get( index ) );
            }
        }
        else
        {
            int end = index + 16;
            int count;

            end = end > buffer.size() ? buffer.size() : end;
            count = end - index;

            for( int i = 0; i < count; i++ )
            {
                asciiBuffer[i] = buffer.get( i + index );
            }

            HexUtils.cleanAscii( asciiBuffer, 0, count );

            str = new String( asciiBuffer, 0, count, utf8 );
        }

        return str;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getColumnCount()
    {
        return 17;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getRowCount()
    {
        if( buffer == null )
        {
            return 0;
        }

        int len = buffer.size();
        int extra = len % 16 == 0 ? 0 : 1;
        return len > 0 ? len / 16 + extra : 0;
    }

    /***************************************************************************
     * Returns the size of the current buffer.
     **************************************************************************/
    public int getBufferSize()
    {
        return buffer != null ? buffer.size() : 0;
    }

    /***************************************************************************
     * Sets the buffer to be displayed.
     **************************************************************************/
    public void setBuffer( IByteBuffer buf )
    {
        buffer = buf;

        int lastRow = getRowCount();
        if( lastRow > -1 )
        {
            super.fireTableRowsInserted( 0, lastRow );
        }
    }

    /***************************************************************************
     * Returns the current buffer.
     **************************************************************************/
    public IByteBuffer getBuffer()
    {
        return buffer;
    }

    /***************************************************************************
     * Returns the 0-relative byte offset for the given row and column.
     * @param row the row of the byte.
     * @param col the column of the byte.
     * @return the byte offset.
     **************************************************************************/
    private static int getOffset( int row, int col )
    {
        if( col != 16 )
        {
            return row * 16 + col;
        }

        return row * 16;
    }
}
