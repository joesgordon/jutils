package jutils.platform;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jutils.platform.data.SerialParams;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UnimplementedPlatform implements IPlatform
{
    /**  */
    private boolean initialized;

    /***************************************************************************
     * 
     **************************************************************************/
    public UnimplementedPlatform()
    {
        this.initialized = false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean initialize()
    {
        return false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
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
        return true;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<String> listSerialPorts()
    {
        return Arrays.asList( new String[0] );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public ISerialPort createSerialPort()
    {
        return new UnimplementedSerialPort();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class UnimplementedSerialPort implements ISerialPort
    {
        /**  */
        private final SerialParams params;

        /**
         * 
         */
        public UnimplementedSerialPort()
        {
            this.params = new SerialParams();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int read( byte [] buf, int off, int len ) throws IOException
        {
            throw new IOException( "Unable to read from unoped port" );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( byte [] buf, int off, int len ) throws IOException
        {
            throw new IOException( "Unable to write to unoped port" );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws Exception
        {
            throw new IOException( "Unable to close unoped port" );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean open( String name )
        {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isOpen()
        {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SerialParams getConfig()
        {
            return new SerialParams( params );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setConfig( SerialParams config )
        {
            params.set( config );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setReadTimeout( int millis )
        {
        }
    }
}
