package jutils.platform.ui;

import java.util.List;

import jutils.core.io.LogUtils;
import jutils.platform.IPlatform;
import jutils.platform.PlatformUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DemoPlatformMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        IPlatform platform = PlatformUtils.getPlatform();

        platform.initialize();

        List<String> ports = platform.listSerialPorts();

        for( String port : ports )
        {
            LogUtils.printDebug( "> %s", port );
        }

        platform.destroy();
    }
}
