package jutils.telemetry.ch10;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;

/*******************************************************************************
 * The length of the packet header is fixed at 24 bytes (192 bits). The packet
 * header is mandatory and shall consist of ten fields.
 ******************************************************************************/
public class PacketHeader implements ITierPrinter
{
    /** The packet synchronization pattern, {@code 0xEB25}. */
    public static final short SYNC_PATTERN = ( short )0xEB25;
    /**  */
    public static final int STD_PKT_SIZE_MAX = 524288;
    /**  */
    public static final int COMPGEN_F1_PKT_SIZE_MAX = 134217728;
    /**  */
    public static final int SIZE = 24;

    /**  */
    public short sync;
    /** The packet channel ID. Channel ID 0x0000 is reserved. */
    public short channelId;
    /**
     * The length of the entire packet (in bytes) including the header(s), body,
     * and checksum; always a multiple of four.
     */
    public int packetLength;
    /**
     * The valid length of the data (in bytes) within the packet. This length
     * includes channel-specific data, IPDHs, intra-packet time stamp(s) (IPTS),
     * and data but does not include packet trailer filler and data checksum.
     */
    public int dataLength;
    /**
     * Value at or below the release version of the standard applied to the data
     * types in {@link DataType#version}.
     */
    public DataTypeVersion dataVersion;
    /**
     * Represents the packet sequence number for each channel ID. This is simply
     * a counter that increments by n + 0x01 to 0xFF for every packet
     * transferred from a particular channel and is not required to start at
     * 0x00 for the first occurrence of a packet for the channel ID.
     */
    public byte sequenceNumber;

    /**  */
    public ChecksumPresence checksumPresent;
    /**  */
    public SecHdrTimeFormat secHdrTimeFmt;
    /**  */
    public boolean dataOverflow;
    /** {@code true} indicates RTC sync error. */
    public boolean rtcSyncError;
    /**
     * The intra-packet timestamp time source: {@code true} indicates packet
     * secondary header time; {@code false} indicates packet header 48-bit RTC;
     */
    public boolean iptsTimeSource;
    /** Secondary header present when {@code true}. */
    public boolean secHdrPresent;

    /** Represents the type of data found in this packet. */
    public DataType dataType;
    /**
     * The 10-MHz RTC. This is a free-running 10-MHz binary counter represented
     * by 48 bits that are common to all data channels. The counter shall be
     * derived from a 10-MHz internal crystal oscillator and shall remain
     * free-running during each session (e.g., recording).
     */
    public long relativeTimeCounter;
    /**
     * The 16-bit arithmetic sum of all 16-bit words in the header excluding the
     * header checksum word.
     */
    public short checksum;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketHeader()
    {
        this.sync = SYNC_PATTERN;
        this.channelId = 0;
        this.packetLength = 0;
        this.dataLength = 0;
        this.dataVersion = DataTypeVersion.RESERVED;
        this.sequenceNumber = 0;
        this.checksumPresent = ChecksumPresence.NO_CHECKSUM;
        this.secHdrTimeFmt = SecHdrTimeFormat.RESERVED;
        this.dataOverflow = false;
        this.rtcSyncError = false;
        this.iptsTimeSource = false;
        this.secHdrPresent = false;
        this.dataType = DataType.COMPUTER_GENERATED_7;
        this.relativeTimeCounter = 0L;
        this.checksum = 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printHexField( "Sync", sync );
        printer.printField( "Channel ID", channelId );
        printer.printField( "Packet Length", packetLength );
        printer.printField( "Data Length", dataLength );
        printer.printField( "Data Version", dataVersion.getDescription() );
        printer.printField( "Sequence #", sequenceNumber );
        printer.printField( "Checksum Present",
            checksumPresent.getDescription() );
        printer.printField( "Secondary Header Time Format",
            secHdrTimeFmt.getDescription() );
        printer.printField( "Data Length", dataLength );
        printer.printField( "Data Overflow", dataOverflow );
        printer.printField( "RTC Sync Error", rtcSyncError );
        printer.printField( "IPTS Time Source", iptsTimeSource );
        printer.printField( "Secondary Header Present", secHdrPresent );
        printer.printField( "Data Type", dataType.getDescription() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class PacketHeaderSerializer
        implements IDataSerializer<PacketHeader>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public PacketHeader read( IDataStream stream )
            throws IOException, ValidationException
        {
            PacketHeader header = new PacketHeader();

            read( header, stream );

            return header;
        }

        /**
         * @param header
         * @param stream
         * @throws IOException
         * @throws ValidationException
         */
        public void read( PacketHeader header, IDataStream stream )
            throws IOException, ValidationException
        {
            long pos = stream.getPosition();

            header.sync = stream.readShort();

            if( header.sync != SYNC_PATTERN )
            {
                String err = String.format(
                    "Invalid packet sync found at position %d; expected %04X, read %04X",
                    pos, SYNC_PATTERN, header.sync );

                throw new ValidationException( err );
            }

            byte flags;
            byte field;

            header.channelId = stream.readShort();
            header.packetLength = stream.readInt();
            header.dataLength = stream.readInt();
            header.dataVersion = DataTypeVersion.fromValue( stream.read() );
            header.sequenceNumber = stream.read();

            flags = stream.read();

            field = PacketFlag.CHECKSUM_PRESENT.getField( flags );
            header.checksumPresent = ChecksumPresence.fromValue( field );

            field = PacketFlag.SEC_HDR_TIME_FMT.getField( flags );
            header.secHdrTimeFmt = SecHdrTimeFormat.fromValue( field );

            header.dataOverflow = PacketFlag.DATA_OVERFLOW.getFlag( flags );
            header.rtcSyncError = PacketFlag.RTC_SYNC_ERROR.getFlag( flags );
            header.iptsTimeSource = PacketFlag.IPTS_TIME_SOURCE.getFlag(
                flags );
            header.secHdrPresent = PacketFlag.SEC_HDR_PRESENT.getFlag( flags );

            header.dataType = DataType.fromValue( stream.read() );
            header.relativeTimeCounter = stream.readLong();
            header.checksum = ( short )( header.relativeTimeCounter >>> 48 );
            header.relativeTimeCounter &= 0xFFFFFFFFFFFFL;

            short csCalcd = 0;

            stream.seek( pos );

            for( int i = 0; i < 11; i++ )
            {
                csCalcd += stream.readShort();
                pos += 2;
            }
            stream.skip( 2 );

            if( csCalcd != header.checksum )
            {
                String err = String.format(
                    "Invalid checksum for packet header read at position %d; expected %04X, read %04X",
                    pos, csCalcd, header.checksum );

                throw new ValidationException( err );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( PacketHeader data, IDataStream stream )
            throws IOException
        {
            // TODO Auto-generated method stub

        }
    }
}
