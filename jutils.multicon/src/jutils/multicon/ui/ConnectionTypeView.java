package jutils.multicon.ui;

import java.awt.Component;

import jutils.core.ui.ComponentView;
import jutils.core.ui.model.IDataView;
import jutils.core.ui.net.TcpInputsView;
import jutils.core.ui.net.UdpConfigView;
import jutils.multicon.data.LinkType;
import jutils.platform.ui.SerialConfigView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ConnectionTypeView implements IDataView<LinkType>
{
    /**  */
    private final ComponentView view;

    /**  */
    private final UdpConfigView udpView;
    /**  */
    private final TcpInputsView tcpConnectView;
    /**  */
    private final TcpInputsView tcpListenView;
    /**  */
    private final SerialConfigView serialView;
    /**  */
    private final BridgeConfigView bridgeView;

    /**  */
    private LinkType type;

    /***************************************************************************
     * 
     **************************************************************************/
    public ConnectionTypeView()
    {
        this.view = new ComponentView();
        this.udpView = new UdpConfigView();
        this.tcpConnectView = new TcpInputsView( false, true );
        this.tcpListenView = new TcpInputsView( true, true );
        this.serialView = new SerialConfigView();
        this.bridgeView = new BridgeConfigView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LinkType getData()
    {
        return type;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( LinkType data )
    {
        this.type = data;

        switch( type )
        {
            case UDP:
                view.setComponent( udpView.getView() );
                break;

            case TCP_CONNECT:
                view.setComponent( tcpConnectView.getView() );
                break;

            case TCP_SERVER:
                view.setComponent( tcpListenView.getView() );
                break;

            case SERIAL:
                view.setComponent( serialView.getView() );
                break;

            case BRIDGE:
                view.setComponent( bridgeView.getView() );
                break;

            default:
                view.setComponent( null );
                break;
        }
    }
}
