package jutils.core.timestamps;

import java.time.LocalDateTime;
import java.util.List;

import jutils.core.Utils;
import jutils.core.ValidationException;
import jutils.core.io.FieldPrinter;
import jutils.core.io.IParser;
import jutils.core.io.parsers.IntegerParser;
import jutils.core.io.parsers.LongParser;
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
        /**  */
        private final LongParser secondsParser;
        /**  */
        private final IntegerParser nanosParser;

        /**
         * 
         */
        public UnixTimeParser()
        {
            this.secondsParser = new LongParser();
            this.nanosParser = new IntegerParser( 0,
                ( int )TimeUtils.NANOS_IN_SEC );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public UnixTime parse( final String str ) throws ValidationException
        {
            UnixTime time = new UnixTime();

            String text = str.trim();

            List<String> fields = Utils.split( text, '.' );

            String secStr = "";
            String nanosStr = "";

            if( fields.size() == 2 )
            {
                secStr = fields.get( 0 );
                nanosStr = fields.get( 1 );

                nanosStr = String.format( "%-9s", nanosStr );
                nanosStr = nanosStr.replace( ' ', '0' );
            }
            else if( fields.size() == 1 )
            {
                secStr = fields.get( 0 );
            }
            else
            {
                throw new ValidationException(
                    "Invalid time string. Too many decimals ('.')" );
            }

            time.seconds = secondsParser.parse( secStr );
            time.nanoseconds = nanosStr.isEmpty() ? 0
                : nanosParser.parse( nanosStr );

            return time;
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
        public String getDescription( UnixTime time )
        {
            return String.format( "%d.%09d", time.seconds, time.nanoseconds );
        }
    }
}
