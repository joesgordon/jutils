package jutils.platform.jni;

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
        // TODO Auto-generated method stub
        return false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<String> listSerialPorts()
    {
        // TODO Auto-generated method stub
        return null;
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
