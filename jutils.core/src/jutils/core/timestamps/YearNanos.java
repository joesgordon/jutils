package jutils.core.timestamps;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jutils.core.INamedItem;
import jutils.core.ValidationException;
import jutils.core.io.FieldPrinter;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.time.TimeUtils;

/*******************************************************************************
 * Defines an instant in time with the year and nanoseconds into the year. The
 * range of years are between {@link Short#MIN_VALUE} and
 * {@link Short#MAX_VALUE}.
 ******************************************************************************/
public class YearNanos implements ITimestamp
{
    /** The year of the instant. */
    public short year;
    /**
     * The number of nanoseconds into year. A long will hold up to attoseconds
     * (10<sup>-18</sup>) for 366 days.
     */
    public long nanos;

    /***************************************************************************
     * Creates a new instant set to midnight January 1 1970.
     **************************************************************************/
    public YearNanos()
    {
        this.year = 1970;
        this.nanos = 0L;
    }

    /***************************************************************************
     * Creates a new instant set to the provided year and nanoseconds into the
     * year.
     * @param year the year of the instant.
     * @param nanos the nanoseconds into the year of the instant.
     **************************************************************************/
    public YearNanos( short year, long nanos )
    {
        this.year = year;
        this.nanos = nanos;
    }

    /***************************************************************************
     * Creates a new instant with the provided time.
     * @param time the instant to be copied.
     **************************************************************************/
    public YearNanos( YearNanos time )
    {
        this.year = time.year;
        this.nanos = time.nanos;
    }

    /***************************************************************************
     * Creates a new instant with the provided date/time.
     * @param time the date/time used to set this instant.
     **************************************************************************/
    public YearNanos( LocalDateTime time )
    {
        fromDateTime( time );
    }

    @Override
    public void printFields( FieldPrinter printer )
    {
        // TODO Auto-generated method stub

    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDateTime toDateTime()
    {
        int doy = ( int )( nanos / TimeUtils.NANOS_PER_DAY );
        long nod = nanos - ( doy * TimeUtils.NANOS_PER_DAY );

        doy++;

        try
        {
            LocalDate date = LocalDate.ofYearDay( year, doy );
            LocalTime time = LocalTime.ofNanoOfDay( nod );

            return LocalDateTime.of( date, time );
        }
        catch( DateTimeException ex )
        {
            String msg = String.format(
                "Unable to convert year:nanos (doy:nod) into date/time: %d : %d ( %d : %d )",
                year, nanos, doy, nod );
            throw new DateTimeException( msg, ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void fromDateTime( LocalDateTime time )
    {
        this.year = ( short )time.getYear();
        int doy = time.getDayOfYear() - 1;
        this.nanos = doy * TimeUtils.NANOS_PER_DAY +
            time.toLocalTime().toNanoOfDay();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setNow()
    {
        fromDateTime( TimeUtils.getUtcNow() );
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
        setTime( NanoTimeUnit.MICROSECONDS, micros, retain );
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
        setTime( NanoTimeUnit.MILLISECONDS, millis, retain );
    }

    /***************************************************************************
     * Sets the number of nanoseconds into the year with the provided number of
     * units of time without retaining the current number of nanoseconds into
     * the unit of time.
     * @param unit the units of time being set.
     * @param time the number of units of time into the year.
     **************************************************************************/
    public void setTime( NanoTimeUnit unit, long time )
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
    public void setTime( NanoTimeUnit unit, long time, boolean retainFraction )
    {
        long fraction = retainFraction ? nanos % unit.nanosPerUnit : 0L;

        nanos = time * unit.nanosPerUnit + fraction;
    }

    /***************************************************************************
     * Creates a new instant that represents the current system time.
     * @return the instant that represents the current system time.
     **************************************************************************/
    public static YearNanos now()
    {
        YearNanos time = new YearNanos();

        time.setNow();

        return time;
    }

    /***************************************************************************
     * Creates a new instant that represents the current UTC time.
     * @return the instant that represents the current UTC time.
     **************************************************************************/
    public static YearNanos nowUtc()
    {
        YearNanos time = new YearNanos();

        time.setNow();

        return time;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        long secs = nanos / TimeUtils.NANOS_IN_SEC;
        long frac = nanos - ( secs * TimeUtils.NANOS_IN_SEC );

        return String.format( "%04d %d.%09d", year, secs, frac );
    }

    /***************************************************************************
     * Defines the units of time supported for setting the number of nanoseconds
     * into the year.
     **************************************************************************/
    public static enum NanoTimeUnit implements INamedItem
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
        private NanoTimeUnit( String name, long nanosPerUnit )
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

    /***************************************************************************
     * 
     **************************************************************************/
    public class YearNanosSerializer implements IDataSerializer<YearNanos>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public YearNanos read( IDataStream stream )
            throws IOException, ValidationException
        {
            YearNanos time = new YearNanos();

            time.year = stream.readShort();
            time.nanos = stream.readLong();

            return time;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( YearNanos time, IDataStream stream )
            throws IOException
        {
            stream.writeShort( time.year );
            stream.writeLong( time.nanos );
        }
    }
}
