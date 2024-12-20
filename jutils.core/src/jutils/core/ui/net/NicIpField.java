package jutils.core.ui.net;

import java.awt.Point;

import jutils.core.net.IpAddress;
import jutils.core.net.NicInfo;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NicIpField extends IpAddressField
{
    /**  */
    private final NetworkInterfacePopup nicMenu;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public NicIpField( String name )
    {
        super( name );

        this.nicMenu = new NetworkInterfacePopup();

        nicMenu.setUpdater( ( d ) -> handleNicChosen( d ) );
    }

    /***************************************************************************
     * @param point
     **************************************************************************/
    @Override
    protected void showMenu( Point point )
    {
        nicMenu.show( getView(), point.x, point.y );
    }

    /***************************************************************************
     * @param nic
     **************************************************************************/
    private void handleNicChosen( NicInfo nic )
    {
        IpAddress ip = getValue();
        ip.setInetAddress( nic.address );
        setValue( ip );
    }
}
