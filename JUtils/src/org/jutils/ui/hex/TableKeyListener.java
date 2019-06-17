package org.jutils.ui.hex;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTable;

import org.jutils.io.LogUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TableKeyListener extends KeyAdapter
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void keyPressed( KeyEvent e )
    {
        // TODO make this not suck.
        JTable table = ( JTable )e.getSource();
        int code = e.getKeyCode();
        boolean shiftDown = e.isShiftDown();
        int idx = table.getSelectionModel().getLeadSelectionIndex();
        int col = idx % table.getColumnCount();
        int row = idx / table.getRowCount() + 1;

        LogUtils.printDebug( "At [" + row + ", " + col + "] going " + code );

        switch( code )
        {
            case KeyEvent.VK_LEFT:
                // move selection left
                moveLeft( table, row, col, shiftDown );
                e.consume();
                break;
            case KeyEvent.VK_UP:
                // move selection up
                moveUp( table, row, col, shiftDown );
                e.consume();
                break;
            case KeyEvent.VK_RIGHT:
                // move selection right
                moveRight( table, row, col, shiftDown );
                e.consume();
                break;
            case KeyEvent.VK_DOWN:
                // move selection down
                moveDown( table, row, col, shiftDown );
                e.consume();
                break;
        }
    }

    /***************************************************************************
     * @param t
     * @param row
     * @param col
     * @param extend
     **************************************************************************/
    private static void moveUp( JTable t, int row, int col, boolean extend )
    {
        row--;
        if( row < 0 )
        {
            return;
        }

        t.changeSelection( row, col, false, extend );
    }

    /***************************************************************************
     * @param t
     * @param row
     * @param col
     * @param extend
     **************************************************************************/
    private static void moveLeft( JTable t, int row, int col, boolean extend )
    {
        col--;
        if( col - 1 < 0 )
        {
            row--;
            col = t.getColumnCount() - 1;
        }

        if( row > -1 )
        {
            t.changeSelection( row, col, false, extend );
        }
    }

    /***************************************************************************
     * @param t
     * @param row
     * @param col
     * @param extend
     **************************************************************************/
    private static void moveRight( JTable t, int row, int col, boolean extend )
    {
        col++;

        if( col > t.getColumnCount() )
        {
            row++;
            col = 0;
        }

        if( row <= t.getRowCount() )
        {
            t.changeSelection( row, col, false, extend );
        }
    }

    /***************************************************************************
     * @param t
     * @param row
     * @param col
     * @param extend
     **************************************************************************/
    private static void moveDown( JTable t, int row, int col, boolean extend )
    {
        row++;
        if( row >= t.getRowCount() )
        {
            return;
        }

        t.changeSelection( row, col, false, extend );
    }
}
