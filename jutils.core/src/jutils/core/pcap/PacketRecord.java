package jutils.core.pcap;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.ethernet.EthernetPacket;
import jutils.core.ethernet.IPacketData;
import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketRecord implements ITierPrinter
{
    /**  */
    public final PacketHeader header;
    /**  */
    public IPacketData packet;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketRecord()
    {
        this.header = new PacketHeader();
        this.packet = new EthernetPacket();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printTier( "Header", header );
        printer.printTier( "Packet", packet );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class PacketRecordSerializer
        implements IDataSerializer<PacketRecord>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public PacketRecord read( IDataStream stream )
            throws IOException, ValidationException
        {
            PacketRecord record = new PacketRecord();

            read( record, stream );

            return record;
        }

        /**
         * @param record
         * @param stream
         * @throws IOException
         * @throws ValidationException
         */
        public void read( PacketRecord record, IDataStream stream )
            throws IOException, ValidationException
        {
            // TODO Auto-generated method stub
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( PacketRecord record, IDataStream stream )
            throws IOException
        {
            // TODO Auto-generated method stub
        }
    }
}
