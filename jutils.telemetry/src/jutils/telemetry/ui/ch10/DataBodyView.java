package jutils.telemetry.ui.ch10;

import java.awt.Component;

import jutils.core.ui.hex.ByteBuffer;
import jutils.core.ui.hex.HexPanel;
import jutils.telemetry.data.ch10.DataBody;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataBodyView implements IPacketBodyView<DataBody>
{
    /**  */
    private final HexPanel panel;
    /**  */
    private DataBody body;

    /***************************************************************************
     * 
     **************************************************************************/
    public DataBodyView()
    {
        this.panel = new HexPanel();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public DataBody getData()
    {
        return this.body;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( DataBody data )
    {
        this.body = data;

        panel.setBuffer( new ByteBuffer( body.data ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return panel.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean hasScrollbar()
    {
        return true;
    }

}
