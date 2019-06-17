package org.jutils.ui;

import java.awt.Component;

import javax.swing.*;

import org.jutils.data.UIProperty;

public class RowHeaderNumberView
{
    /**  */
    private final RowListModel rowModel;
    /**  */
    private final JList<String> rowHeader;

    public RowHeaderNumberView( JTable table )
    {
        this.rowModel = new RowListModel();
        this.rowHeader = new JList<>( rowModel );

        rowHeader.setCellRenderer( new RowHeaderRenderer( table ) );
        rowHeader.setBackground( UIProperty.PANEL_BACKGROUND.getColor() );
        rowHeader.setFixedCellHeight( table.getRowHeight() );
        rowHeader.setFixedCellWidth( 15 );
    }

    public Component getView()
    {
        return rowHeader;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class RowListModel extends AbstractListModel<String>
    {
        /**  */
        private static final long serialVersionUID = 404977197801943790L;
        /**  */
        private long rowStart;
        /**  */
        private int rowCount;

        public RowListModel()
        {
            rowStart = 0;
            rowCount = 0;
        }

        @Override
        public int getSize()
        {
            return rowCount;
        }

        public void setSize( int count )
        {
            rowCount = count;
        }

        public void setStart( long start )
        {
            rowStart = start;
        }

        @Override
        public String getElementAt( int index )
        {
            return Long.toString( rowStart + index + 1 );
        }
    }

    public void setData( long start, int count )
    {
        rowModel.setStart( start );
        rowModel.setSize( count );
        rowHeader.setFixedCellWidth( -1 );
        rowHeader.setFixedCellWidth( rowHeader.getPreferredSize().width + 16 );
        rowHeader.repaint();
    }
}
