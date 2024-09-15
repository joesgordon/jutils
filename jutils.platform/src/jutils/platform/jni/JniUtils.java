package jutils.platform.jni;

import jutils.platform.IPlatform;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class JniUtils
{
    /**  */
    private static JniBuffers buffers;

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private JniUtils()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    static synchronized JniBuffers getBuffers()
    {
        if( buffers == null )
        {
            buffers = new JniBuffers();
        }

        return buffers;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static synchronized IPlatform getPlatform()
    {
        JniPlatform jplat = JniPlatform.getPlatform();

        return new Platform( jplat );
    }
}
