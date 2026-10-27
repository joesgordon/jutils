package jutils.telemetry.ch10.io.time1;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.ui.hex.HexUtils;
import jutils.telemetry.ch10.IrigDayTime;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IrigDayTimeSerializer implements IDataSerializer<IrigDayTime>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IrigDayTime read( IDataStream stream )
        throws IOException, ValidationException
    {
        IrigDayTime time = new IrigDayTime();

        short word1 = stream.readShort();
        short word2 = stream.readShort();
        short word3 = stream.readShort();

        int t;

        // ---------------------------------------------------------------------
        // Word 1 fields
        // ---------------------------------------------------------------------

        t = HexUtils.fromBcd( IrigDayWord1Fields.MILLIS_100S.getByte( word1 ) );
        time.milliseconds += 100 * t;

        t = HexUtils.fromBcd( IrigDayWord1Fields.MILLIS_10S.getByte( word1 ) );
        time.milliseconds += 10 * t;

        t = HexUtils.fromBcd( IrigDayWord1Fields.SECONDS_10S.getByte( word1 ) );
        time.seconds += 10 * t;

        t = HexUtils.fromBcd( IrigDayWord1Fields.SECONDS.getByte( word1 ) );
        time.seconds += t;

        // ---------------------------------------------------------------------
        // Word 2 fields
        // ---------------------------------------------------------------------

        t = HexUtils.fromBcd( IrigDayWord2Fields.MINUTES_10S.getByte( word2 ) );
        time.minutes += 10 * t;

        t = HexUtils.fromBcd( IrigDayWord2Fields.MINUTES.getByte( word2 ) );
        time.minutes += t;

        t = HexUtils.fromBcd( IrigDayWord2Fields.HOURS_10S.getByte( word2 ) );
        time.hours += 10 * t;

        t = HexUtils.fromBcd( IrigDayWord2Fields.HOURS.getByte( word2 ) );
        time.hours += t;

        // ---------------------------------------------------------------------
        // Word 3 fields
        // ---------------------------------------------------------------------

        t = HexUtils.fromBcd( IrigDayWord3Fields.DAYS_100S.getByte( word3 ) );
        time.days += 100 * t;

        t = HexUtils.fromBcd( IrigDayWord3Fields.DAYS_10S.getByte( word3 ) );
        time.days += 10 * t;

        t = HexUtils.fromBcd( IrigDayWord3Fields.DAYS.getByte( word3 ) );
        time.days += t;

        return time;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( IrigDayTime time, IDataStream stream ) throws IOException
    {
        short word1 = 0;
        short word2 = 0;
        short word3 = 0;

        byte t;

        // ---------------------------------------------------------------------
        // Word 1 fields
        // ---------------------------------------------------------------------

        t = ( byte )( ( ( time.milliseconds % 100 ) + 5 ) / 10 );
        word1 = IrigDayWord1Fields.MILLIS_10S.setByte( word1, t );

        t = ( byte )( time.milliseconds / 100 );
        word1 = IrigDayWord1Fields.MILLIS_100S.setByte( word1, t );

        t = ( byte )( time.seconds % 10 );
        word1 = IrigDayWord1Fields.SECONDS.setByte( word1, t );

        t = ( byte )( time.seconds / 10 );
        word1 = IrigDayWord1Fields.SECONDS_10S.setByte( word1, t );

        // ---------------------------------------------------------------------
        // Word 2 fields
        // ---------------------------------------------------------------------

        t = ( byte )( time.minutes % 10 );
        word2 = IrigDayWord2Fields.MINUTES.setByte( word2, t );

        t = ( byte )( time.minutes / 10 );
        word2 = IrigDayWord2Fields.MINUTES_10S.setByte( word2, t );

        t = ( byte )( time.hours % 10 );
        word2 = IrigDayWord2Fields.HOURS.setByte( word2, t );

        t = ( byte )( time.hours / 10 );
        word2 = IrigDayWord2Fields.HOURS_10S.setByte( word2, t );

        // ---------------------------------------------------------------------
        // Word 3 fields
        // ---------------------------------------------------------------------

        t = ( byte )( time.days % 10 );
        word3 = IrigDayWord3Fields.DAYS.setByte( word3, t );

        t = ( byte )( ( time.days / 10 ) % 10 );
        word3 = IrigDayWord3Fields.DAYS_10S.setByte( word3, t );

        t = ( byte )( time.days / 100 );
        word3 = IrigDayWord3Fields.DAYS_100S.setByte( word3, t );

        stream.writeShort( word1 );
        stream.writeShort( word2 );
        stream.writeShort( word3 );
    }
}
