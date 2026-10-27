package jutils.telemetry.ch10;

import java.util.Objects;

import jutils.core.io.FieldPrinter;
import jutils.core.time.TimeUtils;
import jutils.core.timestamps.YearNanos;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IrigDayTime implements ITime1
{
    /** Day of the year. A value of 1 is 1 January. */
    public int days;
    /**  */
    public int hours;
    /**  */
    public int minutes;
    /**  */
    public int seconds;
    /**  */
    public int milliseconds;

    /***************************************************************************
     * 
     **************************************************************************/
    public IrigDayTime()
    {
        this.days = 0;
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.milliseconds = 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Days", days );
        printer.printField( "Hours", hours );
        printer.printField( "Minute", minutes );
        printer.printField( "Seconds", seconds );
        printer.printField( "Milliseconds", milliseconds );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setTime( YearNanos time )
    {
        long nanos = time.nanos;
        int days;
        int hours;
        int minutes;
        int seconds;
        int milliseconds;

        days = ( int )( nanos / TimeUtils.NANOS_IN_DAY );
        nanos = nanos - ( days * TimeUtils.NANOS_IN_DAY );

        hours = ( int )( nanos / TimeUtils.NANOS_IN_HOUR );
        nanos = nanos - ( hours * TimeUtils.NANOS_IN_HOUR );

        minutes = ( int )( nanos / TimeUtils.NANOS_IN_MIN );
        nanos = nanos - ( minutes * TimeUtils.NANOS_IN_MIN );

        seconds = ( int )( nanos / TimeUtils.NANOS_IN_SEC );
        nanos = nanos - ( seconds * TimeUtils.NANOS_IN_SEC );

        milliseconds = ( int )( nanos / TimeUtils.NANOS_IN_MILLI );

        this.days = days + 1;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public YearNanos getTime()
    {
        YearNanos time = new YearNanos();

        time.nanos = 0;
        time.nanos += milliseconds * TimeUtils.NANOS_IN_MILLI;
        time.nanos += seconds * TimeUtils.NANOS_IN_SEC;
        time.nanos += minutes * TimeUtils.NANOS_IN_MIN;
        time.nanos += hours * TimeUtils.NANOS_IN_HOUR;
        time.nanos += ( days - 1 ) * TimeUtils.NANOS_IN_DAY;

        return time;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        if( this == obj )
        {
            return true;
        }
        else if( obj instanceof IrigDayTime )
        {
            IrigDayTime that = ( IrigDayTime )obj;
            if( this.days != that.days )
            {
                return false;
            }
            else if( this.hours != that.hours )
            {
                return false;
            }
            else if( this.minutes != that.minutes )
            {
                return false;
            }
            else if( this.seconds != that.seconds )
            {
                return false;
            }
            else if( this.milliseconds != that.milliseconds )
            {
                return false;
            }

            return true;
        }

        return false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return Objects.hash( days, hours, minutes, seconds, milliseconds );
    }
}
