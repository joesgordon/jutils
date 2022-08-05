package org.jutils.core.ui.net;

import java.awt.Component;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jutils.core.IconConstants;
import org.jutils.core.net.NetUtils;
import org.jutils.core.net.NetUtils.NicInfo;
import org.jutils.core.ui.event.RightClickListener;
import org.jutils.core.ui.event.updater.IUpdater;
import org.jutils.core.ui.model.IView;

public class NetworkInterfacePopup implements IView<JPopupMenu>
{
    /**  */
    private final JPopupMenu nicMenu;
    /**  */
    private IUpdater<NicInfo> updater;

    public NetworkInterfacePopup()
    {
        this.nicMenu = new JPopupMenu();
        this.updater = null;

        buildNicMenu();
    }

    @Override
    public JPopupMenu getView()
    {
        return nicMenu;
    }

    public void addToRightClick( Component component )
    {
        RightClickListener ml = new RightClickListener(
            ( e ) -> show( e.getComponent(), e.getX(), e.getY() ) );
        component.addMouseListener( ml );
    }

    public void show( Component component, int x, int y )
    {
        nicMenu.show( component, x, y );
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
            item.addActionListener( ( e ) -> fireUpdater( nic ) );
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

    private void fireUpdater( NicInfo nic )
    {
        IUpdater<NicInfo> updater = this.updater;

        if( updater != null )
        {
            updater.update( nic );
        }
    }

    public void setUpdater( IUpdater<NicInfo> updater )
    {
        this.updater = updater;
    }
}
