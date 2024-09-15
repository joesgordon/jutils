package jutils.core.pcap.ethernet;

import java.io.IOException;

import jutils.core.io.FieldPrinter;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.ui.hex.HexUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MacHeader implements ITcpIpLayer
{
    /**  */
    public final byte [] destination;
    /**  */
    public final byte [] source;
    /** Optional */
    public int tag802q;
    /**  */
    public short type;

    /***************************************************************************
     * 
     **************************************************************************/
    public MacHeader()
    {
        this.destination = new byte[6];
        this.source = new byte[6];
        this.type = 0;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public EtherType getEtherType()
    {
        return EtherType.fromId( type & 0xFFFF );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Destination MAC",
            HexUtils.toHexString( destination, " " ) );
        printer.printField( "Source MAC", HexUtils.toHexString( source, " " ) );

        if( tag802q != 0 )
        {
            printer.printHexField( "802.1Q Header", tag802q );
        }

        printer.printField( "Ether Type", getEtherType().getDescription() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "MAC";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Class<? extends ITcpIpLayer> getNextLayerType()
    {
        if( ( type & 0xFFFF ) < EtherType.MIN_TYPE )
        {
            return Ipv4Header.class;
        }

        EtherType etherType = getEtherType();

        switch( etherType )
        {
            case IPV4:
                return Ipv4Header.class;

            case ARP:
                break;

            case IPV6:
                return Ipv6Header.class;

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
    public static final class MacHeaderSerializer
        implements IDataSerializer<MacHeader>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public MacHeader read( IDataStream stream ) throws IOException
        {
            MacHeader header = new MacHeader();

            stream.readFully( header.destination );
            stream.readFully( header.source );

            short sval = stream.readShort();

            if( ( sval & 0xFFFF ) == 0x8100 )
            {
                header.tag802q = sval << 16;
                header.tag802q |= stream.readShort() & 0xFFFF;
                header.type = stream.readShort();
            }
            else
            {
                header.tag802q = 0;
                header.type = sval;
            }

            return header;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( MacHeader data, IDataStream stream )
            throws IOException
        {
            // TODO Auto-generated method stub
        }
    }
}
