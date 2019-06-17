package org.jutils.ui.sheet;

import javax.swing.table.AbstractTableModel;

public class SheetModel extends AbstractTableModel
{
    /**  */
    private static final long serialVersionUID = 191109670865335200L;
    private ISpreadSheet sheet;

    public SheetModel()
    {
        sheet = new NullSheet();
    }

    public void setData( ISpreadSheet file )
    {
        this.sheet = file;

        super.fireTableStructureChanged();
    }

    public ISpreadSheet getData()
    {
        return sheet;
    }

    @Override
    public boolean isCellEditable( int row, int col )
    {
        return sheet.isEditable( row, col );
    }

    @Override
    public String getColumnName( int col )
    {
        String name = sheet.getColumnHeader( col );

        if( name == null )
        {
            name = generateDefautColumnName( col );
        }

        return name;
    }

    @Override
    public Object getValueAt( int row, int col )
    {
        return sheet.getValueAt( row, col );
    }

    @Override
    public void setValueAt( Object value, int row, int col )
    {
        sheet.setValueAt( value.toString(), row, col );
    }

    @Override
    public int getRowCount()
    {
        return sheet.getRowCount();
    }

    @Override
    public int getColumnCount()
    {
        return sheet.getColumnCount();
    }

    private static String generateDefautColumnName( int col )
    {
        String name = "";
        int val;
        char c;

        do
        {
            val = col % 26;
            c = ( char )( 'A' + val );
            name = c + name;

            col /= 26;
            col--;
        } while( col > -1 );

        return name;
    }

    private static class NullSheet implements ISpreadSheet
    {
        @Override
        public int getColumnCount()
        {
            return 0;
        }

        @Override
        public int getRowCount()
        {
            return 0;
        }

        @Override
        public Object getValueAt( int row, int col )
        {
            return null;
        }

        @Override
        public String getColumnHeader( int col )
        {
            return null;
        }

        @Override
        public String getRowHeader( int row )
        {
            return null;
        }

        @Override
        public void setValueAt( Object string, int row, int col )
        {
            ;
        }

        @Override
        public String getCornerName()
        {
            return null;
        }

        @Override
        public boolean isEditable( int row, int col )
        {
            return false;
        }
    }
}
