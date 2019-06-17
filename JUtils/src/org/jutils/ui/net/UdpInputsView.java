package org.jutils.ui.net;

import javax.swing.JComponent;

import org.jutils.net.UdpInputs;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.*;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpInputsView implements IDataView<UdpInputs>
{
    /**  */
    private final StandardFormView form;
    /**  */
    private final IntegerFormField localPortField;
    /**  */
    private final IDataFormField<String> nicField;
    /**  */
    private final IntegerFormField timeoutField;
    /**  */
    private final BooleanFormField reuseField;
    /**  */
    private final Ip4AddressField remoteAddressField;
    /**  */
    private final IntegerFormField remotePortField;

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
        this.form = new StandardFormView();

        this.localPortField = new IntegerFormField( "Local Port", 0, 65535 );
        this.nicField = new NetworkInterfaceField( "NIC" );

        this.timeoutField = new IntegerFormField( "Timeout", "ms", 0, null );
        this.reuseField = new BooleanFormField( "Reuse" );

        this.remoteAddressField = new Ip4AddressField( "Remote Address" );
        this.remotePortField = new IntegerFormField( "Remote Port", 0, 65535 );

        form.addField( localPortField );
        form.addField( nicField );

        form.addField( remoteAddressField );
        form.addField( remotePortField );

        if( advanced )
        {
            form.addField( timeoutField );
            form.addField( reuseField );
        }

        setData( new UdpInputs() );

        localPortField.setUpdater( ( d ) -> inputs.localPort = d );
        nicField.setUpdater( ( d ) -> inputs.nic = d );
        timeoutField.setUpdater( ( d ) -> inputs.timeout = d );
        reuseField.setUpdater( ( d ) -> inputs.reuse = d );
        remoteAddressField.setUpdater( ( d ) -> inputs.remoteAddress.set( d ) );
        remotePortField.setUpdater( ( d ) -> inputs.remotePort = d );
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

        timeoutField.setValue( inputs.timeout );
        reuseField.setValue( inputs.reuse );

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

        timeoutField.setEditable( enabled );
        reuseField.setEditable( enabled );

        // remoteAddressField.setEditable( enabled );
        // remotePortField.setEditable( enabled );
    }
}
