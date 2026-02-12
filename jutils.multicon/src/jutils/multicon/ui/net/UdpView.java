package jutils.multicon.ui.net;

import java.awt.*;
import java.io.IOException;
import java.net.SocketException;

import javax.swing.*;

import jutils.core.io.options.OptionsSerializer;
import jutils.core.net.*;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.net.EndPointField;
import jutils.core.ui.net.UdpConfigView;
import jutils.core.ui.validation.Validity;
import jutils.multicon.MulticonMain;
import jutils.multicon.MulticonOptions;
import jutils.multicon.ui.IConnectionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpView implements IConnectionView<UdpConfig>
{
    /**  */
    public static final String NAME = "UDP Connection";

    /**  */
    private final JComponent view;
    /**  */
    private final UdpConfigView inputsView;
    /**  */
    private final EndPointField remoteField;

    /**  */
    private UdpConnection connection;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpView()
    {
        this.inputsView = new UdpConfigView();
        this.remoteField = new EndPointField( "Remote" );
        this.view = createView();
        this.connection = null;

        OptionsSerializer<MulticonOptions> userio = MulticonMain.getUserData();

        inputsView.setData( new UdpConfig( userio.getOptions().udpInputs ) );

        inputsView.addValidityChanged( ( v ) -> inputsValidityChanged( v ) );

        remoteField.setUpdater( ( d ) -> connection.setRemote( d ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( inputsView.getView(), constraints );

        StandardFormView form = new StandardFormView();

        form.addField( remoteField );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( form.getView(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

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
    public UdpConfig getData()
    {
        return inputsView.getData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( UdpConfig data )
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
        UdpConfig inputs = inputsView.getData();
        EndPoint remote = remoteField.getValue();

        UdpConnection connection = new UdpConnection( inputs, remote );

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
            EndPoint remote = remoteField.getValue();

            connection.setRemote( remote );
        }
    }
}
