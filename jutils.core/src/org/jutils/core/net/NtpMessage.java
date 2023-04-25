package org.jutils.core.net;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.Arrays;

import org.jutils.core.io.BitsReader;
import org.jutils.core.io.ByteArrayStream;
import org.jutils.core.io.DataStream;
import org.jutils.core.io.IDataSerializer;
import org.jutils.core.io.IDataStream;
import org.jutils.core.io.LogUtils;
import org.jutils.core.utils.ByteOrdering;

/*******************************************************************************
 *
 ******************************************************************************/
public class NtpMessage
{
    /**  */
    public NtpMode mode;
    /**  */
    public byte version;
    /**  */
    public LeapIndicator leap;
    /** This value indicates the stratum level of the local clock. */
    public int stratumLevel;

    /**
     * This value indicates the maximum interval between successive messages, in
     * seconds to the nearest power of two. The values that can appear in this
     * field presently range from 4 (16 s) to 14 (16284 s); however, most
     * applications use only the sub-range 6 (64 s) to 10 (1024 s).
     */
    public byte poll;

    /**
     * This value indicates the precision of the local clock, in seconds to the
     * nearest power of two. The values that normally appear in this field range
     * from -6 for mains-frequency clocks to -20 for microsecond clocks found in
     * some workstations.
     */
    public byte precision;

    /**
     * This value indicates the total roundtrip delay to the primary reference
     * source, in seconds. Note that this variable can take on both positive and
     * negative values, depending on the relative time and frequency offsets.
     * The values that normally appear in this field range from negative values
     * of a few milliseconds to positive values of several hundred milliseconds.
     */
    public int rootDelay;

    /**
     * This value indicates the nominal error relative to the primary reference
     * source, in seconds. The values that normally appear in this field range
     * from 0 to several hundred milliseconds.
     */

    public int rootDispersion;

    /**
     * This is a 4-byte array identifying the particular reference source. In
     * the case of NTP Version 3 or Version 4 stratum-0 (unspecified) or
     * stratum-1 (primary) servers, this is a four-character ASCII string, left
     * justified and zero padded to 32 bits. In NTP Version 3 secondary servers,
     * this is the 32-bit IPv4 address of the reference source. In NTP Version 4
     * secondary servers, this is the low order 32 bits of the latest transmit
     * timestamp of the reference source. NTP primary (stratum 1) servers should
     * set this field to a code identifying the external reference source
     * according to the following list. If the external reference is one of
     * those listed, the associated code should be used. Codes for sources not
     * listed can be contrived as appropriate. Code External Reference Source
     * ---- ------------------------- LOCL uncalibrated local clock used as a
     * primary reference for a subnet without external means of synchronization
     * PPS atomic clock or other pulse-per-second source individually calibrated
     * to national standards ACTS NIST dialup modem service USNO USNO modem
     * service PTB PTB (Germany) modem service TDF Allouis (France) Radio 164
     * kHz DCF Mainflingen (Germany) Radio 77.5 kHz MSF Rugby (UK) Radio 60 kHz
     * WWV Ft. Collins (US) Radio 2.5, 5, 10, 15, 20 MHz WWVB Boulder (US) Radio
     * 60 kHz WWVH Kaui Hawaii (US) Radio 2.5, 5, 10, 15 MHz CHU Ottawa (Canada)
     * Radio 3330, 7335, 14670 kHz LORC LORAN-C radionavigation system OMEG
     * OMEGA radionavigation system GPS Global Positioning Service GOES
     * Geostationary Orbit Environment Satellite
     */
    public int refID;

    /**  */
    public final NtpTimestamp reference;

    /**  */
    public final NtpTimestamp origin;

    /**  */
    public final NtpTimestamp receive;

    /**  */
    public final NtpTimestamp transmit;

