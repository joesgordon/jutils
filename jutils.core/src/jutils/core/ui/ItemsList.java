package jutils.core.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;

import jutils.core.ui.model.CollectionListModel;
import jutils.core.ui.model.ItemsListModel;
import jutils.core.ui.model.LabelListCellRenderer;
import jutils.core.ui.model.LabelListCellRenderer.IListCellLabelDecorator;

/*******************************************************************************
 * @param <D>
 ******************************************************************************/
public class ItemsList<D> extends JList<D>
{
    /**  */
    private static final long serialVersionUID = 1043132025374075714L;

    /**  */
    private final CollectionListModel<D> listModel;
    /**  */
    private final ItemsListModel<D> itemModel;

    /***************************************************************************
     * @param itemModel
     * @param listModel
     **************************************************************************/
    public ItemsList( ItemsListModel<D> itemModel )
    {
        this( itemModel, new CollectionListModel<>() );
    }

    /***************************************************************************
     * @param itemModel
     * @param listModel
     **************************************************************************/
    public ItemsList( ItemsListModel<D> itemModel,
        CollectionListModel<D> listModel )
    {
        super( listModel );

        this.listModel = listModel;
        this.itemModel = itemModel;

        setCellRenderer( new LabelListCellRenderer<D>(
            new DescriptorListCellLabelDecorator<D>( itemModel ) ) );
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
    public D getItem( int index )
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
    public void setItems( List<D> items )
    {
        listModel.setData( items );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getItemCount()
    {
        return listModel.getSize();
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
