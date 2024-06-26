package jutils.core.ui.sheet;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface ISpreadSheet
{
    /***************************************************************************
     * @return
     **************************************************************************/
    public int getColumnCount();

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getRowCount();

    /***************************************************************************
     * @param row
     * @param col
     * @return
     **************************************************************************/
    public Object getValueAt( int row, int col );

    /***************************************************************************
     * @param col
     * @return
     **************************************************************************/
    public String getColumnHeader( int col );

    /***************************************************************************
     * @param row
     * @return
     **************************************************************************/
    public String getRowHeader( int row );

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getCornerName();

    /***************************************************************************
     * @param string
     * @param row
     * @param col
     **************************************************************************/
    public void setValueAt( Object string, int row, int col );

    /***************************************************************************
     * @param row
     * @param col
     * @return
     **************************************************************************/
    public boolean isEditable( int row, int col );
}
