package jutils.platform.ui;

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

        platform.destroy();
    }
}
