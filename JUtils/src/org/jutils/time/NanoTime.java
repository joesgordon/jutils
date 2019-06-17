package org.jutils.time;

import java.time.*;

import org.jutils.INamedItem;

/*******************************************************************************
 * Defines an instant in time with the year and nanoseconds into the year. The
 * range of years are between {@link Short#MIN_VALUE} and
 * {@link Short#MAX_VALUE}.
 ******************************************************************************/
public class NanoTime
{
    /** The number of nanoseconds in a day. */
    public static final long NANOS_PER_DAY = 1000000000L * 60 * 60 * 24;

    /** The year of the instant. */
    public short year;
    /**
     * The number of nanoseconds into year. A long will hold up to attoseconds
     * (10<sup>-18</sup>) for 366 days.
     */
    public long nanoseconds;

    /***************************************************************************
     * Creates a new instant set to midnight January 1 1970.
     **************************************************************************/
    public NanoTime()
    {
        this.year = 1970;
        this.nanoseconds = 0L;
    }

    /***************************************************************************
     * Creates a new instant set to the provided year and nanoseconds into the
     * year.
     * @param year the year of the instant.
     * @param nanos the nanoseconds into the year of the instant.
     **************************************************************************/
    public NanoTime( short year, long nanos )
    {
        this.year = year;
        this.nanoseconds = nanos;
    }

    /***************************************************************************
     * Creates a new instant with the provided time.
     * @param time the instant to be copied.
     **************************************************************************/
    public NanoTime( NanoTime time )
    {
        this.year = time.year;
        this.nanoseconds = time.nanoseconds;
    }

    /***************************************************************************
     * Creates a new instant with the provided date/time.
     * @param time the date/time used to set this instant.
     **************************************************************************/
    public NanoTime( LocalDateTime time )
    {
        setDateTime( time );
    }

    /***************************************************************************
     * Converts this instant to a date/time.
     * @return the date/time of this instant.
     **************************************************************************/
    public LocalDateTime toDateTime()
    {
        int doy = ( int )( nanoseconds / NANOS_PER_DAY );
        long nod = nanoseconds - ( doy * NANOS_PER_DAY );

        doy++;

        LocalDate date = LocalDate.ofYearDay( year, doy );
        LocalTime time = LocalTime.ofNanoOfDay( nod );

        return LocalDateTime.of( date, time );
    }

    /***************************************************************************
     * Sets this instant to the provided date/time.
     * @param time the date/time to set this instant to.
     **************************************************************************/
    public void setDateTime( LocalDateTime time )
    {
        this.year = ( short )time.getYear();
        int doy = time.getDayOfYear() - 1;
        this.nanoseconds = doy * NANOS_PER_DAY +
            time.toLocalTime().toNanoOfDay();
    }

    /***************************************************************************
     * Sets this instant to now (system time).
     **************************************************************************/
    public void setNow()
    {
        setDateTime( LocalDateTime.now() );
    }

    /***************************************************************************
     * Sets this instant to now (system time).
     **************************************************************************/
    public void setNowUtc()
    {
        setDateTime( LocalDateTime.now( ZoneOffset.UTC ) );
    }

    /***************************************************************************
     * Sets the number of nanoseconds into the year with the provided number of
     * microseconds without retaining the current number of nanoseconds into the
     * microsecond.
     * @param micros the number of microseconds into the year.
     **************************************************************************/
    public void setMicros( long micros )
    {
        setMicros( micros, false );
    }

    /***************************************************************************
     * Sets the number of nanoseconds into the year with the provided number of
     * microseconds and retains the current number of nanoseconds into the
     * microsecond as specified.
     * @param micros the number of microseconds into the year.
     * @param retain retains the current number of nanoseconds into the
     * microsecond if {@code true}; discards otherwise.
     **************************************************************************/
    public void setMicros( long micros, boolean retain )
    {
        setTime( TimeUnit.MICROSECONDS, micros, retain );
    }

    /***************************************************************************
     * Sets the number of nanoseconds into the year with the provided number of
     * milliseconds without retaining the current number of nanoseconds into the
     * millisecond.
     * @param millis the number of milliseconds into the year.
     **************************************************************************/
    public void setMillis( long millis )
    {
        setMicros( millis, false );
    }

    /***************************************************************************
     * Sets the number of nanoseconds into the year with the provided number of
     * milliseconds and retains the current number of nanoseconds into the
     * millisecond as specified.
     * @param millis the number of milliseconds into the year.
     * @param retain retains the current number of nanoseconds into the
     * millisecond if {@code true}; discards otherwise.
     **************************************************************************/
    public void setMillis( long millis, boolean retain )
    {
        setTime( TimeUnit.MILLISECONDS, millis, retain );
    }

    /***************************************************************************
     * Sets the number of nanoseconds into the year with the provided number of
     * units of time without retaining the current number of nanoseconds into
     * the unit of time.
     * @param unit the units of time being set.
     * @param time the number of units of time into the year.
     **************************************************************************/
    public void setTime( TimeUnit unit, long time )
    {
        setTime( unit, time, false );
    }

    /***************************************************************************
     * Sets the number of nanoseconds into the year with the provided number of
     * units of time and retains the current number of nanoseconds into the unit
     * of time as specified.
     * @param unit the units of time being set.
     * @param time the number of units of time into the year.
     * @param retainFraction retains the current number of nanoseconds into the
     * unit of time if {@code true}; discards otherwise.
     **************************************************************************/
    public void setTime( TimeUnit unit, long time, boolean retainFraction )
    {
        long fraction = retainFraction ? nanoseconds % unit.nanosPerUnit : 0L;

        nanoseconds = time * unit.nanosPerUnit + fraction;
    }

    /***************************************************************************
     * Creates a new instant that represents the current system time.
     * @return the instant that represents the current system time.
     **************************************************************************/
    public static NanoTime now()
    {
        NanoTime time = new NanoTime();

        time.setNow();

        return time;
    }

    /***************************************************************************
     * Creates a new instant that represents the current UTC time.
     * @return the instant that represents the current UTC time.
     **************************************************************************/
    public static NanoTime nowUtc()
    {
        NanoTime time = new NanoTime();

        time.setNowUtc();

        return time;
    }

    /***************************************************************************
     * Defines the units of time supported for setting the number of nanoseconds
     * into the year.
     **************************************************************************/
    public static enum TimeUnit implements INamedItem
    {
        /** Represents nanoseconds. */
        NANOSECONDS( "Nanoseconds", 1L ),
        /** Represents microseconds. */
        MICROSECONDS( "Microseconds", 1000L ),
        /** Represents milliseconds. */
        MILLISECONDS( "Milliseconds", 1000000L ),
        /** Represents seconds. */
        SECONDS( "Seconds", 1000000000L );

        /** The name of the unit. */
        public final String name;
        /** The number of nanoseconds in the unit. */
        public final long nanosPerUnit;

        /**
         * Creates a new unit.
         * @param name the name of the unit.
         * @param nanosPerUnit the number of nanoseconds in the unit.
         */
        private TimeUnit( String name, long nanosPerUnit )
        {
            this.name = name;
            this.nanosPerUnit = nanosPerUnit;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
