package jutils.core.ui.event;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import jutils.core.SwingUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ResizingTableModelListener implements TableModelListener
{
    /**  */
    private final JTable table;

    /***************************************************************************
     * @param table
     **************************************************************************/
    public ResizingTableModelListener( JTable table )
    {
        this.table = table;
    }

    /***************************************************************************
     * @param table
     **************************************************************************/
    public static void addToTable( JTable table )
    {
        TableModel model = table.getModel();
        model.addTableModelListener( new ResizingTableModelListener( table ) );
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public void tableChanged( TableModelEvent e )
    {
        SwingUtilities.invokeLater( () -> resizeTable( table ) );
    }

    /***************************************************************************
     * @param table
     **************************************************************************/
    public static void resizeTable( JTable table )
    {
        SwingUtils.resizeTable( table, Integer.MAX_VALUE );
    }
}
