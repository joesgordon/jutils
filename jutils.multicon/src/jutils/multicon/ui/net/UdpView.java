package jutils.multicon.ui.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.JComponent;

import jutils.core.io.options.OptionsSerializer;
import jutils.core.net.*;
import jutils.core.ui.net.UdpInputsView;
import jutils.core.ui.validation.Validity;
import jutils.multicon.MulticonMain;
import jutils.multicon.MulticonOptions;
import jutils.multicon.ui.IConnectionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpView implements IConnectionView<UdpInputs>
{
    public static final String NAME = "UDP Connection";

    /**  */
    private final UdpInputsView inputsView;

    /**  */
    private UdpConnection connection;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpView()
    {
        this.inputsView = new UdpInputsView();
        this.connection = null;

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();

        inputsView.setData( new UdpInputs( userio.getOptions().udpInputs ) );

        inputsView.addValidityChanged( ( v ) -> inputsValidityChanged( v ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return inputsView.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public UdpInputs getData()
    {
        return inputsView.getData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( UdpInputs data )
    {
        inputsView.setData( data );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getTitle()
    {
        return NAME;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IConnection createConnection() throws SocketException, IOException
    {
        UdpInputs inputs = inputsView.getData();

        UdpConnection connection = new UdpConnection( inputs );

        this.connection = connection;

        return connection;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        inputsView.setEnabled( editable );
    }

    /***************************************************************************
     * @param v
     **************************************************************************/
    private void inputsValidityChanged( Validity v )
    {
        if( connection != null && v.isValid )
        {
            UdpInputs inputs = inputsView.getData();

            InetAddress address;

            address = inputs.remoteAddress.getInetAddress();

            connection.setRemote( inputs.remotePort );
            connection.setRemote( address );
        }
    }
}
