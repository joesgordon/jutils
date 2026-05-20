package jutils.telemetry.ch10.ui;

import java.awt.Component;

import javax.swing.JPanel;

import jutils.core.ui.ClassedView.IClassedView;
import jutils.telemetry.ch10.PcmUnpackedData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcmUnpackedDataView implements IClassedView<PcmUnpackedData>
{
    /**  */
    private final JPanel view;

    /**  */
    private PcmUnpackedData pcmData;

    /***************************************************************************
     * 
     **************************************************************************/
    public PcmUnpackedDataView()
    {
        this.view = new JPanel();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public PcmUnpackedData getData()
    {
        return pcmData;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( PcmUnpackedData data )
    {
        this.pcmData = data;

        // TODO Auto-generated method stub
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
    public void setEditable( boolean editable )
    {
        // TODO Auto-generated method stub
    }
}
