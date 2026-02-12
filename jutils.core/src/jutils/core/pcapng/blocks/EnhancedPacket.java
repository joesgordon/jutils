package jutils.core.pcapng.blocks;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.ethernet.EthernetPacket;
import jutils.core.ethernet.EthernetPacket.TcpIpPacketSerializer;
import jutils.core.io.ByteArrayStream;
import jutils.core.io.DataStream;
import jutils.core.io.FieldPrinter;
import jutils.core.io.IDataStream;
import jutils.core.pcapng.BlockType;
import jutils.core.pcapng.IBlock;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EnhancedPacket extends IBlock
{
    /**  */
    public int interfaceId;
    /**
     * The units of time from Linux epoch UTC. The length of a unit of time is
     * specified by the 'if_tsresol' option of the {@link InterfaceDescription}
     * block.
     */
    public long timestamp;
    /**
     * The number of octets captured from the packet (i.e. the length of the
     * Packet Data field)
     */
    public int capturedLength;
    /**
     * The actual length of the packet when it was transmitted on the network.
     */
    public int originalLength;

    /**  */
    public byte [] data;

    /***************************************************************************
     * 
     **************************************************************************/
    public EnhancedPacket()
    {
        super( BlockType.ENHANCED_PACKET );

        this.data = new byte[0];
    }

    /***************************************************************************
     * @return
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    public EthernetPacket readData()
    {
        try( ByteArrayStream byteStream = new ByteArrayStream( data,
            data.length, 0, false );
             IDataStream stream = new DataStream( byteStream ) )
        {
            TcpIpPacketSerializer serializer = new TcpIpPacketSerializer();

            return serializer.read( stream );
        }
        catch( IOException | ValidationException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Interface ID", interfaceId );
        printer.printField( "Timestamp", timestamp );
        printer.printField( "Captured Length", capturedLength );
        printer.printField( "Original Length", originalLength );
        printer.printTier( "Packet", readData() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class EnhancedPacketSerializer
        implements IBlockBodySerializer<EnhancedPacket>
    {
        /**  */
        public static final int PROTO_OFFSET = 0x17;
        /**  */
        public static final int TCP_HDR_OFFSET = 0x22;

        /**
         * {@inheritDoc}
         */
        @Override
        public EnhancedPacket read( IDataStream stream, BlockType type,
            int length ) throws IOException
        {
            EnhancedPacket block = new EnhancedPacket();

            block.length = length;

            block.interfaceId = stream.readInt();
            block.timestamp = readTimestamp( stream );
            block.capturedLength = stream.readInt();
            block.originalLength = stream.readInt();

            int packetLen = ( ( block.capturedLength + 3 ) / 4 ) * 4;
            int paddingLen = packetLen - block.capturedLength;
            int optionsLen = block.length - 8 * 4 - packetLen;

            block.data = new byte[block.capturedLength];

            stream.readFully( block.data );
            stream.skip( paddingLen );

            readOptions( stream, block, optionsLen );

            return block;
        }

        /**
         * @param stream
         * @return
         * @throws IOException
         */
        private static long readTimestamp( IDataStream stream )
            throws IOException
        {
            long tsHigh = stream.readInt() & 0xFFFFFFFFL;
            long tsLow = stream.readInt() & 0xFFFFFFFFL;

            return tsHigh << 32 | tsLow;
        }
    }
}
