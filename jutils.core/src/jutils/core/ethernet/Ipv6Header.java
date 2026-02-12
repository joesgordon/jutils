package jutils.core.ethernet;

import java.io.IOException;

import jutils.core.io.FieldPrinter;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.net.IpAddress;
import jutils.core.net.NetUtils;

/*******************************************************************************
 * <a href="https://en.wikipedia.org/wiki/IPv6_packet">Wiki</a>
 ******************************************************************************/
public class Ipv6Header implements ITcpIpLayer
{
    /**  */
    public byte version;
    /**  */
    public byte trafficClass;
    /**  */
    public int flowLabel;
    /**  */
    public short payloadLength;
    /**  */
    public byte nextHeader;
    /**  */
    public byte hopLimit;
    /**  */
    public final byte [] source;
    /**  */
    public final byte [] destination;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ipv6Header()
    {
        this.version = 0;
        this.trafficClass = 0;
        this.flowLabel = 0;
        this.payloadLength = 0;
        this.nextHeader = 0;
        this.hopLimit = 0;
        this.source = new byte[NetUtils.IPV6_SIZE];
        this.destination = new byte[NetUtils.IPV6_SIZE];
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Protocol getNextHeaderProtocol()
    {
        return Protocol.fromValue( nextHeader & 0xFF );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Version", version );
        printer.printHexField( "Traffic Class", trafficClass );
        printer.printField( "Payload Length", payloadLength );
        printer.printField( "Next Header",
            getNextHeaderProtocol().getDescription() );
        printer.printField( "Hop Limit", hopLimit );
        printer.printField( "Source", new IpAddress( source ).toString() );
        printer.printField( "Destination",
            new IpAddress( destination ).toString() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "IPv6";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Class<? extends ITcpIpLayer> getNextLayerType()
    {
        Protocol p = getNextHeaderProtocol();

        return Ipv4Header.getNextLayerFromProtocol( p );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class Ipv6HeaderSerializer
        implements IDataSerializer<Ipv6Header>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public Ipv6Header read( IDataStream stream ) throws IOException
        {
            Ipv6Header header = new Ipv6Header();

            int ival;

            ival = stream.readInt();

            header.version = ( byte )( ( ival >> 28 ) & 0x0F );
            header.trafficClass = ( byte )( ( ival >> 20 ) & 0xFF );
            header.flowLabel = ival & 0xFFFFF;

            header.payloadLength = stream.readShort();
            header.nextHeader = stream.read();
            header.hopLimit = stream.read();

            stream.readFully( header.source );
            stream.readFully( header.destination );

            return header;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( Ipv6Header data, IDataStream stream )
            throws IOException
        {
            // TODO Auto-generated method stub
        }
    }
}
