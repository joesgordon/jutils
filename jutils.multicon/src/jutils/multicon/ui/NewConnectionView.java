package jutils.multicon.ui;

import java.awt.Font;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import jutils.core.ui.ItemListView;
import jutils.core.ui.ListView;
import jutils.core.ui.ListView.IListViewModel;
import jutils.core.ui.ListView.SelectionMode;
import jutils.core.ui.model.IView;
import jutils.core.ui.model.LabelListCellRenderer.IListCellLabelDecorator;
import jutils.multicon.data.LinkType;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NewConnectionView implements IView<JComponent>
{
    /**  */
    private final ItemListView<LinkType> typeList;

    /***************************************************************************
     * 
     **************************************************************************/
    public NewConnectionView()
    {
        this.typeList = new ItemListView<>( new ConnectionTypeView(),
            new ConnectionsListModel(), false, false );

        typeList.setSelectionMode( SelectionMode.SINGLE_ITEM );
        typeList.setData( LinkType.getSortedTypes() );
        typeList.setItemDecorator( new TypeListCellDecorator() );
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
        implements IListViewModel<LinkType>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( LinkType item )
        {
            return item.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LinkType promptForNew( ListView<LinkType> view )
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TypeListCellDecorator
        implements IListCellLabelDecorator<LinkType>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label,
            JList<? extends LinkType> list, LinkType value,
            int index, boolean isSelected, boolean cellHasFocus )
        {
            label.setFont(
                label.getFont().deriveFont( 16f ).deriveFont( Font.BOLD ) );
            label.setBorder( new EmptyBorder( 8, 3, 8, 0 ) );
        }
    }
}
