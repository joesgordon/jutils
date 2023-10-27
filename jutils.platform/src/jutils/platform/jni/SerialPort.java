package jutils.platform.jni;

import java.nio.ByteBuffer;

import jutils.platform.ISerialPort;
import jutils.platform.SerialConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
class SerialPort implements ISerialPort
{
    /**  */
    private final JniSerialPort api;
    /**  */
    private ByteBuffer readBuffer;
    /**  */
    private ByteBuffer writeBuffer;

    /***************************************************************************
     * 
     **************************************************************************/
    SerialPort()
    {
        this.api = new JniSerialPort();
        this.readBuffer = null;
        this.writeBuffer = null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean open( String name )
    {
        boolean result = api.open( name );

        if( result )
        {
            this.readBuffer = JniUtils.getBuffers().nextBuffer();
            this.writeBuffer = JniUtils.getBuffers().nextBuffer();
        }

        return result;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean close()
    {
        boolean result = true;

        if( isOpen() )
        {
            result = api.close();
        }

        if( result )
        {
            JniUtils.getBuffers().releaseBuffer( this.readBuffer );
            JniUtils.getBuffers().releaseBuffer( this.writeBuffer );

            this.readBuffer = null;
            this.writeBuffer = null;
        }

        return result;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isOpen()
    {
        return readBuffer != null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public SerialConfig getConfig()
    {
        if( isOpen() )
        {
            SerialConfig config = new SerialConfig();
            if( api.getConfig( config ) )
            {
                return config;
            }
        }

        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setConfig( SerialConfig config )
    {
        if( isOpen() )
        {
            api.setConfig( config );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setReadTimeout( int millis )
    {
        if( isOpen() )
        {
            api.setReadTimeout( millis );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int read( byte [] buffer, int offset, int len )
    {
        int count = -1;

        if( isOpen() )
        {
            readBuffer.position( 0 );
            count = api.read( len );

            if( count > 0 )
            {
                readBuffer.position( 0 );
                readBuffer.get( buffer, offset, count );
            }
        }

        return count;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int write( byte [] buffer, int offset, int len )
    {
        int count = -1;

        if( isOpen() )
        {
            writeBuffer.position( 0 );
            writeBuffer.put( buffer, offset, len );

            writeBuffer.position( 0 );
            count = api.write( len );
        }

        return count;
    }
}
