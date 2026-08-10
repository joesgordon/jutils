package jutils.core.ui.net;

import javax.swing.JComponent;

import jutils.core.net.TcpServerConfig;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.IntegerFormField;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpServerConfigView implements IDataView<TcpServerConfig>
{
    /**  */
    private final StandardFormView form;
    /**  */
    private final NetworkInterfaceField nicField;
    /**  */
    private final IntegerFormField localPortField;
    /**  */
    private final IntegerFormField backlogField;
    /**  */
    private final IntegerFormField timeoutField;

    /**  */
    private TcpServerConfig config;

    /***************************************************************************
     * @param advanced shows time-to-live and timeout fields.
     **************************************************************************/
    public TcpServerConfigView( boolean advanced )
    {
        this.form = new StandardFormView();

        this.localPortField = new IntegerFormField( "Local Port", 0, 65535 );
        this.nicField = new NetworkInterfaceField( "NIC" );

        this.backlogField = new IntegerFormField( "Backlog", 1, null );
        this.timeoutField = new IntegerFormField( "Timeout", "ms", 0, null );

        form.addField( localPortField );
        form.addField( nicField );

        if( advanced )
        {
            form.addField( backlogField );
            form.addField( timeoutField );
        }

        setData( new TcpServerConfig() );

        nicField.setUpdater( ( d ) -> config.local.address.set( d ) );
        localPortField.setUpdater( ( d ) -> config.local.port = d );
        backlogField.setUpdater( ( d ) -> config.backlog = d );
        timeoutField.setUpdater( ( d ) -> config.timeout = d );
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
    public TcpServerConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( TcpServerConfig data )
    {
        this.config = data;

        nicField.setValue( config.local.address );
        localPortField.setValue( config.local.port );

        backlogField.setValue( config.backlog );
        timeoutField.setValue( config.timeout );
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        localPortField.setEditable( enabled );
        nicField.setEditable( enabled );

        backlogField.setEditable( enabled );
        timeoutField.setEditable( enabled );
    }
}
