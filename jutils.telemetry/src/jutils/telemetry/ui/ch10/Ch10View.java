package jutils.telemetry.ui.ch10;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jutils.core.ui.model.IDataView;
import jutils.telemetry.data.ch10.Ch10File;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ch10View implements IDataView<Ch10File>
{
    /**  */
    private final JComponent view;
    /**  */
    private final JTabbedPane tabs;
    /**  */
    private final Ch10InfoView infoView;
    /**  */
    private final PacketInfosView packetsView;
    /**  */
    private final TmatsView tmatsView;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ch10View()
    {
        this.packetsView = new PacketInfosView();
        this.infoView = new Ch10InfoView();
        this.tmatsView = new TmatsView();
        this.tabs = new JTabbedPane();

        this.view = createView();

        tabs.addTab( "Info", infoView.getView() );
        tabs.addTab( "Packets", packetsView.getView() );
        tabs.addTab( "TMATS", tmatsView.getView() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

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
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Ch10File getData()
    {
        return packetsView.getData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Ch10File data )
    {
        infoView.setData( data );
        packetsView.setData( data );
    }
}