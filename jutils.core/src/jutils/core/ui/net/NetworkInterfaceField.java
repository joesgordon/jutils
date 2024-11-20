package jutils.core.ui.net;

import javax.swing.JComponent;

import jutils.core.net.NicInfo;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.fields.StringFormField;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetworkInterfaceField implements IDataFormField<String>
{
    /**  */
    private final StringFormField nicField;
    /**  */
    private final NetworkInterfacePopup nicMenu;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public NetworkInterfaceField( String name )
    {
        this.nicField = new StringFormField( name );
        this.nicMenu = new NetworkInterfacePopup();

        nicMenu.addToRightClick( nicField.getTextField() );
        nicMenu.setUpdater( ( d ) -> handleNicChosen( d ) );
    }

    /***************************************************************************
     * @param nic
     **************************************************************************/
    private void handleNicChosen( NicInfo nic )
    {
        nicField.setValue( nic.address.getHostAddress() );
        IUpdater<String> updater = nicField.getUpdater();
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
    public String getValue()
    {
        return nicField.getValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( String value )
    {
        nicField.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<String> updater )
    {
        nicField.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<String> getUpdater()
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
