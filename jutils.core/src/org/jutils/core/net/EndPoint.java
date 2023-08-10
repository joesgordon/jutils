package org.jutils.core.net;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IDataSerializer;
import org.jutils.core.io.IDataStream;
import org.jutils.core.net.IpAddress.IpAddressSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EndPoint
{
    /**  */
    public final IpAddress address;
    /**  */
    public int port;

    /***************************************************************************
     * Creates and end-point initialized to 0.0.0.0 and port 0.
     **************************************************************************/
    public EndPoint()
    {
        this.address = new IpAddress();
        this.port = 0;
    }

    /***************************************************************************
     * Creates and end-point initialized to 0.0.0.0 and the provided port.
     * @param port the network port.
     **************************************************************************/
    public EndPoint( int port )
    {
        this();

        this.address.setOctets( 0, 0, 0, 0 );
        this.port = port;
    }

    /***************************************************************************
     * Creates an end point at the provided address and port.
     * @param address the IP4 address of the end point.
     * @param port the port of the end point.
     **************************************************************************/
    public EndPoint( IpAddress address, int port )
    {
        this();

        this.address.set( address );
        this.port = port;
    }

    /***************************************************************************
     * @param address
     * @param port
     **************************************************************************/
    public EndPoint( InetAddress address, int port )
    {
        this();

        this.address.setInetAddress( address );
        this.port = port;
    }

    /***************************************************************************
     * Sets this end point to the same values as the provided end point.
     * @param that the values to copy to this end point.
     **************************************************************************/
    public void set( EndPoint that )
    {
        this.set( that.address, that.port );
    }

    /***************************************************************************
     * @param address
     * @param port
     **************************************************************************/
    public void set( IpAddress address, int port )
    {
        this.address.set( address );
        this.port = port;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setAny()
    {
        setAny( 0 );
    }

    /***************************************************************************
     * @param port
     **************************************************************************/
    public void setAny( int port )
    {
        this.address.setAny();
        this.port = port;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public SocketAddress getInetSocketAddress()
    {
        return new InetSocketAddress( address.getInetAddress(), port );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        else if( obj == this )
        {
            return true;
        }
        else if( obj instanceof EndPoint )
        {
            EndPoint that = ( EndPoint )obj;
            return this.address.equals( that.address ) &&
                this.port == that.port;
        }

        return false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return 31 * address.hashCode() + port;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return String.format( "%s:%d", address, port );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class EndPointSerializer implements IDataSerializer<EndPoint>
    {
        /**  */
        private final IpAddressSerializer ipSerializer;

        /**
         * 
         */
        public EndPointSerializer()
        {
            this.ipSerializer = new IpAddressSerializer();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public EndPoint read( IDataStream stream )
            throws IOException, ValidationException
        {
            EndPoint point = new EndPoint();

            read( point, stream );

            return point;
        }

        /**
         * @param point
         * @param stream
         * @throws IOException
         * @throws EOFException
         */
        public void read( EndPoint point, IDataStream stream )
            throws IOException, ValidationException
        {
            ipSerializer.read( point.address, stream );
            point.port = stream.readInt();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( EndPoint point, IDataStream stream )
            throws IOException
        {
            ipSerializer.write( point.address, stream );
            stream.writeInt( point.port );
        }
    }
}
