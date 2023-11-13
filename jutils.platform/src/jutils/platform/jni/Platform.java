package jutils.platform.jni;

import java.util.ArrayList;
import java.util.List;

import jutils.core.io.LogUtils;
import jutils.platform.IPlatform;
import jutils.platform.ISerialPort;

/*******************************************************************************
 * 
 ******************************************************************************/
class Platform implements IPlatform
{
    /**  */
    private final JniPlatform jplatform;

    /**  */
    private boolean initialized;

    /***************************************************************************
     * @param jplatform
     **************************************************************************/
    Platform( JniPlatform jplatform )
    {
        this.jplatform = jplatform;
        this.initialized = false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean initialize()
    {
        this.initialized = jplatform.initialize();

        return initialized;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public boolean isInialized()
    {
        return initialized;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean destroy()
    {
        LogUtils.printDebug( "Calling destroy" );
        boolean result = jplatform.destroy();
        LogUtils.printDebug( "Called destroy: %s", result );

        return result;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<String> listSerialPorts()
    {
        List<String> ports = new ArrayList<String>();

        jplatform.listPorts( ports );

        return ports;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public ISerialPort createSerialPort()
    {
        SerialPort port = new SerialPort();

        return port;
    }
}
