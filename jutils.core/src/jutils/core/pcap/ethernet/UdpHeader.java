package jutils.core.pcap.ethernet;

import java.io.IOException;

import jutils.core.io.FieldPrinter;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpHeader implements ITcpIpLayer
{
    /**  */
    public int sourcePort;
    /**  */
    public int destinationPort;
    /**  */
    public int length;
    /**  */
    public short checksum;

    /***************************************************************************
     * 
     **************************************************************************/
    public UdpHeader()
    {
        this.sourcePort = 0;
        this.destinationPort = 0;
        this.length = 0;
        this.checksum = 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Source Port", sourcePort );
        printer.printField( "Destination Port", destinationPort );
        printer.printField( "Length", length );
        printer.printHexField( "Checksum", checksum );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "UDP";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Class<? extends ITcpIpLayer> getNextLayerType()
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class UdpHeaderSerializer
        implements IDataSerializer<UdpHeader>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public UdpHeader read( IDataStream stream ) throws IOException
        {
            UdpHeader header = new UdpHeader();

            header.sourcePort = stream.readShort() & 0xFFFF;
            header.destinationPort = stream.readShort() & 0xFFFF;
            header.length = stream.readShort() & 0xFFFF;
            header.checksum = stream.readShort();

            return header;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( UdpHeader data, IDataStream stream )
            throws IOException
        {
            // TODO Auto-generated method stub
        }
    }
}
