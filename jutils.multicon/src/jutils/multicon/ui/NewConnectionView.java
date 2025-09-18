package jutils.multicon.ui;

import java.awt.Font;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import jutils.core.ui.ItemListView;
import jutils.core.ui.ListView;
import jutils.core.ui.ListView.IItemListModel;
import jutils.core.ui.ListView.SelectionMode;
import jutils.core.ui.model.IView;
import jutils.core.ui.model.LabelListCellRenderer.IListCellLabelDecorator;
import jutils.multicon.data.ConnectionType;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NewConnectionView implements IView<JComponent>
{
    /**  */
    private final ItemListView<ConnectionType> typeList;

    /***************************************************************************
     * 
     **************************************************************************/
    public NewConnectionView()
    {
        this.typeList = new ItemListView<>( new ConnectionTypeView(),
            new ConnectionsListModel(), false, false );

        typeList.setSelectionMode( SelectionMode.SINGLE_ITEM );
        typeList.setData( getSortedTypes() );
        typeList.setItemDecorator( new TypeListCellRenderer() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private List<ConnectionType> getSortedTypes()
    {
        ArrayList<ConnectionType> types = new ArrayList<>(
            Arrays.asList( ConnectionType.values() ) );

        Collections.sort( types, ( this1, that1 ) -> {
            return Integer.compare( this1.value, that1.value );
        } );

        return types;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return typeList.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ConnectionsListModel
        implements IItemListModel<ConnectionType>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( ConnectionType item )
        {
            return item.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ConnectionType promptForNew( ListView<ConnectionType> view )
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TypeListCellRenderer
        implements IListCellLabelDecorator<ConnectionType>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label,
            JList<? extends ConnectionType> list, ConnectionType value,
            int index, boolean isSelected, boolean cellHasFocus )
        {
            label.setFont(
                label.getFont().deriveFont( 16f ).deriveFont( Font.BOLD ) );
            label.setBorder( new EmptyBorder( 8, 3, 8, 0 ) );
        }
    }
}
