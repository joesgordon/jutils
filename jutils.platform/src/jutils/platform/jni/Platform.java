package jutils.platform.jni;

import java.util.ArrayList;
import java.util.List;

import jutils.platform.IPlatform;
import jutils.platform.ISerialPort;

/*******************************************************************************
 * 
 ******************************************************************************/
class Platform implements IPlatform
{
    /**  */
    private final JniPlatform jplatform;

    /***************************************************************************
     * @param jplatform
     **************************************************************************/
    Platform( JniPlatform jplatform )
    {
        this.jplatform = jplatform;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean initialize()
    {
        return jplatform.initialize();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean destroy()
    {
        return jplatform.destroy();
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
        // TODO Auto-generated method stub
        return null;
    }
}
