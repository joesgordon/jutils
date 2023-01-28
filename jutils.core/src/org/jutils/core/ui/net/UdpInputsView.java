package org.jutils.core.ui.net;

import javax.swing.JComponent;

import org.jutils.core.io.LogUtils;
import org.jutils.core.io.parsers.MulticastGroupParser;
import org.jutils.core.net.IpAddress;
import org.jutils.core.net.UdpInputs;
import org.jutils.core.ui.StandardFormView;
import org.jutils.core.ui.fields.BooleanFormField;
import org.jutils.core.ui.fields.IDataFormField;
import org.jutils.core.ui.fields.IntegerFormField;
import org.jutils.core.ui.fields.UsableFormField;
import org.jutils.core.ui.model.IDataView;
import org.jutils.core.ui.validation.AggregateValidityChangedManager;
import org.jutils.core.ui.validation.IValidationField;
import org.jutils.core.ui.validation.IValidityChangedListener;
import org.jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpInputsView implements IDataView<UdpInputs>, IValidationField
{
    /**  */
    private final StandardFormView form;
    /**  */
    private final IntegerFormField localPortField;
    /**  */
    private final IDataFormField<String> nicField;
    /**  */
    private final BooleanFormField broadcastField;
    /**  */
    private final UsableFormField<IpAddress> multicastField;
    /**  */
    private final IntegerFormField timeoutField;
    /**  */
    private final BooleanFormField reuseField;
    /**  */
    private final BooleanFormField loopbackField;
    /**  */
    private final IntegerFormField ttlField;
    /**  */
    private final IpAddressField remoteAddressField;
    /**  */
    private final IntegerFormField remotePortField;

    /**  */
    private final boolean advanced;

    /**  */
    private final AggregateValidityChangedManager validityManager;

    /**  */
    private UdpInputs inputs;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpInputsView()
    {
        this( true );
    }

    /***************************************************************************
     * @param advanced shows time-to-live and timeout fields.
     **************************************************************************/
    public UdpInputsView( boolean advanced )
    {
        this.advanced = advanced;

        this.form = new StandardFormView();

        this.localPortField = new IntegerFormField( "Local Port", 0, 65535 );
        this.nicField = new NetworkInterfaceField( "NIC" );

        this.broadcastField = new BooleanFormField( "Broadcast" );
        this.multicastField = new UsableFormField<>(
            new IpAddressField( "Multicast", new MulticastGroupParser() ) );

        this.timeoutField = new IntegerFormField( "Timeout", "ms", 0, null );
        this.reuseField = new BooleanFormField( "Reuse" );

        this.loopbackField = new BooleanFormField( "Loopback" );
        this.ttlField = new IntegerFormField( "TTL", 0, 255 );

        this.remoteAddressField = new IpAddressField( "Remote Address" );
        this.remotePortField = new IntegerFormField( "Remote Port", 0, 65535 );

        this.validityManager = new AggregateValidityChangedManager();

        form.addField( localPortField );
        form.addField( nicField );

        if( advanced )
        {
            form.addField( broadcastField );
            form.addField( multicastField );

            form.addField( timeoutField );
            form.addField( reuseField );

            form.addField( loopbackField );
            form.addField( ttlField );
        }

        form.addField( remoteAddressField );
        form.addField( remotePortField );

        setData( new UdpInputs() );

        validityManager.addField( localPortField );
        validityManager.addField( nicField );

        validityManager.addField( broadcastField );
        validityManager.addField( multicastField );

        validityManager.addField( timeoutField );
        validityManager.addField( reuseField );

        validityManager.addField( loopbackField );
        validityManager.addField( ttlField );

        validityManager.addField( remoteAddressField );
        validityManager.addField( remotePortField );

        localPortField.setUpdater( ( d ) -> inputs.localPort = d );
        nicField.setUpdater( ( d ) -> inputs.nic = d );

        broadcastField.setUpdater( ( d ) -> inputs.broadcast = d );
        multicastField.setUpdater( ( d ) -> inputs.multicast.set( d ) );

        timeoutField.setUpdater( ( d ) -> inputs.timeout = d );
        reuseField.setUpdater( ( d ) -> inputs.reuse = d );

        loopbackField.setUpdater( ( d ) -> inputs.loopback = d );
        ttlField.setUpdater( ( d ) -> inputs.ttl = d );

        remoteAddressField.setUpdater( ( d ) -> {
            inputs.remoteAddress.set( d );
            LogUtils.printDebug( "Remote address is %s", inputs.remoteAddress );
        } );

        remotePortField.setUpdater( ( d ) -> {
            inputs.remotePort = d;
        } );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public UdpInputs getData()
    {
        return inputs;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( UdpInputs data )
    {
        this.inputs = data;

        localPortField.setValue( inputs.localPort );
        nicField.setValue( inputs.nic );

        broadcastField.setValue( inputs.broadcast );
        multicastField.setValue( inputs.multicast );

        timeoutField.setValue( inputs.timeout );
        reuseField.setValue( inputs.reuse );

        loopbackField.setValue( inputs.loopback );
        ttlField.setValue( inputs.ttl );

        remoteAddressField.setValue( inputs.remoteAddress );
        remotePortField.setValue( inputs.remotePort );
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        localPortField.setEditable( enabled );
        nicField.setEditable( enabled );

        broadcastField.setEditable( enabled && advanced );
        multicastField.setEditable( enabled && advanced );

        timeoutField.setEditable( enabled && advanced );
        reuseField.setEditable( enabled && advanced );

        loopbackField.setEditable( enabled && advanced );
        ttlField.setEditable( enabled && advanced );

        // remoteAddressField.setEditable( enabled );
        // remotePortField.setEditable( enabled );
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        validityManager.addValidityChanged( l );
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        validityManager.removeValidityChanged( l );
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return validityManager.getValidity();
    }
}
