package org.jutils.ui.hex;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.*;
import javax.swing.text.*;

import org.jutils.data.UIProperty;
import org.jutils.ui.HighlightedLabel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexTable extends JTable
{
    /**  */
    private static final long serialVersionUID = 1569560679743570898L;

    /**  */
    private final HexTableModel model;
    /**  */
    private final ByteCellRenderer renderer;
    /**  */
    private final ByteCellEditor editor;
    /**  */
    private final List<IRangeSelectedListener> rangeListeners;

    /**  */
    public final SelectionRange selection;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexTable()
    {
        super();

        this.editor = new ByteCellEditor();
        this.renderer = new ByteCellRenderer();
        this.model = new HexTableModel();
        this.rangeListeners = new ArrayList<IRangeSelectedListener>();
        this.selection = new SelectionRange();

        setFont( new Font( "Monospaced", Font.PLAIN, 12 ) );
        setCellSelectionEnabled( true );
        setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION );
        getTableHeader().setReorderingAllowed( false );
        setShowGrid( false );
        setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );

        ColHeaderRenderer colRenderer = new ColHeaderRenderer(
            getTableHeader().getDefaultRenderer() );

        getTableHeader().setDefaultRenderer( colRenderer );

        setModel( model );

        FontMetrics fm = getFontMetrics( getFont() );
        Font headerFont = UIProperty.TABLEHEADER_FONT.getFont();
        FontMetrics headerFM = getFontMetrics( headerFont );

        int w = Math.max( fm.stringWidth( "4444" ),
            headerFM.stringWidth( "4444" ) );

        for( int i = 0; i < getColumnCount(); i++ )
        {
            TableColumn column = getColumnModel().getColumn( i );
            column.setCellEditor( editor );

            if( i == getColumnCount() - 1 )
            {
                column.setPreferredWidth( w * 5 );
                column.setCellRenderer( new AsciiCellRenderer( this ) );
            }
            else
            {
                column.setPreferredWidth( w );
                column.setCellRenderer( renderer );
            }
            column.setResizable( false );
        }

        setPreferredScrollableViewportSize(
            new Dimension( w * 21, 25 * getRowHeight() ) );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addRangeSelectedListener( IRangeSelectedListener l )
    {
        rangeListeners.add( l );
    }

    /***************************************************************************
     * @param buf
     **************************************************************************/
    public void setBuffer( IByteBuffer buf )
    {
        model.setBuffer( buf );
    }

    /***************************************************************************
     * @param c
     **************************************************************************/
    public void setHightlightColor( Color c )
    {
        renderer.setHightlightColor( c );
    }

    /***************************************************************************
     * @param length
     **************************************************************************/
    public void setHighlightLength( int length )
    {
        renderer.setHighlightLength( length );

        int row1 = selection.end / 16;
        int row2 = ( selection.end + length ) / 16;

        model.fireTableRowsUpdated( row1, row2 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void changeSelection( int row, int col, boolean toggle,
        boolean extend )
    {
        int selRow = row;
        int selCol = col;

        if( selCol > 15 )
        {
            selCol = 0;
            selRow++;
        }

        // Check for empty spaces if on the last row.
        if( selRow == getRowCount() - 1 )
        {
            int lastCol = ( model.getBufferSize() + 15 ) % 16;
            selCol = Math.min( lastCol, selCol );
        }
        selCol = Math.min( selCol, getColumnCount() - 2 );

        // LogUtils.printDebug( "At [" + row + ", " + col + "] going to [" +
        // selRow + ", " + selCol + "]" );

        super.changeSelection( selRow, selCol, toggle, extend );

        if( isFocusable() )
        {
            selection.end = calculateDataOffset( selRow, selCol );
            if( !extend )
            {
                selection.start = selection.end;
            }

            if( selection.start < 0 || selection.end >= model.getBufferSize() )
            {
                return;
            }

            repaint();

            renderer.setHighlightOffset( selection.end );
            // renderer.setHighlightOffset( Math.min( selection.start,
            // selection.end
            // )
            // );

            fireSelectionChanged( selection.start, selection.end );
        }
    }

    /***************************************************************************
     * @param start
     * @param end
     **************************************************************************/
    private void fireSelectionChanged( int start, int end )
    {
        for( IRangeSelectedListener l : rangeListeners )
        {
            l.rangeSelected( start, end );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void clearSelection()
    {
        if( selection != null )
        {
            selection.clear();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isCellSelected( int row, int col )
    {
        if( col == 16 )
        {
            return false;
        }

        int offset = row * 16 + col;
        int min = Math.min( selection.start, selection.end );
        int max = Math.max( selection.start, selection.end );

        return offset >= min && offset <= max;
    }

    /***************************************************************************
     * Calculate the offset into the data model's byte data for the specified
     * row and column.
     * @param row
     * @param col
     * @return
     **************************************************************************/
    private int calculateDataOffset( int row, int col )
    {
        if( row < 0 || row > getRowCount() || col < 0 || col > 16 )
        {
            return -1;
        }

        int offset = row * 16 + col;

        return offset < 0 ? -1 : offset;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IByteBuffer getBuffer()
    {
        return model.getBuffer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AutoSelector implements FocusListener
    {
        @Override
        public void focusGained( FocusEvent e )
        {
            ( ( JTextField )e.getSource() ).selectAll();
        }

        @Override
        public void focusLost( FocusEvent e )
        {
            ;
        }
    }

    /***************************************************************************
     * Filter that prevents the user from entering in text that does not parse
     * to a byte.
     **************************************************************************/
    private static class EditorDocumentFilter extends DocumentFilter
    {
        private Component comp;

        public EditorDocumentFilter( Component c )
        {
            comp = c;
        }

        private static boolean isByte( String str )
        {
            try
            {
                int i = Integer.parseInt( str, 16 );
                if( i < 0 || i > 0xff )
                {
                    throw new NumberFormatException();
                }
            }
            catch( NumberFormatException nfe )
            {
                return false;
            }
            return true;
        }

        @Override
        public void insertString( FilterBypass fb, int offs, String text,
            AttributeSet attr ) throws BadLocationException
        {
            Document doc = fb.getDocument();
            String str = doc.getText( 0, offs ) + text +
                doc.getText( offs, doc.getLength() - offs );

            if( isByte( str ) )
            {
                fb.insertString( offs, str, attr );
            }
            else
            {
                UIManager.getLookAndFeel().provideErrorFeedback( comp );
            }
        }

        @Override
        public void replace( FilterBypass fb, int offs, int len, String text,
            AttributeSet attrs ) throws BadLocationException
        {
            Document doc = fb.getDocument();
            int endIndex = offs + len;
            String str = doc.getText( 0, offs ) + text +
                doc.getText( endIndex, doc.getLength() - endIndex );

            if( isByte( str ) )
            {
                fb.replace( offs, len, text, attrs );
            }
            else
            {
                UIManager.getLookAndFeel().provideErrorFeedback( comp );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ByteCellEditor implements TableCellEditor
    {
        private final DefaultCellEditor editor;
        private final JTextField field;

        public ByteCellEditor()
        {
            this.field = new JTextField();
            this.editor = new DefaultCellEditor( field );

            field.setFont( new Font( "Monospaced", Font.PLAIN, 12 ) );
            field.setHorizontalAlignment( SwingConstants.CENTER );
            field.setMargin( new Insets( 0, 0, 0, 0 ) );
            field.addFocusListener( new AutoSelector() );

            AbstractDocument doc = ( AbstractDocument )field.getDocument();
            doc.setDocumentFilter( new EditorDocumentFilter( field ) );
        }

        @Override
        public boolean stopCellEditing()
        {
            // Prevent the user from entering empty string as a value.
            String value = getCellEditorValue().toString();

            if( value.length() == 0 )
            {
                UIManager.getLookAndFeel().provideErrorFeedback( null );
                return false;
            }

            return editor.stopCellEditing();
        }

        @Override
        public Object getCellEditorValue()
        {
            return editor.getCellEditorValue();
        }

        @Override
        public boolean isCellEditable( EventObject anEvent )
        {
            return editor.isCellEditable( anEvent );
        }

        @Override
        public boolean shouldSelectCell( EventObject anEvent )
        {
            return editor.shouldSelectCell( anEvent );
        }

        @Override
        public void cancelCellEditing()
        {
            editor.cancelCellEditing();
        }

        @Override
        public void addCellEditorListener( CellEditorListener l )
        {
            editor.addCellEditorListener( l );
        }

        @Override
        public void removeCellEditorListener( CellEditorListener l )
        {
            editor.removeCellEditorListener( l );
        }

        @Override
        public Component getTableCellEditorComponent( JTable table,
            Object value, boolean isSelected, int row, int column )
        {
            return editor.getTableCellEditorComponent( table, value, isSelected,
                row, column );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AsciiCellRenderer implements TableCellRenderer
    {
        private HighlightedLabel label;

        private HexTable ht;

        public AsciiCellRenderer( HexTable table )
        {
            ht = table;
            label = new HighlightedLabel();

            label.setHorizontalAlignment( SwingConstants.CENTER );
            label.setHighlightColor( ByteCellRenderer.ALTERNATING_ROW_COLOR );
            label.setFont( loadFont() );
        }

        private static Font loadFont()
        {
            Font f;

            f = new Font( "Monospaced", Font.PLAIN, 12 );

            return f;
        }

        @Override
        public Component getTableCellRendererComponent( JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int col )
        {
            int selectStart = ht.selection.start;
            int selectEnd = ht.selection.end;

            label.setText( ( String )value );
            // label.setHorizontalAlignment( SwingConstants.LEFT );

            if( selectStart >= 0 )
            {
                int min = Math.min( selectStart, selectEnd );
                int max = Math.max( selectStart, selectEnd );

                if( min / 16 < row && max / 16 > row )
                {
                    label.setHighlight( 0, 16 );
                }
                else if( min / 16 == row )
                {
                    int off = min - ( row * 16 );
                    label.setHighlight( off, max - min + 1 );
                }
                else if( max / 16 == row )
                {
                    label.setHighlight( 0, max - ( row * 16 ) + 1 );
                }
                else
                {
                    label.clearHighlight();
                }
            }
            else
            {
                label.clearHighlight();
            }

            return label;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IRangeSelectedListener
    {
        public void rangeSelected( int start, int end );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class SelectionRange
    {
        public int start;
        public int end;

        public SelectionRange()
        {
            clear();
        }

        public void clear()
        {
            start = -1;
            end = -1;
        }

        public void set( int start, int end )
        {
            this.start = start;
            this.end = end;
        }
    }
}
