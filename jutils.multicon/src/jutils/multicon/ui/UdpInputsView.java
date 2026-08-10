package jutils.multicon.ui;

import javax.swing.JComponent;

import jutils.core.ui.model.IDataView;
import jutils.core.ui.net.*;
import jutils.core.ui.validation.*;
import jutils.multicon.data.UdpInputs;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpInputsView implements IDataView<UdpInputs>, IValidationField
{
    /**  */
    private final UdpConfigView configView;
    /**  */
    private final IpAddressField remoteAddressField;
    /**  */
    private final PortField remotePortField;

    /**  */
    private final AggregateValidityChangedManager validityManager;

    /**  */
    private UdpInputs inputs;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpInputsView()
    {
        this.configView = new UdpConfigView();
        this.remoteAddressField = new IpAddressField( "Remote Address" );
        this.remotePortField = new PortField( "Remote Port" );

        this.validityManager = new AggregateValidityChangedManager();

        configView.addField( remoteAddressField );
        configView.addField( remotePortField );

        validityManager.addField( configView );
        validityManager.addField( remoteAddressField );
        validityManager.addField( remotePortField );

        setData( new UdpInputs() );

        remoteAddressField.setUpdater(
            ( d ) -> inputs.remote.address.set( d ) );
        remotePortField.setUpdater( ( d ) -> inputs.remote.port = d );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        validityManager.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        validityManager.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return validityManager.getValidity();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return configView.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public UdpInputs getData()
    {
        return inputs;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( UdpInputs data )
    {
        this.inputs = data;

        configView.setData( inputs.config );
        remoteAddressField.setValue( inputs.remote.address );
        remotePortField.setValue( inputs.remote.port );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        configView.setEditable( editable );
        remoteAddressField.setEditable( editable );
        remotePortField.setEditable( editable );
    }
}
