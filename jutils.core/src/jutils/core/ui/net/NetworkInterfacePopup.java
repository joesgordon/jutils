package jutils.core.ui.net;

import java.awt.Component;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jutils.core.IconConstants;
import jutils.core.net.NetUtils;
import jutils.core.net.NetUtils.NicInfo;
import jutils.core.ui.event.RightClickListener;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class NetworkInterfacePopup implements IView<JPopupMenu>
{
    /**  */
    private final JPopupMenu nicMenu;
    /**  */
    private IUpdater<NicInfo> updater;

    /***************************************************************************
     * 
     **************************************************************************/
    public NetworkInterfacePopup()
    {
        this( false );
    }

    /***************************************************************************
     * @param ipv4Only
     **************************************************************************/
    public NetworkInterfacePopup( boolean ipv4Only )
    {
        this.nicMenu = new JPopupMenu();
        this.updater = null;

        buildNicMenu( ipv4Only );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPopupMenu getView()
    {
        return nicMenu;
    }

    /***************************************************************************
     * @param component
     **************************************************************************/
    public void addToRightClick( Component component )
    {
        RightClickListener ml = new RightClickListener(
            ( e ) -> show( e.getComponent(), e.getX(), e.getY() ) );
        component.addMouseListener( ml );
    }

    /***************************************************************************
     * @param component
     * @param x
     * @param y
     **************************************************************************/
    public void show( Component component, int x, int y )
    {
        nicMenu.show( component, x, y );
    }

    /***************************************************************************
     * @param ipv4Only
     * @param e
     **************************************************************************/
    private void buildNicMenu( boolean ipv4Only )
    {
        List<NicInfo> nics = NetUtils.buildNicList();

        nicMenu.removeAll();

        for( NicInfo nic : nics )
        {
            if( !ipv4Only || nic.isIpv4 )
            {
                String title = nic.addressString + " : " + nic.name;
                JMenuItem item = new JMenuItem( title );
                item.addActionListener( ( e ) -> fireUpdater( nic ) );
                nicMenu.add( item );
            }
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
        item.addActionListener( ( ae ) -> buildNicMenu( ipv4Only ) );
        nicMenu.add( item );
    }

    /***************************************************************************
     * @param nic
     **************************************************************************/
    private void fireUpdater( NicInfo nic )
    {
        IUpdater<NicInfo> updater = this.updater;

        if( updater != null )
        {
            updater.update( nic );
        }
    }

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void setUpdater( IUpdater<NicInfo> updater )
    {
        this.updater = updater;
    }
}
