package jutils.telemetry.io.ch10;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.io.KeyedSerializer;
import jutils.core.ui.hex.HexUtils;
import jutils.telemetry.data.ch10.DateFormat;
import jutils.telemetry.data.ch10.ITime1;
import jutils.telemetry.data.ch10.IrigDayTime;
import jutils.telemetry.data.ch10.IrigTimeSource;
import jutils.telemetry.data.ch10.MonthDayTime;
import jutils.telemetry.data.ch10.PacketHeader;
import jutils.telemetry.data.ch10.Time1Body;
import jutils.telemetry.data.ch10.Time1Word;
import jutils.telemetry.data.ch10.TimeSource;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Time1BodySerializer implements IBodySerializer<Time1Body>
{
    /**  */
    private KeyedSerializer<DateFormat, ITime1> time1Serializers;

    /***************************************************************************
     * 
     **************************************************************************/
    public Time1BodySerializer()
    {
        this.time1Serializers = new KeyedSerializer<>();

        time1Serializers.put( DateFormat.IRIG_DAY,
            new IrigDateTime1Serializer() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Time1Body read( IDataStream stream, PacketHeader header )
        throws IOException, ValidationException
    {
        Time1Body body = new Time1Body();

        int word = stream.readInt();
        int field;

        field = Time1Word.SOURCE.getField( word );
        body.source = TimeSource.fromValue( field );

        field = Time1Word.FORMAT.getField( word );
        body.format = DateFormat.fromValue( field );

        field = Time1Word.ITS.getField( word );
        body.irigSource = IrigTimeSource.fromValue( field );

        field = Time1Word.RESERVED.getField( word );
        body.reserved = field;

        body.time = time1Serializers.read( stream, body.format );

        return body;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( Time1Body body, IDataStream stream, PacketHeader header )
        throws IOException
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class IrigDateTime1Serializer
        implements IDataSerializer<IrigDayTime>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public IrigDayTime read( IDataStream stream )
            throws IOException, ValidationException
        {
            IrigDayTime time = new IrigDayTime();
            byte [] buf = new byte[6];
            int t;

            stream.readFully( buf );

            t = HexUtils.fromBcd( buf[0] );
            time.milliseconds = 10 * t;

            t = HexUtils.fromBcd( buf[1] );
            time.seconds = t;

            t = HexUtils.fromBcd( buf[2] );
            time.minutes = t;

            t = HexUtils.fromBcd( buf[3] );
            time.hours = t;

            t = HexUtils.fromBcd( buf[4] );
            time.days = t;

            t = HexUtils.fromBcd( buf[0] );
            time.days += t * 100;

            return time;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( IrigDayTime data, IDataStream stream )
            throws IOException
        {
            // TODO Auto-generated method stub
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class MonthDayTime1Serializer
        implements IDataSerializer<MonthDayTime>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public MonthDayTime read( IDataStream stream )
            throws IOException, ValidationException
        {
            MonthDayTime time = new MonthDayTime();
            byte [] buf = new byte[8];
            int t;

            stream.readFully( buf );

            t = HexUtils.fromBcd( buf[0] );
            time.milliseconds = 10 * t;

            t = HexUtils.fromBcd( buf[1] );
            time.seconds = t;

            t = HexUtils.fromBcd( buf[2] );
            time.minutes = t;

            t = HexUtils.fromBcd( buf[3] );
            time.hours = t;

            t = HexUtils.fromBcd( buf[4] );
            time.dayofMonth = t;

            t = HexUtils.fromBcd( buf[5] );
            time.month = t;

            t = HexUtils.fromBcd( buf[6] );
            time.year = t;

            t = HexUtils.fromBcd( buf[7] );
            time.year += t * 100;

            return time;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( MonthDayTime data, IDataStream stream )
            throws IOException
        {
            // TODO Auto-generated method stub
        }
    }
}
