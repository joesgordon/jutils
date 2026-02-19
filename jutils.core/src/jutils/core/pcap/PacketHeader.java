package jutils.core.pcap;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.timestamps.UnixTime;
import jutils.core.utils.BitMasks;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketHeader implements ITierPrinter
{
    /** The time the data was captured. */
    public final UnixTime timestamp;
    /** The length of the packet data to follow. */
    public long capturedLength;
    /** The length of the packet when transmitted. */
    public long originalLength;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketHeader()
    {
        this.timestamp = new UnixTime();
        this.capturedLength = 0L;
        this.originalLength = 0L;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printTier( "Timestamp", timestamp );
        printer.printField( "Captured Length", capturedLength );
        printer.printField( "Original Length", originalLength );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class PacketHeaderSerializer
        implements IDataSerializer<PacketHeader>
    {
        /**  */
        private boolean isMicros;

        /**
         * 
         */
        public PacketHeaderSerializer()
        {
            this.isMicros = true;
        }

        /**
         * @param isMicros
         */
        public void setMicroseconds( boolean isMicros )
        {
            this.isMicros = isMicros;
        }

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
            header.timestamp.seconds = stream.readInt() & BitMasks.INT_MASK;
            header.timestamp.nanoseconds = stream.readInt();
            header.capturedLength = stream.readInt() & BitMasks.INT_MASK;
            header.originalLength = stream.readInt() & BitMasks.INT_MASK;

            if( isMicros )
            {
                header.timestamp.nanoseconds *= 1000;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( PacketHeader header, IDataStream stream )
            throws IOException
        {
            int frac = header.timestamp.nanoseconds;

            frac = isMicros ? ( header.timestamp.nanoseconds + 500 ) / 1000
                : frac;

            stream.writeInt( ( int )header.timestamp.seconds );
            stream.writeInt( frac );
            stream.writeInt( ( int )header.capturedLength );
            stream.writeInt( ( int )header.originalLength );
        }
    }
}
