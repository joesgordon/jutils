package jutils.telemetry.ui.ch10;

import java.awt.Component;

import jutils.core.ui.ClassedView.IClassedView;
import jutils.core.ui.hex.ShiftHexView;
import jutils.telemetry.data.ch10.PcmThroughputData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcmThroughputDataView implements IClassedView<PcmThroughputData>
{
    /**  */
    private final ShiftHexView dataView;

    /**  */
    private PcmThroughputData pcmData;

    /***************************************************************************
     * 
     **************************************************************************/
    public PcmThroughputDataView()
    {
        this.dataView = new ShiftHexView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public PcmThroughputData getData()
    {
        return this.pcmData;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( PcmThroughputData data )
    {
        this.pcmData = data;

        dataView.setData( pcmData.data );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return dataView.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        dataView.setEditable( editable );
    }
}
