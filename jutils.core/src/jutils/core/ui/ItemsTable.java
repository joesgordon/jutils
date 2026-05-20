package jutils.core.ui;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import jutils.core.ui.model.ITableConfig;
import jutils.core.ui.model.IView;
import jutils.core.ui.model.ItemsTableModel;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ItemsTable<T> implements IView<Component>
{
    /**  */
    public final JScrollPane pane;
    /**  */
    public final JTable table;
    /**  */
    public final ItemsTableModel<T> model;

    /***************************************************************************
     * @param config
     **************************************************************************/
    public ItemsTable( ITableConfig<T> config )
    {
        this.model = new ItemsTableModel<>( config );
        this.table = new JTable( model );
        this.pane = new JScrollPane( table );

        pane.getHorizontalScrollBar().setUnitIncrement( 12 );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return pane;
    }
}
