package org.jutils.core.pcap;

import java.io.IOException;

import org.jutils.core.io.IDataSerializer;
import org.jutils.core.io.IDataStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ipv4Header
{
    /**  */
    public static final byte TCP_PROTOCOL = 6;
    /**  */
    public static final byte UDP_PROTOCOL = 17;

    /**  */
    public byte verLenWord;
    /**  */
    public byte services;
    /**  */
    public short totalLength;
    /**  */
    public short id;
    /**  */
    public short flagsFragOff;
    /**  */
    public byte ttl;
    /**  */
    public byte protocol;
    /**  */
    public short checksum;
    /**  */
    public final byte [] source;
    /**  */
    public final byte [] destination;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ipv4Header()
    {
        this.verLenWord = 0;
        this.services = 0;
        this.totalLength = 0;
        this.id = 0;
        this.flagsFragOff = 0;
        this.ttl = 0;
        this.protocol = 0;
        this.checksum = 0;
        this.source = new byte[4];
        this.destination = new byte[4];
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Protocol getProtocol()
    {
        return Protocol.fromId( protocol & 0xFF );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class Ipv4HeaderSerializer
        implements IDataSerializer<Ipv4Header>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public Ipv4Header read( IDataStream stream ) throws IOException
        {
            Ipv4Header header = new Ipv4Header();

            header.verLenWord = stream.read();
            header.services = stream.read();
            header.totalLength = stream.readShort();
            header.id = stream.readShort();
            header.flagsFragOff = stream.readShort();
            header.ttl = stream.read();
            header.protocol = stream.read();
            header.checksum = stream.readShort();

            stream.readFully( header.source );
            stream.readFully( header.destination );

            return header;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( Ipv4Header data, IDataStream stream )
            throws IOException
        {
            // TODO Auto-generated method stub
        }
    }
}
