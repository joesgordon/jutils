package org.jutils.ui.net;

import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.net.NetUtils;
import org.jutils.net.NetUtils.NicInfo;
import org.jutils.ui.event.RightClickListener;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.IDataFormField;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetworkInterfaceField implements IDataFormField<String>
{
    /**  */
    private final StringFormField nicField;
    /**  */
    private final JPopupMenu nicMenu;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public NetworkInterfaceField( String name )
    {
        this.nicField = new StringFormField( name );
        this.nicMenu = new JPopupMenu();

        buildNicMenu();

        nicField.getView().getComponent( 0 ).addMouseListener(
            new RightClickListener( ( e ) -> nicMenu.show( e.getComponent(),
                e.getX(), e.getY() ) ) );
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void buildNicMenu()
    {
        List<NicInfo> nics = NetUtils.buildNicList();

        nicMenu.removeAll();

        for( NicInfo nic : nics )
        {
            String title = nic.addressString + " : " + nic.name;
            JMenuItem item = new JMenuItem( title );
            item.addActionListener( ( e ) -> {
                nicField.setValue( nic.address.getHostAddress() );
                IUpdater<String> updater = nicField.getUpdater();
                if( updater != null )
                {
                    updater.update( nicField.getValue() );
                }
            } );
            nicMenu.add( item );
        }

        if( nics.isEmpty() )
        {
            JMenuItem item = new JMenuItem( "No NICs Detected" );
            item.setEnabled( false );
            nicMenu.add( item );
        }

        nicMenu.addSeparator();

        JMenuItem item = new JMenuItem( "Refresh",
            IconConstants.getIcon( IconConstants.REFRESH_16 ) );
        item.addActionListener( ( ae ) -> buildNicMenu() );
        nicMenu.add( item );
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
