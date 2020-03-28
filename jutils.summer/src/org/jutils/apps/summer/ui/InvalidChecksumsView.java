package org.jutils.apps.summer.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.*;

import org.jutils.apps.summer.data.InvalidChecksum;
import org.jutils.core.ui.model.*;

/*******************************************************************************
 *
 ******************************************************************************/
public class InvalidChecksumsView implements IDataView<List<InvalidChecksum>>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ItemsTableModel<InvalidChecksum> model;

    /***************************************************************************
     *
     **************************************************************************/
    public InvalidChecksumsView()
    {
        this.model = new ItemsTableModel<>( new InvalidChecksumModel() );
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JTable table = new JTable( model );
        JScrollPane pane = new JScrollPane( table );

        table.getTableHeader().setReorderingAllowed( false );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        pane.getViewport().setBackground( table.getBackground() );

        panel.add( pane, BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public List<InvalidChecksum> getData()
    {
        return model.getItems();
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public void setData( List<InvalidChecksum> data )
    {
        model.setItems( data );
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class InvalidChecksumModel
        implements ITableItemsConfig<InvalidChecksum>
    {
        private static final Class<?> [] CLASSES = { String.class, String.class,
            String.class };
        private static final String [] NAMES = { "Read Checksum",
            "Calculated Checksum", "File" };

        @Override
        public String [] getColumnNames()
        {
            return NAMES;
        }

        @Override
        public Class<?> [] getColumnClasses()
        {
            return CLASSES;
        }

        @Override
        public Object getItemData( InvalidChecksum sum, int col )
        {
            switch( col )
            {
                case 0:
                    return sum.readSum.checksum;

                case 1:
                    return sum.calcSum.checksum;

                case 2:
                    return sum.readSum.file;

                default:
                    throw new IllegalArgumentException(
                        "Invalid column: " + col );
            }
        }

        @Override
        public void setItemData( InvalidChecksum item, int col, Object data )
        {
        }

        @Override
        public boolean isCellEditable( InvalidChecksum item, int col )
        {
            return false;
        }
    }
}
