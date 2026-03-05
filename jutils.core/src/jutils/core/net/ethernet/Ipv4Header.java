package jutils.core.net.ethernet;

import java.io.IOException;

import jutils.core.io.FieldPrinter;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.net.IpAddress;
import jutils.core.net.IpVersion;
import jutils.core.net.NetUtils;
import jutils.core.ui.hex.HexUtils;

/*******************************************************************************
 * <a href="https://en.wikipedia.org/wiki/Internet_Protocol_version_4">Wiki</a>
 * <a href="https://www.rfc-editor.org/rfc/rfc760">RFC-760</a>
 ******************************************************************************/
public class Ipv4Header implements ITcpIpLayer
{
    /** Format of the Internet Header */
    public IpVersion version;
    /**
     * Internet Header Length is the number of 32 bit words in this header
     * (minimum 5).
     */
    public byte ihl;
    /**  */
    public byte servicesWord;
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
    /**  */
    public byte [] options;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ipv4Header()
    {
        this.version = IpVersion.IPV4;
        this.ihl = 0;
        this.servicesWord = 0;
        this.totalLength = 0;
        this.id = 0;
        this.flagsFragOff = 0;
        this.ttl = 0;
        this.protocol = 0;
        this.checksum = 0;
        this.source = new byte[NetUtils.IPV4_SIZE];
        this.destination = new byte[NetUtils.IPV4_SIZE];
        this.options = new byte[0];
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Protocol getProtocol()
    {
        return Protocol.fromValue( protocol & 0xFF );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Version", version.name );
        printer.printField( "IHL", ihl );
        printer.printHexField( "DSCP/ECN", servicesWord );
        printer.printField( "Total Length", totalLength );
        printer.printField( "Identification", id );
        printer.printField( "Flags/Frag Offset", flagsFragOff );
        printer.printField( "TTL", ttl );
        printer.printField( "Protocol", getProtocol().getDescription() );
        printer.printHexField( "Header Checksum", checksum );
        printer.printField( "Source Address",
            new IpAddress( source ).toString() );
        printer.printField( "Destination Address",
            new IpAddress( destination ).toString() );
        printer.printField( "Options", HexUtils.toHexString( options, " " ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "IPv4";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Class<? extends ITcpIpLayer> getNextLayerType()
    {
        Protocol p = getProtocol();

        return getNextLayerFromProtocol( p );
    }

    /***************************************************************************
     * @param p
     * @return
     **************************************************************************/
    public static Class<? extends ITcpIpLayer> getNextLayerFromProtocol(
        Protocol p )
    {
        switch( p )
        {
            case HOPOPT:
                break;

            case ICMP:
                break;

            case TCP:
                return TcpHeader.class;

            case UDP:
                return UdpHeader.class;

            case UNKNOWN:
                break;

            default:
                break;
        }

        return null;
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

            byte verIhl = stream.read();
            byte version = ( byte )( ( verIhl >>> 4 ) & 0x0F );
            byte ihl = ( byte )( verIhl & 0x0F );

            header.version = IpVersion.fromId( version );
            header.ihl = ihl;
            header.servicesWord = stream.read();
            header.totalLength = stream.readShort();
            header.id = stream.readShort();
            header.flagsFragOff = stream.readShort();
            header.ttl = stream.read();
            header.protocol = stream.read();
            header.checksum = stream.readShort();

            stream.readFully( header.source );
            stream.readFully( header.destination );

            if( ihl > 5 )
            {
                header.options = new byte[4 * ( ihl - 5 )];
            }

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
