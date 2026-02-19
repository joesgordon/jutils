package jutils.core.ui.net;

import java.awt.Component;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jutils.core.IconConstants;
import jutils.core.net.IpAddress;
import jutils.core.net.IpVersion;
import jutils.core.net.NetUtils;
import jutils.core.ui.event.RightClickListener;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * Defines a {@link JPopupMenu} that displays the IP addresses of the local
 * network interfaces.
 ******************************************************************************/
public class NetworkInterfacePopup implements IView<JPopupMenu>
{
    /** The menu of all IP addresses. */
    private final JPopupMenu nicMenu;

    /** The callback invoked when a selection is made. Always non-null. */
    private IUpdater<IpAddress> updater;

    /***************************************************************************
     * Creates a new popup menu with both IPv4 and IPv6 addresses
     **************************************************************************/
    public NetworkInterfacePopup()
    {
        this( null );
    }

    /***************************************************************************
     * Creates a new popup menu with only the specified version.
     * @param version either IPv4, IPv6, or {@code null} for both.
     **************************************************************************/
    public NetworkInterfacePopup( IpVersion version )
    {
        this.nicMenu = new JPopupMenu();
        this.updater = ( d ) -> {
        };

        buildNicMenu( version );
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
     * Adds this popup to the components mouse listeners, displaying on
     * right-click only.
     * @param component the component to display the menu on right-click.
     **************************************************************************/
    public void addToRightClick( Component component )
    {
        RightClickListener ml = new RightClickListener(
            ( e ) -> show( e.getComponent(), e.getX(), e.getY() ) );
        component.addMouseListener( ml );
    }

    /***************************************************************************
     * Displays this menu for the provided component at its x/y position.
     * @param component the component to display the menu.
     * @param x the x position in the component's space.
     * @param y the y position in the component's space.
     **************************************************************************/
    public void show( Component component, int x, int y )
    {
        nicMenu.show( component, x, y );
    }

    /***************************************************************************
     * Builds the list of local addresses and sets up the callbacks for menu
     * selection.
     * @param version either IPv4, IPv6, or {@code null} for both.
     **************************************************************************/
    private void buildNicMenu( IpVersion version )
    {
        List<IpAddress> ips = NetUtils.listLocalAddresses( version );

        nicMenu.removeAll();

        for( IpAddress ip : ips )
        {
            String title = ip.toString();
            JMenuItem item = new JMenuItem( title );
            item.addActionListener( ( e ) -> updater.update( ip ) );
            nicMenu.add( item );
        }

        if( ips.isEmpty() )
        {
            JMenuItem item = new JMenuItem( "No Addresses Detected" );
            item.setEnabled( false );
            nicMenu.add( item );
        }

        nicMenu.addSeparator();

        JMenuItem item = new JMenuItem( "Refresh",
            IconConstants.getIcon( IconConstants.REFRESH_16 ) );
        item.addActionListener( ( ae ) -> buildNicMenu( version ) );
        nicMenu.add( item );
    }

    /***************************************************************************
     * Sets the callback invoked when a selection is made.
     * @param updater the callback to be invoked.
     **************************************************************************/
    public void setUpdater( IUpdater<IpAddress> updater )
    {
        this.updater = updater != null ? updater : ( d ) -> {
        };
    }
}
