package jutils.telemetry.ui.ch10;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import jutils.core.io.FieldPrinter;
import jutils.core.ui.hex.ByteBuffer;
import jutils.core.ui.hex.HexPanel;
import jutils.core.ui.model.IDataView;
import jutils.core.ui.net.StringWriterView;
import jutils.telemetry.data.ch10.Packet;
import jutils.telemetry.data.ch10.PacketInfo;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketInfoView implements IDataView<PacketInfo>
{
    /**  */
    private final JComponent view;
    /**  */
    private final StringWriterView<Packet> packetView;
    /**  */
    private final PacketBodyView bodyView;
    /**  */
    private final HexPanel dataView;

    /**  */
    private PacketInfo packet;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketInfoView()
    {
        this.packetView = new StringWriterView<>(
            ( h ) -> FieldPrinter.toString( h ) );
        this.bodyView = new PacketBodyView();
        this.dataView = new HexPanel();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JScrollPane pane = new JScrollPane( packetView.getView() );

        JPanel panel = new JPanel( new GridBagLayout() );

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Packet", pane );
        tabs.addTab( "Body", bodyView.getView() );
        tabs.addTab( "Data", dataView.getView() );

        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 10, 10, 10, 10 ), 0, 0 );
        panel.add( tabs, constraints );

        return panel;
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
    public PacketInfo getData()
    {
        return this.packet;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( PacketInfo data )
    {
        this.packet = data;

        packetView.setData( packet.packet );
        bodyView.setData( data.packet.body );
        dataView.setBuffer( new ByteBuffer( packet.data ) );
    }
}
