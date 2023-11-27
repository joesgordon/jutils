package jutils.core.ui;

import java.awt.Component;

import javax.swing.JTabbedPane;

import jutils.core.data.DataItemPair;
import jutils.core.ui.hex.ByteBuffer;
import jutils.core.ui.hex.HexPanel;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class DataItemPairView<T> implements IDataView<DataItemPair<T>>
{
    /**  */
    private final JTabbedPane view;
    /**  */
    private final IDataView<T> itemView;
    /**  */
    private final HexPanel dataView;

    /**  */
    private DataItemPair<T> pair;

    /***************************************************************************
     * @param itemView
     **************************************************************************/
    public DataItemPairView( IDataView<T> itemView )
    {
        this.view = new JTabbedPane();
        this.itemView = itemView;
        this.dataView = new HexPanel();

        this.pair = new DataItemPair<>( new byte[0], null );

        view.addTab( "Info", itemView.getView() );
        view.addTab( "Data", dataView.getView() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public DataItemPair<T> getData()
    {
        return pair;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( DataItemPair<T> data )
    {
        this.pair = data;

        itemView.setData( pair.item );
        dataView.setBuffer( new ByteBuffer( pair.data ) );
    }
}