    /***************************************************************************
     * 
     **************************************************************************/
    public NtpMessage()
    {
        this.mode = NtpMode.CLIENT;
        this.version = 3;
        this.leap = LeapIndicator.NO_WARINGS;
        this.stratumLevel = 0;
        this.poll = 0;
        this.precision = 0;
        this.rootDelay = 0;
        this.rootDispersion = 0;
        this.refID = 0;
        this.reference = new NtpTimestamp();
        this.origin = new NtpTimestamp();
        this.receive = new NtpTimestamp();
        this.transmit = new NtpTimestamp();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return String.format(
            "%s, %02X, %s, %02X, %02X, %02X, %d, %d, %d, %s, %s, %s, %s", mode,
            version, leap, stratumLevel, poll, precision, rootDelay,
            rootDispersion, refID, reference, origin, receive, transmit );
    }

    /***************************************************************************
     * @param ntpServer
     * @param local
     * @return
     * @throws IOException
     * @throws SocketException
     **************************************************************************/
    public static NtpMessage sendTo( IpAddress ntpServer, IpAddress local )
        throws SocketException, IOException
    {
        NtpMessage response = new NtpMessage();
        NtpMessage request = new NtpMessage();

        LogUtils.printInfo( "Request: %s", request );

        UdpInputs inputs = new UdpInputs();

        inputs.broadcast = false;
        inputs.localPort = 0;
        inputs.loopback = false;
        inputs.multicast.isUsed = false;
        inputs.nic = local.toString();
        inputs.remoteAddress.set( ntpServer );
        inputs.remotePort = 123;
        inputs.timeout = 5000;
        inputs.ttl = 1;

        try( UdpConnection socket = new UdpConnection( inputs ) )
        {
            try( ByteArrayStream bs = new ByteArrayStream( 1024 );
                 DataStream s = new DataStream( bs, ByteOrdering.BIG_ENDIAN ) )
            {
                NtpMsgSerializer nms = new NtpMsgSerializer();
                NetMessage netMsg;

                s.seek( 0L );
                nms.write( request, s );

                int len = ( int )s.getPosition();

                netMsg = socket.sendMessage(
                    Arrays.copyOf( bs.getBuffer(), len ) );

                LogUtils.printInfo( "Request Msg: %s", netMsg );

                netMsg = socket.receiveMessage();

                if( netMsg != null )
                {
                    LogUtils.printInfo( "Response Msg: %s", netMsg );
                    s.seek( 0L );
                    bs.write( netMsg.contents );

                    s.seek( 0L );
                    nms.read( response, s );

                    LogUtils.printInfo( "Response: %s", response );
                }
                else
                {
                    LogUtils.printWarning( "No Response from %s",
                        inputs.remoteAddress );
                }
            }
        }

        return response;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum NtpMode
    {
        /**  */
        RESERVED( 0 ),
        /**  */
        SYMMETRIC_ACTIVE( 1 ),
        /**  */
        SYMMETRIC_PASSIVE( 2 ),
        /**  */
        CLIENT( 3 ),
        /**  */
        SERVER( 4 ),
        /**  */
        BROADCAST( 5 ),
        /**  */
        NTP_CTRL( 6 ),
        /**  */
        PRIVATE( 7 );

        /**  */
        public final int value;

        /**
         * @param value
         */
        private NtpMode( int value )
        {
            this.value = value;
        }

        /**
         * @param value
         * @return
         */
        public static NtpMode fromValue( byte value )
        {
            for( NtpMode m : values() )
            {
                if( m.value == value )
                {
                    return m;
                }
            }

            return null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum LeapIndicator
    {
        /**  */
        NO_WARINGS( 0 ),
        /**  */
        LAST_MIN_61S( 1 ),
        /**  */
        LAST_MIN_59S( 2 ),
        /** Clock not synchronized. */
        ALARM( 3 );

        /**  */
        public final int value;

        /**
         * @param value
         */
        private LeapIndicator( int value )
        {
            this.value = value;
        }

        /**
         * @param value
         * @return
         */
        public static LeapIndicator fromValue( byte value )
        {
            for( LeapIndicator li : values() )
            {
                if( li.value == value )
                {
                    return li;
                }
            }

            return null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum Stratum
    {
        /**  */
        NO_WARINGS( 0 ),
        /**  */
        LAST_MIN_61S( 1 ),
        /**  */
        LAST_MIN_59S( 2 ),
        /** Clock not synchronized. */
        ALARM( 3 );

        /**  */
        public final int value;

        /**
         * @param value
         */
        private Stratum( int value )
        {
            this.value = value;
        }

        /**
         * @param value
         * @return
         */
        public static Stratum fromValue( byte value )
        {
            for( Stratum s : values() )
            {
                if( s.value == value )
                {
                    return s;
                }
            }

            return null;
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    public static final class NtpTimestamp
    {
        /** Seconds since Jan 1, 1970 (or maybe 1900?) */
        public int seconds;
        /** LSB = 232e-12 seconds */
        public int fraction;

        public NtpTimestamp()
        {
            this.seconds = 0;
            this.fraction = 0;
        }

        @Override
        public String toString()
        {
            return String.format( "%d[%d]", seconds, fraction );
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    public static final class NtpMsgSerializer
        implements IDataSerializer<NtpMessage>
    {
        /**  */
        private final BitsReader modeReader;
        /**  */
        private final BitsReader versionReader;
        /**  */
        private final BitsReader leapReader;

        /**
         * 
         */
        public NtpMsgSerializer()
        {
            this.modeReader = new BitsReader( 0, 2 );
            this.versionReader = new BitsReader( 3, 5 );
            this.leapReader = new BitsReader( 6, 7 );
        }

        /**
         * @param ts
         * @param stream
         * @throws IOException
         */
        private static void readTimestamp( NtpTimestamp ts, IDataStream stream )
            throws IOException
        {
            ts.seconds = stream.readInt();
            ts.fraction = stream.readInt();
        }

        /**
         * @param ts
         * @param stream
         * @throws IOException
         */
        private static void writeTimestamp( NtpTimestamp ts,
            IDataStream stream ) throws IOException
        {
            stream.writeInt( ts.seconds );
            stream.writeInt( ts.fraction );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public NtpMessage read( IDataStream stream ) throws IOException
        {
            NtpMessage msg = new NtpMessage();

            read( msg, stream );

            return msg;
        }

        /**
         * @param msg
         * @param stream
         * @throws EOFException
         * @throws IOException
         */
        private void read( NtpMessage msg, IDataStream stream )
            throws EOFException, IOException
        {
            // This member is a bit field with the following:
            // bits 0:2 - Mode
            // bits 3:5 - VN (Version Number)
            // bits 6:7 - LI (Leap Indicator)
            byte b = stream.read();

            msg.mode = NtpMode.fromValue( modeReader.read( b ) );
            msg.version = versionReader.read( b );
            msg.leap = LeapIndicator.fromValue( leapReader.read( b ) );

            msg.stratumLevel = stream.read() & 0xFF;
            msg.poll = stream.read();
            msg.precision = stream.read();
            msg.rootDelay = stream.readInt();
            msg.rootDispersion = stream.readInt();
            msg.refID = stream.readInt();

            readTimestamp( msg.reference, stream );
            readTimestamp( msg.origin, stream );
            readTimestamp( msg.receive, stream );
            readTimestamp( msg.transmit, stream );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( NtpMessage msg, IDataStream stream )
            throws IOException
        {
            byte b = 0;

            b = modeReader.write( ( byte )msg.mode.value, b );
            b = versionReader.write( msg.version, b );
            b = leapReader.write( ( byte )msg.leap.value, b );

            stream.write( b );

            stream.write( ( byte )msg.stratumLevel );
            stream.write( msg.poll );
            stream.write( msg.precision );
            stream.writeInt( msg.rootDelay );
            stream.writeInt( msg.rootDispersion );
            stream.writeInt( msg.refID );

            writeTimestamp( msg.reference, stream );
            writeTimestamp( msg.origin, stream );
            writeTimestamp( msg.receive, stream );
            writeTimestamp( msg.transmit, stream );
        }
    }
}
