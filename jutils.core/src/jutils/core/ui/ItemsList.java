package jutils.core.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import jutils.core.ui.model.CollectionListModel;
import jutils.core.ui.model.ItemsListModel;
import jutils.core.ui.model.LabelListCellRenderer;
import jutils.core.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ItemsList<T> extends JList<T>
{
    /**  */
    private static final long serialVersionUID = 1043132025374075714L;

    /**  */
    private final CollectionListModel<T> listModel;
    /**  */
    private final ItemsListModel<T> itemModel;

    /***************************************************************************
     * @param itemModel
     * @param listModel
     **************************************************************************/
    public ItemsList( ItemsListModel<T> itemModel )
    {
        this( itemModel, new CollectionListModel<>() );
    }

    /***************************************************************************
     * @param itemModel
     * @param listModel
     **************************************************************************/
    public ItemsList( ItemsListModel<T> itemModel,
        CollectionListModel<T> listModel )
    {
        this( itemModel, listModel, new LabelListCellRenderer<T>(
            new DescriptorListCellLabelDecorator<T>( itemModel ) ) );
    }

    /***************************************************************************
     * @param itemModel
     * @param renderer
     **************************************************************************/
    public ItemsList( ItemsListModel<T> itemModel,
        ListCellRenderer<T> renderer )
    {
        this( itemModel, new CollectionListModel<>(), renderer );
    }

    /***************************************************************************
     * @param itemModel
     * @param listModel
     * @param renderer
     **************************************************************************/
    public ItemsList( ItemsListModel<T> itemModel,
        CollectionListModel<T> listModel, ListCellRenderer<T> renderer )
    {
        super( listModel );

        this.listModel = listModel;
        this.itemModel = itemModel;

        setCellRenderer( renderer );
    }

    /***************************************************************************
     * @param e
     * @return
     **************************************************************************/
    public int getIndex( MouseEvent e )
    {
        return getIndex( e.getPoint() );
    }

    /***************************************************************************
     * @param point
     * @return
     **************************************************************************/
    public int getIndex( Point point )
    {
        int index = locationToIndex( point );

        return index;
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public T getItem( int index )
    {
        return listModel.get( index );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getToolTipText( MouseEvent e )
    {
        int index = getIndex( e );

        if( index > -1 )
        {
            return itemModel.getTooltip( getItem( index ) );
        }
        return null;
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public void setItems( List<T> items )
    {
        listModel.setData( items );
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    public void addItem( T item )
    {
        listModel.add( item );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getItemCount()
    {
        return listModel.getSize();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<T> getItems()
    {
        return listModel.getData();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class DescriptorListCellLabelDecorator<T>
        implements IListCellLabelDecorator<T>
    {
        /**  */
        private final ItemsListModel<T> descriptor;

        /**
         * @param descriptor
         */
        public DescriptorListCellLabelDecorator( ItemsListModel<T> descriptor )
        {
            this.descriptor = descriptor;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void decorate( JLabel label, JList<? extends T> list, T value,
            int index, boolean isSelected, boolean cellHasFocus )
        {
            Icon icon = null;
            String text = "";

            if( value != null )
            {
                icon = descriptor.getIcon( value );
                text = descriptor.getName( value );
            }

            label.setBorder( new EmptyBorder( 0, 2, 0, 2 ) );
            label.setIcon( icon );
            label.setText( text );
        }
    }
}
