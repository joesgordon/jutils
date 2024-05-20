package jutils.telemetry.ch10.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jutils.core.io.FieldPrinter;
import jutils.core.ui.model.IDataView;
import jutils.core.ui.net.StringWriterView;
import jutils.telemetry.ch10.Packet;
import jutils.telemetry.ch10.PacketHeader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketView implements IDataView<Packet>
{
    /**  */
    private final JComponent view;
    /**  */
    private final StringWriterView<PacketHeader> headerView;

    /**  */
    private Packet packet;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketView()
    {
        this.headerView = new StringWriterView<>(
            ( h ) -> FieldPrinter.toString( h ) );

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Header", headerView.getView() );

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
    public Packet getData()
    {
        return this.packet;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Packet data )
    {
        this.packet = data;

        headerView.setData( packet.header );
        // TODO Auto-generated method stub
    }
}
