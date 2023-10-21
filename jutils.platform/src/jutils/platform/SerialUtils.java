package jutils.platform;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class SerialUtils
{
    /**  */
    private static JniBuffers buffers;

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private SerialUtils()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static ISerialPort createSerialPort()
    {
        return new JniSerialPort();
    }

    /***************************************************************************
     * @param ports list of port names
     * @return
     **************************************************************************/
    public static List<String> listPorts()
    {
        List<String> ports = new ArrayList<String>();

        JniSerialApi.listPorts( ports );

        return ports;
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
}
