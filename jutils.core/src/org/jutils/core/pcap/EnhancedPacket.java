package org.jutils.core.pcap;

import java.io.EOFException;
import java.io.IOException;

import org.jutils.core.io.IDataStream;
import org.jutils.core.pcap.EthernetHeader.EthernetHeaderSerializer;
import org.jutils.core.pcap.Ipv4Header.Ipv4HeaderSerializer;
import org.jutils.core.pcap.TcpHeader.TcpHeaderSerializer;
import org.jutils.core.utils.ByteOrdering;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EnhancedPacket extends IBlock
{
    /**  */
    public int interfaceId;
    /**  */
    public long timestamp;
    /**  */
    public int capturedLength;
    /**  */
    public int originalLength;

    /**  */
    public int dataOffset;

    /**  */
    public EthernetHeader ethernet;
    /**  */
    public Ipv4Header ipv4;
    /**  */
    public TcpHeader tcp;

    /**  */
    public byte [] data;
    /**  */
    public byte [] options;

    /***************************************************************************
     * 
     **************************************************************************/
    public EnhancedPacket()
    {
        super( BlockType.ENHANCED_PACKET );

        this.ethernet = null;
        this.ipv4 = null;
        this.tcp = null;

        this.data = new byte[0];
        this.options = new byte[0];
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class EnhancedPacketSerializer
        implements IBlockBodySerializer
    {
        /**  */
        public static final int PROTO_OFFSET = 0x17;
        /**  */
        public static final int TCP_HDR_OFFSET = 0x22;

        /**  */
        private final EthernetHeaderSerializer ethernetSerializer;
        /**  */
        private final Ipv4HeaderSerializer ipv4Serializer;
        /**  */
        private final TcpHeaderSerializer tcpSerializer;

        /**
         * 
         */
        public EnhancedPacketSerializer()
        {
            this.ethernetSerializer = new EthernetHeaderSerializer();
            this.ipv4Serializer = new Ipv4HeaderSerializer();
            this.tcpSerializer = new TcpHeaderSerializer();
        }

        /**
         * {@inheritDoc}
         * @throws IOException
         * @throws EOFException
         */
        @Override
        public IBlock read( IDataStream stream, int id, int length )
            throws IOException
        {
            EnhancedPacket block = new EnhancedPacket();

            block.length = length;

            block.interfaceId = stream.readInt();
            block.timestamp = stream.readLong();
            block.capturedLength = stream.readInt();
            block.originalLength = stream.readInt();

            ByteOrdering order = stream.getOrder();

            stream.setOrder( ByteOrdering.BIG_ENDIAN );

            long packetStart = stream.getPosition();

            block.ethernet = ethernetSerializer.read( stream );

            EtherType etype = block.ethernet.getEtherType();

            if( etype == EtherType.IPV4 )
            {
                block.ipv4 = ipv4Serializer.read( stream );

                if( block.ipv4.protocol == Ipv4Header.TCP_PROTOCOL )
                {
                    block.tcp = tcpSerializer.read( stream );
                }
            }

            long dataStart = stream.getPosition();

            int packetLen = ( ( block.capturedLength + 3 ) / 4 ) * 4;
            int paddingLen = packetLen - block.capturedLength;

            int dataLen = packetLen - ( int )( dataStart - packetStart ) -
                paddingLen;
            int optionsLen = block.length - 8 * 4 - packetLen;

            block.data = new byte[dataLen];
            block.options = new byte[optionsLen];

            stream.readFully( block.data );
            stream.skip( paddingLen );
            stream.readFully( block.options );

            stream.setOrder( order );

            return block;
        }
    }
}
