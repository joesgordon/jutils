package jutils.core.ui.sheet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;

import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import jutils.core.SwingUtils;
import jutils.core.laf.UIProperty;
import jutils.core.ui.RowHeaderRenderer;
import jutils.core.ui.event.MouseClickListener.MouseButton;
import jutils.core.ui.event.ResizingTableModelListener;
import jutils.core.ui.event.TableMouseListener;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SpreadSheetView implements IDataView<ISpreadSheet>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JScrollPane scrollpane;
    /**  */
    private final JTable table;
    /**  */
    private final SheetModel model;
    /**  */
    private final RowListModel rowHeaderModel;
    /**  */
    private final JList<String> rowHeader;
    /**  */
    private final int rowHeaderFontWidth;
    /**  */
    private final JLabel cornerLabel;

    /**  */
    private JPopupMenu popup;

    /***************************************************************************
     * 
     **************************************************************************/
    public SpreadSheetView()
    {
        this.rowHeaderModel = new RowListModel( this );
        this.rowHeader = new JList<String>( rowHeaderModel );
        this.model = new SheetModel();
        this.table = new JTable( model );
        this.scrollpane = new JScrollPane( table );
        this.cornerLabel = new JLabel();
        this.view = new JPanel( new BorderLayout() );

        this.popup = null;

        RowHeaderRenderer rhr = new RowHeaderRenderer( table );

        rowHeader.setSelectionMode(
            ListSelectionModel.SINGLE_INTERVAL_SELECTION );
        rowHeader.setCellRenderer( rhr );
        rowHeader.setFixedCellHeight( table.getRowHeight() );
        rowHeader.setMinimumSize( new Dimension( 50, 5 ) );
        rowHeader.setBackground( UIProperty.PANEL_BACKGROUND.getColor() );
        rowHeader.setFixedCellWidth( 50 );

        TableMouseListener tml = new TableMouseListener();

        tml.addHandler( ( b, r, c, p ) -> handleMouseRelease( b, p ) );

        table.addMouseListener( tml );
        table.setColumnSelectionAllowed( false );
        table.setRowSelectionAllowed( false );
        table.setCellSelectionEnabled( true );
        table.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        // scrollpane.getViewport().setBackground(table.getBackground());
        scrollpane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );
        scrollpane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        scrollpane.setRowHeaderView( rowHeader );
        scrollpane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        scrollpane.setCorner( ScrollPaneConstants.UPPER_LEFT_CORNER,
            cornerLabel );

        view.add( scrollpane, BorderLayout.CENTER );

        FontMetrics fm = rowHeader.getFontMetrics( rhr.getFont() );
        this.rowHeaderFontWidth = fm.charWidth( 'W' );
    }

    /***************************************************************************
     * @param button
     * @param p
     **************************************************************************/
    private void handleMouseRelease( MouseButton button, Point p )
    {
        if( button == MouseButton.SECONDARY )
        {
            popup.show( table, p.x, p.y );
        }
    }

    /***************************************************************************
     * @param col
     * @param width
     **************************************************************************/
    public void setColWidth( int col, int width )
    {
        TableColumn column = table.getColumnModel().getColumn( col );

        column.setPreferredWidth( width );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void autoLayout()
    {
        ResizingTableModelListener.resizeTable( table );
    }

    /***************************************************************************
     * @param popup
     **************************************************************************/
    public void setPopup( JPopupMenu popup )
    {
        this.popup = popup;
    }

    /***************************************************************************
     * @param renderer
     **************************************************************************/
    public void setRenderer( TableCellRenderer renderer )
    {
        table.setDefaultRenderer( Object.class, renderer );
    }

    /***************************************************************************
     * @param handler
     **************************************************************************/
    public void setTransferHandler( TransferHandler handler )
    {
        table.setTransferHandler( handler );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSelectedIndex()
    {
        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();
        int index = -1;

        if( row > -1 && col > -1 )
        {
            index = table.getColumnCount() * row + col;
        }

        return index;
    }

    /***************************************************************************
     * @param index0
     * @param index1
     **************************************************************************/
    public void setSelected( int index0, int index1 )
    {
        int row0 = index0 / table.getColumnCount();
        int col0 = index0 % table.getColumnCount();

        int row1 = index1 / table.getColumnCount();
        int col1 = index1 % table.getColumnCount();

        table.setRowSelectionInterval( row0, row1 );
        table.setColumnSelectionInterval( col0, col1 );

        SwingUtils.scrollToVisible( table, row0, col0 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTable getTable()
    {
        return table;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ISpreadSheet getData()
    {
        return model.getData();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( ISpreadSheet data )
    {
        model.setData( data );
        rowHeaderModel.setSize( data.getRowCount() );
        refreshRowHeader();
        cornerLabel.setText( data.getCornerName() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void refreshRowHeader()
    {
        int len = rowHeaderModel.getStringSize();

        len = rowHeaderFontWidth * ( len + 2 );

        rowHeader.setFixedCellWidth( len );
        rowHeader.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class RowListModel extends AbstractListModel<String>
    {
        /**  */
        private static final long serialVersionUID = -7144290320125729828L;
        /**  */
        private final SpreadSheetView view;
        /**  */
        private int rowCount;
        /**  */
        private int stringSize;

        public RowListModel( SpreadSheetView view )
        {
            this.view = view;

            this.rowCount = 1;
            this.stringSize = 1;
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
            int w = view.model.getData().getRowHeader( count ).length();

            stringSize = w;

            super.fireContentsChanged( this, 0, count );
        }

        @Override
        public String getElementAt( int index )
        {
            return view.model.getData().getRowHeader( index );
        }
    }
}
