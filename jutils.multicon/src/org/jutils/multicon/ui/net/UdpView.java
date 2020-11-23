package org.jutils.multicon.ui.net;

import java.io.IOException;
import java.net.*;

import javax.swing.JComponent;

import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.net.*;
import org.jutils.core.ui.net.UdpInputsView;
import org.jutils.core.ui.validation.Validity;
import org.jutils.multicon.MulticonMain;
import org.jutils.multicon.MulticonOptions;
import org.jutils.multicon.ui.IConnectionView;

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

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();

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

            try
            {
                address = inputs.remoteAddress.getInetAddress();
            }
            catch( UnknownHostException ex )
            {
                return;
            }

            connection.setRemote( inputs.remotePort );
            connection.setRemote( address );
        }
    }
}
