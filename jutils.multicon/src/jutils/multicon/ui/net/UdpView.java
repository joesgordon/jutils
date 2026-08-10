package jutils.multicon.ui.net;

import java.io.IOException;
import java.net.SocketException;

import javax.swing.JComponent;

import jutils.core.io.options.OptionsSerializer;
import jutils.core.net.*;
import jutils.core.ui.validation.Validity;
import jutils.multicon.MulticonMain;
import jutils.multicon.MulticonOptions;
import jutils.multicon.data.UdpInputs;
import jutils.multicon.ui.IConnectionView;
import jutils.multicon.ui.UdpInputsView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpView implements IConnectionView<UdpInputs>
{
    /**  */
    public static final String NAME = "UDP Connection";

    /**  */
    private final UdpInputsView inputsView;

    /**  */
    private final UdpConnection connection;

    /***************************************************************************
     * 
     **************************************************************************/
    @SuppressWarnings( "resource")
    public UdpView()
    {
        this( new UdpConnection() );
    }

    /***************************************************************************
     * @param connection
     **************************************************************************/
    public UdpView( UdpConnection connection )
    {
        this.connection = connection;
        this.inputsView = new UdpInputsView();

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
    public void connect() throws SocketException, IOException
    {
        UdpInputs inputs = inputsView.getData();

        connection.open( inputs.config, inputs.remote );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void disconnect() throws IOException
    {
        connection.close();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IConnection getConnection()
    {
        return connection;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        inputsView.setEditable( editable );
    }

    /***************************************************************************
     * @param v
     **************************************************************************/
    private void inputsValidityChanged( Validity v )
    {
        if( connection != null && v.isValid )
        {
            EndPoint remote = inputsView.getData().remote;

            connection.setRemote( remote );
        }
    }
}
