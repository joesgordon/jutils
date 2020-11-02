package org.jutils.core.ui.event;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TableMouseListener extends MouseAdapter
{
    /**  */
    private final List<IReleasedHandler> handlers;

    /***************************************************************************
     * @param view
     **************************************************************************/
    public TableMouseListener()
    {
        handlers = new ArrayList<>();
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public void mousePressed( MouseEvent e )
    {
        if( SwingUtilities.isRightMouseButton( e ) )
        {
            selectCellAt( e );
        }
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public void mouseReleased( MouseEvent e )
    {
        Point p = e.getPoint();
        JTable table = ( JTable )e.getSource();
        RowCol rc = fromPointToRowCol( table, p );

        if( SwingUtilities.isRightMouseButton( e ) )
        {
            selectCellAt( table, rc );
        }

        fireHandlers( e, rc, p );
    }

    /***************************************************************************
     * @param e
     * @param rc
     * @param p
     **************************************************************************/
    private void fireHandlers( MouseEvent e, RowCol rc, Point p )
    {
        MouseButton button = MouseButton.fromEvent( e );

        for( IReleasedHandler handler : handlers )
        {
            handler.released( button, rc.row, rc.col, p );
        }
    }

    /***************************************************************************
     * @param handler
     **************************************************************************/
    public void addHandler( IReleasedHandler handler )
    {
        handlers.add( handler );
    }

    /***************************************************************************
     * @param e
     * @param invokeHandlers
     **************************************************************************/
    private void selectCellAt( MouseEvent e )
    {
        JTable table = ( JTable )e.getSource();
        RowCol rc = fromPointToRowCol( table, e.getPoint() );

        selectCellAt( table, rc );
    }

    /***************************************************************************
     * @param table
     * @param rc
     **************************************************************************/
    private void selectCellAt( JTable table, RowCol rc )
    {
        if( rc.row > -1 && rc.col > -1 )
        {
            table.setRowSelectionInterval( rc.row, rc.row );
            table.setColumnSelectionInterval( rc.col, rc.col );

            TableModel model = table.getModel();

            if( model instanceof AbstractTableModel )
            {
                AbstractTableModel abstractModel = ( AbstractTableModel )model;

                abstractModel.fireTableCellUpdated( rc.row, rc.col );
            }
        }
        else
        {
            table.clearSelection();
        }
    }

    /***************************************************************************
     * @param table
     * @param p
     * @return
     **************************************************************************/
    public static RowCol fromPointToRowCol( JTable table, Point p )
    {
        RowCol rc = new RowCol();

        rc.row = table.rowAtPoint( p );
        rc.col = table.columnAtPoint( p );

        return rc;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RowCol
    {
        /**  */
        public int row;
        /**  */
        public int col;

        /**
         * 
         */
        public RowCol()
        {
            this.row = 0;
            this.col = 0;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IReleasedHandler
    {
        /**
         * @param button
         * @param row
         * @param col
         * @param p
         */
        public void released( MouseButton button, int row, int col, Point p );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum MouseButton
    {
        /**  */
        LEFT,
        /**  */
        RIGHT,
        /**  */
        MIDDLE,
        /**  */
        OTHER;

        /**
         * @param e
         * @return
         */
        public static MouseButton fromEvent( MouseEvent e )
        {
            boolean isRight = SwingUtilities.isRightMouseButton( e );
            boolean isLeft = SwingUtilities.isLeftMouseButton( e );
            boolean isMiddle = SwingUtilities.isMiddleMouseButton( e );

            return isRight ? RIGHT
                : ( isLeft ? LEFT : ( isMiddle ? MIDDLE : OTHER ) );
        }
    }
}
