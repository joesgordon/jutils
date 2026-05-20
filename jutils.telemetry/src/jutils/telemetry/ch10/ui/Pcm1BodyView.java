package jutils.telemetry.ch10.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.ui.ClassedView;
import jutils.telemetry.ch10.IPcmData;
import jutils.telemetry.ch10.Pcm1Body;
import jutils.telemetry.ch10.PcmPackedData;
import jutils.telemetry.ch10.PcmThroughputData;
import jutils.telemetry.ch10.PcmUnpackedData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Pcm1BodyView implements IPacketBodyView<Pcm1Body>
{
    /**  */
    private final JComponent view;
    /**  */
    private final Pcm1SpecificDataView psdView;
    /**  */
    private final ClassedView<IPcmData> bodyView;

    /**  */
    private Pcm1Body body;

    /***************************************************************************
     * 
     **************************************************************************/
    public Pcm1BodyView()
    {
        this.psdView = new Pcm1SpecificDataView();
        this.bodyView = new ClassedView<>();

        this.view = createView();

        bodyView.put( PcmThroughputData.class, new PcmThroughputDataView(),
            false );
        bodyView.put( PcmUnpackedData.class, new PcmUnpackedDataView(), false );
        bodyView.put( PcmPackedData.class, new PcmPackedDataView(), false );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( psdView.getView(), BorderLayout.WEST );
        panel.add( bodyView.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        psdView.setEditable( editable );
        bodyView.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Pcm1Body getData()
    {
        return this.body;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Pcm1Body data )
    {
        this.body = data;

        psdView.setData( body.specificData );
        bodyView.setData( body.data );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

}
