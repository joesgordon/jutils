package jutils.platform.ui;

import javax.swing.JComponent;

import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.fields.StringFormField;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ComPortField implements IDataFormField<String>
{
    /**  */
    private final StringFormField portField;
    /**  */
    private final ComPortPopup menu;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public ComPortField( String name )
    {
        this.portField = new StringFormField( name );
        this.menu = new ComPortPopup();

        menu.addToRightClick( portField.getTextField() );
        menu.setUpdater( ( d ) -> handlePortChosen( d ) );
    }

    /***************************************************************************
     * @param port
     **************************************************************************/
    private void handlePortChosen( String port )
    {
        portField.setValue( port );
        IUpdater<String> updater = portField.getUpdater();
        if( updater != null )
        {
            updater.update( portField.getValue() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return portField.getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return portField.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getValue()
    {
        return portField.getValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( String value )
    {
        portField.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<String> updater )
    {
        portField.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<String> getUpdater()
    {
        return portField.getUpdater();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        portField.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        portField.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        portField.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return portField.getValidity();
    }
}
