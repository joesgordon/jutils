package org.jutils.ui.net;

import javax.swing.JComponent;

import org.jutils.io.parsers.MulticastGroupParser;
import org.jutils.net.MulticastInputs;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.*;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastInputsView implements IDataView<MulticastInputs>
{
    /**  */
    private final StandardFormView form;
    /**  */
    private final Ip4AddressField addressField;
    /**  */
    private final IntegerFormField portField;
    /**  */
    private final IDataFormField<String> nicField;
    /**  */
    private final IntegerFormField ttlField;
    /**  */
    private final IntegerFormField timeoutField;
    /**  */
    private final BooleanFormField loopbackField;

    /**  */
    private MulticastInputs inputs;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticastInputsView()
    {
        this( true );
    }

    /***************************************************************************
     * @param advanced shows time-to-live and timeout fields.
     **************************************************************************/
    public MulticastInputsView( boolean advanced )
    {
        this.form = new StandardFormView();
        this.addressField = new Ip4AddressField( "Multicast Group",
            new MulticastGroupParser() );
        this.portField = new IntegerFormField( "Port", 0, 65535 );
        this.nicField = new NetworkInterfaceField( "NIC" );
        this.ttlField = new IntegerFormField( "TTL", 0, 255 );
        this.timeoutField = new IntegerFormField( "Timeout", "ms", 0, null );
        this.loopbackField = new BooleanFormField( "Loopback" );

        form.addField( addressField );
        form.addField( portField );
        form.addField( nicField );

        if( advanced )
        {
            form.addField( ttlField );
            form.addField( timeoutField );
            form.addField( loopbackField );
        }

        setData( new MulticastInputs() );

        addressField.setUpdater( ( d ) -> inputs.group.set( d ) );
        portField.setUpdater( ( d ) -> inputs.port = d );
        nicField.setUpdater( ( d ) -> inputs.nic = d );
        ttlField.setUpdater( ( d ) -> inputs.ttl = d );
        timeoutField.setUpdater( ( d ) -> inputs.timeout = d );
        loopbackField.setUpdater( ( d ) -> inputs.loopback = d );
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
    public MulticastInputs getData()
    {
        return inputs;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( MulticastInputs data )
    {
        this.inputs = data;

        addressField.setValue( inputs.group );
        portField.setValue( inputs.port );
        nicField.setValue( inputs.nic );
        ttlField.setValue( inputs.ttl );
        timeoutField.setValue( inputs.timeout );
        loopbackField.setValue( inputs.loopback );
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        addressField.setEditable( enabled );
        portField.setEditable( enabled );
        nicField.setEditable( enabled );
        ttlField.setEditable( enabled );
        timeoutField.setEditable( enabled );
        loopbackField.setEditable( enabled );
    }
}
