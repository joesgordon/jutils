package jutils.telemetry.ch10;

import java.time.LocalDate;
import java.util.Objects;

import jutils.core.io.FieldPrinter;
import jutils.core.time.TimeUtils;
import jutils.core.timestamps.YearNanos;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MonthYearTime implements ITime1
{
    /**  */
    public int year;
    /**  */
    public int month;
    /**  */
    public int dayofMonth;
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
    public MonthYearTime()
    {
        this.year = 0;
        this.month = 0;
        this.dayofMonth = 0;
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
        printer.printField( "Year", year );
        printer.printField( "Month", month );
        printer.printField( "Day of Month", dayofMonth );
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

        int dayOfYear = days + 1;
        LocalDate date = TimeUtils.fromDayOfYear( time.year, dayOfYear );

        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.dayofMonth = date.getDayOfMonth();
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

        int dayOfYear = TimeUtils.toDayOfYear( year, month, dayofMonth );

        time.year = ( short )year;

        time.nanos = 0;
        time.nanos += milliseconds * TimeUtils.NANOS_IN_MILLI;
        time.nanos += seconds * TimeUtils.NANOS_IN_SEC;
        time.nanos += minutes * TimeUtils.NANOS_IN_MIN;
        time.nanos += hours * TimeUtils.NANOS_IN_HOUR;
        time.nanos += ( dayOfYear - 1 ) * TimeUtils.NANOS_IN_DAY;

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
        else if( obj instanceof MonthYearTime )
        {
            MonthYearTime that = ( MonthYearTime )obj;
            if( this.year != that.year )
            {
                return false;
            }
            else if( this.month != that.month )
            {
                return false;
            }
            else if( this.dayofMonth != that.dayofMonth )
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
        return Objects.hash( year, month, dayofMonth, hours, minutes, seconds,
            milliseconds );
    }
}
