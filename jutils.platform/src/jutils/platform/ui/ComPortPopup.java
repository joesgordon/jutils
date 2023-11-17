package jutils.platform.ui;

import java.awt.Component;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jutils.core.IconConstants;
import jutils.core.ui.event.RightClickListener;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IView;
import jutils.platform.IPlatform;
import jutils.platform.PlatformUtils;

/*******************************************************************************
 *
 ******************************************************************************/
public class ComPortPopup implements IView<JPopupMenu>
{
    /**  */
    private final JPopupMenu menu;
    /**  */
    private IUpdater<String> updater;

    /***************************************************************************
     * 
     **************************************************************************/
    public ComPortPopup()
    {
        this.menu = new JPopupMenu();
        this.updater = null;

        buildMenu();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPopupMenu getView()
    {
        return menu;
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
        menu.show( component, x, y );
    }

    /***************************************************************************
     * @param ipv4Only
     * @param e
     **************************************************************************/
    private void buildMenu()
    {
        IPlatform platform = PlatformUtils.getPlatform();
        String errStr = "";

        if( platform.isInialized() )
        {
            List<String> ports = platform.listSerialPorts();

            menu.removeAll();

            for( String port : ports )
            {
                String title = port;
                JMenuItem item = new JMenuItem( title );
                item.addActionListener( ( e ) -> fireUpdater( port ) );
                menu.add( item );
            }

            if( ports.isEmpty() )
            {
                errStr = "No Comm Ports Detected";
            }
        }
        else
        {
            errStr = "Platform is not initialized";
        }

        if( !errStr.isEmpty() )
        {
            JMenuItem item = new JMenuItem( errStr );
            item.setEnabled( false );
            menu.add( item );
        }

        menu.addSeparator();

        JMenuItem item = new JMenuItem( "Refresh",
            IconConstants.getIcon( IconConstants.REFRESH_16 ) );
        item.addActionListener( ( ae ) -> buildMenu() );
        menu.add( item );
    }

    /***************************************************************************
     * @param port
     **************************************************************************/
    private void fireUpdater( String port )
    {
        IUpdater<String> updater = this.updater;

        if( updater != null )
        {
            updater.update( port );
        }
    }

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void setUpdater( IUpdater<String> updater )
    {
        this.updater = updater;
    }
}
