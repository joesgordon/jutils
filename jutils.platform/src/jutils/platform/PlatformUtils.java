package jutils.platform;

import java.io.File;

import jutils.core.EmbeddedResources;
import jutils.core.io.IOUtils;
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

            PLATFORM = JniUtils.getPlatform();
        }

        return PLATFORM;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void checkResources()
    {
        EmbeddedResources resources = new EmbeddedResources( RESOURCES_DIR );
        resources.addEmbeddedResource( PlatformUtils.class, "resources",
            "cutils_jni.dll", true );
        resources.checkResources();
        resourcesChecked = true;
    }
}
