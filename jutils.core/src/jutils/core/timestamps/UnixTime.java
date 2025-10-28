package jutils.core.timestamps;

import java.time.LocalDateTime;

import jutils.core.ValidationException;
import jutils.core.io.FieldPrinter;
import jutils.core.io.IParser;
import jutils.core.time.TimeUtils;
import jutils.core.ui.fields.IDescriptor;

/*******************************************************************************
 * Defines a representation of <a
 * href="https://en.wikipedia.org/wiki/Unix_time"> Unix</a> as structure with
 * seconds (traditional fidelity) and nanoseconds (for greater fidelity). This
 * implementation of a time is time zone agnostic.
 ******************************************************************************/
public class UnixTime implements ITimestamp
{
    /** The posix time in seconds for 00:00:00 1 Jan 2010. */
    public long SEC_2010_01_01 = 1262304000L;

    /** The number of seconds since Unix epoch (00:00:00 1 Jan 1970). */
    public long seconds;
    /**
     * The number of nanoseconds into the current second. This value may not be
     * negative.
     */
    public int nanoseconds;

    /***************************************************************************
     * 
     **************************************************************************/
    public UnixTime()
    {
        this.seconds = 0;
        this.nanoseconds = 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Seconds", seconds );
        printer.printField( "Nanoseconds", nanoseconds );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDateTime toDateTime()
    {
        LocalDateTime time = TimeUtils.fromUnixEpoch( seconds * 1000 );
        time = time.plusNanos( nanoseconds );
        return time;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void fromDateTime( LocalDateTime time )
    {
        seconds = TimeUtils.toUnixEpoch( time ) / 1000;
        nanoseconds = time.getNano();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class UnixTimeParser implements IParser<UnixTime>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public UnixTime parse( String str ) throws ValidationException
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class UnixTimeDescriptor implements IDescriptor<UnixTime>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getDescription( UnixTime item )
        {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
