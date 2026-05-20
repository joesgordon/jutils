package jutils.telemetry.ch10.io;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.utils.BitMasks;
import jutils.telemetry.ch10.ChecksumSize;
import jutils.telemetry.ch10.DataType;
import jutils.telemetry.ch10.DataTypeVersion;
import jutils.telemetry.ch10.PacketFlag;
import jutils.telemetry.ch10.PacketHeader;
import jutils.telemetry.ch10.SecondaryHeaderTimeFormat;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketHeaderSerializer implements IDataSerializer<PacketHeader>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public PacketHeader read( IDataStream stream )
        throws IOException, ValidationException
    {
        PacketHeader header = new PacketHeader();

        read( header, stream );

        return header;
    }

    /***************************************************************************
     * @param header
     * @param stream
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    public void read( PacketHeader header, IDataStream stream )
        throws IOException, ValidationException
    {
        long pos = stream.getPosition();

        header.sync = stream.readShort();

        if( header.sync != PacketHeader.SYNC_PATTERN )
        {
            String err = String.format(
                "Invalid packet sync found at position %d; expected %04X, read %04X",
                pos, PacketHeader.SYNC_PATTERN, header.sync );

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
        header.checksumPresent = ChecksumSize.fromValue( field );

        field = PacketFlag.SEC_HDR_TIME_FMT.getField( flags );
        header.secHdrTimeFmt = SecondaryHeaderTimeFormat.fromValue( field );

        header.dataOverflow = PacketFlag.DATA_OVERFLOW.getFlag( flags );
        header.rtcSyncError = PacketFlag.RTC_SYNC_ERROR.getFlag( flags );
        header.iptsTimeSource = PacketFlag.IPTS_TIME_SOURCE.getFlag( flags );
        header.secHdrPresent = PacketFlag.SEC_HDR_PRESENT.getFlag( flags );

        header.dataType = DataType.fromValue( stream.read() );

        int rtcLslw = stream.readInt();
        int rtcMsw = stream.readShort();
        short csRead = stream.readShort();

        header.relativeTimeCounter = ( ( rtcMsw &
            BitMasks.SHORT_MASK ) << 32L ) | ( rtcLslw & BitMasks.INT_MASK );
        header.checksum = csRead;

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

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( PacketHeader data, IDataStream stream )
        throws IOException
    {
        // TODO Auto-generated method stub
    }
}
