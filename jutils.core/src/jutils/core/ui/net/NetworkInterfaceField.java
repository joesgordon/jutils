package jutils.core.ui.net;

import javax.swing.JComponent;

import jutils.core.net.IpAddress;
import jutils.core.net.IpVersion;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetworkInterfaceField implements IDataFormField<IpAddress>
{
    /**  */
    private final IpAddressField nicField;
    /**  */
    private final NetworkInterfacePopup nicMenu;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public NetworkInterfaceField( String name )
    {
        this( name, null );
    }

    /***************************************************************************
     * @param name
     * @param version
     **************************************************************************/
    public NetworkInterfaceField( String name, IpVersion version )
    {
        this.nicField = new IpAddressField( name );
        this.nicMenu = new NetworkInterfacePopup( version );

        nicMenu.addToRightClick( nicField.getTextField() );
        nicMenu.setUpdater( ( d ) -> handleNicChosen( d ) );
    }

    /***************************************************************************
     * @param address
     **************************************************************************/
    private void handleNicChosen( IpAddress address )
    {
        nicField.setValue( address );
        IUpdater<IpAddress> updater = nicField.getUpdater();
        if( updater != null )
        {
            updater.update( nicField.getValue() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return nicField.getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return nicField.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IpAddress getValue()
    {
        return nicField.getValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( IpAddress value )
    {
        nicField.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<IpAddress> updater )
    {
        nicField.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<IpAddress> getUpdater()
    {
        return nicField.getUpdater();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        nicField.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        nicField.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        nicField.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return nicField.getValidity();
    }
}
