package org.jutils.ui.net;

import java.awt.event.MouseEvent;
import java.net.*;
import java.util.Enumeration;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.net.NetUtils;
import org.jutils.net.NetUtils.NicInfo;
import org.jutils.ui.event.RightClickListener;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.*;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetworkInterfaceComboField implements IDataFormField<String>
{
    /**  */
    private final ComboFormField<NicInfo> nicField;
    /**  */
    private final NicInfoDescriptor descriptor;
    /**  */
    private final NicUpdater updater;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public NetworkInterfaceComboField( String name )
    {
        this.descriptor = new NicInfoDescriptor();
        this.nicField = new ComboFormField<>( name, NetUtils.buildNicList(),
            descriptor );

        this.updater = new NicUpdater();

        nicField.setUpdater( updater );

        nicField.getView().getComponent( 0 ).addMouseListener(
            new RightClickListener( ( e ) -> showMenu( e ) ) );
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void showMenu( MouseEvent e )
    {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem item = new JMenuItem( "Refresh",
            IconConstants.getIcon( IconConstants.REFRESH_16 ) );
        item.addActionListener(
            ( ae ) -> nicField.setValues( NetUtils.buildNicList() ) );
        menu.add( item );

        menu.show( e.getComponent(), e.getX(), e.getY() );
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
        NicInfo choice = nicField.getValue();

        return choice == null ? null : choice.name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( String value )
    {
        NicInfo info = null;

        if( value != null )
        {
            try
            {
                NetworkInterface nic = null;
                InetAddress address = null;

                nic = NetworkInterface.getByName( value );
                Enumeration<InetAddress> addresses = nic.getInetAddresses();

                if( addresses.hasMoreElements() )
                {
                    address = addresses.nextElement();
                    info = new NicInfo( nic, address );
                }
                else
                {
                    info = NicInfo.createAny();
                }
            }
            catch( SocketException ex )
            {
                info = NicInfo.createAny();
            }
        }
        else
        {
            info = NicInfo.createAny();
        }

        nicField.setValue( info );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<String> updater )
    {
        this.updater.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<String> getUpdater()
    {
        return updater.getUpdater();
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class NicInfoDescriptor implements IDescriptor<NicInfo>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getDescription( NicInfo choice )
        {
            return choice.name + " : " + choice.addressString;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private final static class NicUpdater implements IUpdater<NicInfo>
    {
        /**  */
        private IUpdater<String> updater;

        /**
         * {@inheritDoc}
         */
        @Override
        public void update( NicInfo choice )
        {
            if( updater != null )
            {
                String name = choice.name;
                updater.update( name );
            }
        }

        /**
         * @return
         */
        public IUpdater<String> getUpdater()
        {
            return updater;
        }

        /**
         * @param updater
         */
        public void setUpdater( IUpdater<String> updater )
        {
            this.updater = updater;
        }
    }
}
