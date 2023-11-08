package jutils.platform.jni;

import java.io.IOException;

import jutils.core.io.LogUtils;
import jutils.core.ui.hex.HexUtils;
import jutils.platform.ISerialPort;
import jutils.platform.data.SerialParams;

/*******************************************************************************
 * 
 ******************************************************************************/
class SerialPort implements ISerialPort
{
    /**  */
    private final JniSerialPort api;

    /***************************************************************************
     * 
     **************************************************************************/
    SerialPort()
    {
        this.api = new JniSerialPort();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean open( String name )
    {
        boolean result = api.open( name );

        LogUtils.printDebug( "Attempted to open %s: %s", name, result );

        return result;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        if( isOpen() )
        {
            api.close();
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isOpen()
    {
        return api.isOpen();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public SerialParams getConfig()
    {
        if( isOpen() )
        {
            JniSerialParams config = new JniSerialParams();

            if( api.getConfig( config ) )
            {
                return config.getParams();
            }
        }

        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setConfig( SerialParams config )
    {
        if( isOpen() )
        {
            JniSerialParams params = new JniSerialParams( config );

            api.setConfig( params );
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
            count = api.read( buffer, offset, len );

            if( count > 0 )
            {
                LogUtils.printDebug( "SerialPort.read() - Read %d bytes: %s",
                    count, HexUtils.toHexString( buffer, " ", offset, count ) );
            }
        }

        return count;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( byte [] buffer, int offset, int len ) throws IOException
    {
        int count = -1;

        // LogUtils.printDebug( "Writing bytes to serial port" );

        if( isOpen() )
        {
            count = api.write( buffer, offset, len );

            LogUtils.printDebug( "Wrote %d bytes", count );

            if( count != len )
            {
                throw new IOException( "Unable to write " + len +
                    " bytes; wrote " + count + " bytes." );
            }
        }
    }
}
