package jutils.telemetry.ch10.io.time1;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataStream;
import jutils.core.io.KeyedSerializer;
import jutils.telemetry.ch10.DateFormat;
import jutils.telemetry.ch10.ITime1;
import jutils.telemetry.ch10.IrigTimeSource;
import jutils.telemetry.ch10.PacketHeader;
import jutils.telemetry.ch10.Time1Body;
import jutils.telemetry.ch10.Time1Word;
import jutils.telemetry.ch10.TimeSource;
import jutils.telemetry.ch10.io.IBodySerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Time1BodySerializer implements IBodySerializer<Time1Body>
{
    /**  */
    private final KeyedSerializer<DateFormat, ITime1> time1Serializers;

    /***************************************************************************
     * 
     **************************************************************************/
    public Time1BodySerializer()
    {
        this.time1Serializers = new KeyedSerializer<>();

        time1Serializers.put( DateFormat.IRIG_DAY,
            new IrigDayTimeSerializer() );
        time1Serializers.put( DateFormat.MONTH_YEAR,
            new MonthYearTimeSerializer() );
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
        body.source.setId( field, TimeSource.fromValue( field ) );

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
}
