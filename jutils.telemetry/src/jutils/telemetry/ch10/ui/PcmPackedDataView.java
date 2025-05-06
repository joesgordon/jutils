package jutils.telemetry.ch10.ui;

import java.awt.Component;

import javax.swing.JPanel;

import jutils.core.ui.ClassedView.IClassedView;
import jutils.telemetry.ch10.PcmPackedData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcmPackedDataView implements IClassedView<PcmPackedData>
{
    /**  */
    private final JPanel view;

    /**  */
    private PcmPackedData pcmData;

    /***************************************************************************
     * 
     **************************************************************************/
    public PcmPackedDataView()
    {
        this.view = new JPanel();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public PcmPackedData getData()
    {
        return pcmData;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( PcmPackedData data )
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
