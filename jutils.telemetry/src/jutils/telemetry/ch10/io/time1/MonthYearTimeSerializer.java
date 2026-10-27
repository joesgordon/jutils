package jutils.telemetry.ch10.io.time1;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.ui.hex.HexUtils;
import jutils.telemetry.ch10.MonthYearTime;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MonthYearTimeSerializer implements IDataSerializer<MonthYearTime>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public MonthYearTime read( IDataStream stream )
        throws IOException, ValidationException
    {
        MonthYearTime time = new MonthYearTime();
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

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( MonthYearTime data, IDataStream stream )
        throws IOException
    {
        // TODO Auto-generated method stub
    }
}
