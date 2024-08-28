package jutils.platform;

import java.io.File;

import jutils.core.EmbeddedResources;
import jutils.core.io.IOUtils;
import jutils.platform.data.Platform;
import jutils.platform.jni.JniUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class PlatformUtils
{
    /**  */
    private static final File RESOURCES_DIR = IOUtils.getUsersFile( ".jutils",
        "platform" );
    /**  */
    private static boolean resourcesChecked = false;
    /**  */
    private static IPlatform PLATFORM = null;

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private PlatformUtils()
    {
        ;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static synchronized IPlatform getPlatform()
    {
        if( PLATFORM == null )
        {
            if( !resourcesChecked )
            {
                checkResources();
            }

            if( isSupported() )
            {
                PLATFORM = JniUtils.getPlatform();
            }
            else
            {
                PLATFORM = new UnimplementedPlatform();
            }
        }

        return PLATFORM;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static boolean isSupported()
    {
        Platform plat = Platform.getSystemPlatform();

        switch( plat )
        {
            case WINDOWS:
                return true;

            default:
                return false;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void checkResources()
    {
        if( isSupported() )
        {
            EmbeddedResources resources = new EmbeddedResources(
                RESOURCES_DIR );
            resources.addEmbeddedResource( PlatformUtils.class, "resources",
                "cutils_jni.dll", true );
            resources.checkResources();
            resourcesChecked = true;
        }
    }
}
