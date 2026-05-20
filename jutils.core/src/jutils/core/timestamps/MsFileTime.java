package jutils.core.timestamps;

import java.time.LocalDateTime;

import jutils.core.io.FieldPrinter;
import jutils.core.time.TimeUtils;

/*******************************************************************************
 * Defines a representation of the <a
 * href="https://learn.microsoft.com/en-us/windows/win32/api/minwinbase/ns-minwinbase-filetime">
 * FILETIME</a> structure.
 ******************************************************************************/
public class MsFileTime implements ITimestamp
{
    /** The FILETIME that corresponds to 00:00:00.000000000 January 1 1970. */
    public static final long UNIX_EPOCH = 116444736000000000L;

    /**  */
    public static final long NANOS_PER_TICK = 100;
    /**  */
    public static final long TICKS_PER_MICRO = 10;
    /**  */
    public static final long TICKS_PER_MILLI = TICKS_PER_MICRO *
        TimeUtils.MICROS_IN_MILLI;
    /**  */
    public static final long TICKS_PER_SEC = TICKS_PER_MILLI *
        TimeUtils.MILLIS_IN_SEC;

    /**
     * Number of 100 nanosecond intervals since 00:00:00.000000000 January 1,
     * 1601 Coordinated Universal Time (UTC)
     */
    public long time;

    /***************************************************************************
     * 
     **************************************************************************/
    public MsFileTime()
    {
        this.time = UNIX_EPOCH;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Ticks", time );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDateTime toDateTime()
    {
        long unixTicks = time - UNIX_EPOCH;
        long ticksIntoMilli = time % TICKS_PER_MILLI;
        long nanos = ticksIntoMilli * NANOS_PER_TICK;
        long unixMillis = unixTicks / TICKS_PER_MILLI;

        LocalDateTime time = TimeUtils.fromUnixEpoch( unixMillis );
        time.plusNanos( nanos );

        return time;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void fromDateTime( LocalDateTime time )
    {
        long unixMillis = TimeUtils.toUnixEpoch( time );
        long nanosIntoMilli = time.getNano() % TimeUtils.NANOS_IN_MILLI;
        long ticksIntoMilli = nanosIntoMilli / NANOS_PER_TICK;
        long unixTicks = unixMillis * TICKS_PER_MILLI + ticksIntoMilli;

        this.time = UNIX_EPOCH + unixTicks;
    }
}
