package org.jutils.time;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class TimeUtils
{
    /**  */
    public static final long MINUTES_IN_DAY = 60 * 24;
    /**  */
    public static final long SECONDS_IN_DAY = 60 * MINUTES_IN_DAY;

    /**  */
    public static final long MILLIS_IN_MIN = 1000 * 60;
    /**  */
    public static final long MILLIS_IN_HOUR = 60 * MILLIS_IN_MIN;
    /**  */
    public static final long MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR;
    /**  */
    public static final long MILLIS_IN_WEEK = 7 * MILLIS_IN_DAY;

    /**  */
    public static final long MICROS_IN_DAY = 1000 * MILLIS_IN_DAY;

    /**  */
    public static final long MAX_DAYS_IN_YEAR = 366;
    /**  */
    public static final long MAX_HOURS_IN_YEAR = MAX_DAYS_IN_YEAR * 24;
    /**  */
    public static final long MAX_MIN_IN_YEAR = MAX_HOURS_IN_YEAR * 60;
    /**  */
    public static final long MAX_SECONDS_IN_YEAR = MAX_MIN_IN_YEAR * 60;
    /**  */
    public static final long MAX_MILLIS_IN_YEAR = MAX_SECONDS_IN_YEAR * 1000;
    /**  */
    public static final long MAX_MICROS_IN_YEAR = MAX_MILLIS_IN_YEAR * 1000;
    /**  */
    /**  */
    public static final TimeZone UTC = TimeZone.getTimeZone( "UTC" );

    /***************************************************************************
     * Private constructor to prevent instantiation.
     **************************************************************************/
    private TimeUtils()
    {
    }

    /***************************************************************************
     * Returns the current year as of invocation.
     * @return the current year.
     **************************************************************************/
    public static int getCurrentYear()
    {
        return LocalDate.now( ZoneOffset.UTC ).getYear();
    }

    /***************************************************************************
     * Returns a string representing the provided microseconds in
     * {@code D HH:mm:ss.uuuuuu} format where D = days, HH = hours into day
     * (00-23), mm = minutes into hour (00-59), ss = seconds into minute
     * (00-59), uuuuuu = microseconds (000000-999999).
     * @param microseconds the duration in microseconds.
     * @return the string representing the provided duration.
     **************************************************************************/
    public static String microsDurationToString( long microseconds )
    {
        int micros = ( int )( Math.abs( microseconds ) % 1000 );
        long millis = microseconds / 1000;

        return durationToString( millis, micros );
    }

    /***************************************************************************
     * Creates a string with days hours, minutes, seconds and fractional seconds
     * (e.g. 2 03:44:66.254) from the provided duration in milliseconds.
     * @param milliseconds the duration to be converted to a string.
     * @return the string representing the provided duration.
     **************************************************************************/
    public static String durationToString( long milliseconds )
    {
        boolean negative = milliseconds < 0;
        long t = Math.abs( milliseconds );

        int millis = ( int )( t % 1000 );
        t = t / 1000; // seconds

        int sec = ( int )( t % 60 );
        t = t / 60; // min

        int min = ( int )( t % 60 );
        t = t / 60; // hours

        int hours = ( int )( t % 24 );
        t = t / 24; // days

        int days = ( int )( t );

        String str = negative ? "-" : "";

        if( days > 0 )
        {
            str += String.format( "%03d %02d:%02d:%02d.%03d", days, hours, min,
                sec, millis );
        }
        else if( hours > 0 )
        {
            str += String.format( "%02d:%02d:%02d.%03d", hours, min, sec,
                millis );
        }
        else if( min > 0 )
        {
            str += String.format( "%02d:%02d.%03d", min, sec, millis );
        }
        else if( sec > 0 )
        {
            str += String.format( "%02d.%03d", sec, millis );
        }
        else if( millis > 0 )
        {
            str += String.format( "0.%03d", millis );
        }
        else if( millis == 0 )
        {
            str += "0.000";
        }

        return str;
    }

    /***************************************************************************
     * Creates a string with days hours, minutes, seconds and fractional seconds
     * (e.g. 2 03:44:66.254987) from the provided duration in milliseconds.
     * @param milliseconds the duration to be converted to a string.
     * @param micros the number of microseconds into the provided number of
     * milliseconds (range 0-999).
     * @return the string representing the provided duration.
     **************************************************************************/
    public static String durationToString( long milliseconds, int micros )
    {
        return durationToString( milliseconds ) +
            String.format( "%03d", Math.abs( micros ) );
    }

    /***************************************************************************
     * Returns the number of milliseconds from the beginning of the current year
     * to midnight of the current day.
     * @return the number of milliseconds into the year of the current day.
     **************************************************************************/
    public static long getMillisIntoYear()
    {
        return getMillisIntoYear( LocalDate.now( ZoneOffset.UTC ) );
    }

    /***************************************************************************
     * Returns the number of milliseconds from the beginning of the provided
     * year to midnight of the provided month and day of month.
     * @param month 1-relative month.
     * @param day 1-relative day of the month.
     * @param year the four digit year.
     * @return milliseconds since the beginning of the provided year
     **************************************************************************/
    public static long getMillisIntoYear( int month, int day, int year )
    {
        LocalDate date = LocalDate.of( year, month, day );

        return getMillisIntoYear( date );
    }

    /***************************************************************************
     * Returns the number of milliseconds from the beginning of the year to
     * midnight of the provided date
     * @param date the date used to calculate milliseconds into the year.
     * @return the number of milliseconds from the beginning of the year to
     * midnight of the provided date.
     **************************************************************************/
    private static long getMillisIntoYear( LocalDate date )
    {
        int doy = date.getDayOfYear() - 1;
        long millis = doy * MILLIS_IN_DAY;

        return millis;
    }

    /***************************************************************************
     * Returns the number of milliseconds from the beginning of the current year
     * until now.
     * @return the number of milliseconds from the beginning of the current year
     * until now.
     **************************************************************************/
    public static long getMillisIntoYearNow()
    {
        LocalDateTime now = LocalDateTime.now( ZoneOffset.UTC );
        return getMillisIntoYear( now.toLocalDate() ) +
            getMillisIntoDay( now.toLocalTime() );
    }

    /***************************************************************************
     * @param millisFromEpoch the number of milliseconds from midnight January
     * 1, 1970.
     * @return the year calculated by the provided milliseconds from epoch.
     **************************************************************************/
    public static short getYearOfMillis( long millisFromEpoch )
    {
        long epochDay = millisFromEpoch / MILLIS_IN_DAY;
        LocalDate date = LocalDate.ofEpochDay( epochDay );

        return ( short )date.getYear();
    }

    /***************************************************************************
     * Returns the number of milliseconds from midnight until the provided time.
     * @param time a time of day.
     * @return the number of milliseconds since midnight.
     **************************************************************************/
    private static long getMillisIntoDay( LocalTime time )
    {
        return time.toSecondOfDay() * 1000 + time.getNano() / 1000000;
    }

    /***************************************************************************
     * Returns the date/time at midnight on Sunday of the provided date/time.
     * @param time a date/time.
     * @return midnight on Sunday of the provided date/time.
     **************************************************************************/
    public static LocalDateTime getBeginningOfWeek( LocalDateTime time )
    {
        int days = time.getDayOfWeek().getValue();

        days = days < 7 ? days : 0;

        LocalDate date = time.toLocalDate().minus( days, ChronoUnit.DAYS );

        return LocalDateTime.of( date, LocalTime.MIDNIGHT );
    }
}
