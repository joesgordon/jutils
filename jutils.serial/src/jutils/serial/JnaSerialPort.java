package jutils.serial;

import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
class JnaSerialPort
{
    /***************************************************************************
     * @param name
     * @param config
     * @param buffer
     * @return the handle of the opened port.
     **************************************************************************/
    static native int open( String name, JnaPortConfig config );

    /***************************************************************************
     * @param handle
     **************************************************************************/
    static native void close( int handle );

    /***************************************************************************
     * @param handle
     * @param config
     * @return
     **************************************************************************/
    static native boolean getConfig( int handle, JnaPortConfig config );

    /***************************************************************************
     * @param handle
     * @param config
     * @return
     **************************************************************************/
    static native boolean setConfig( int handle, JnaPortConfig config );

    /***************************************************************************
     * @param ports list of port names
     * @return
     **************************************************************************/
    static native boolean listPorts( List<String> ports );

    /***************************************************************************
     * @param handle
     * @param buffer
     * @param offset
     * @param length
     * @return
     **************************************************************************/
    static native int read( int handle, byte [] buffer, int offset,
        int length );

    /***************************************************************************
     * @param handle
     * @param buffer
     * @param offset
     * @param length
     * @return
     **************************************************************************/
    static native int write( int handle, byte [] buffer, int offset,
        int length );
}
