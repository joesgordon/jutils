package org.jutils.io;

import java.io.IOException;
import java.time.LocalDateTime;

import org.jutils.time.NanoTime;

/*******************************************************************************
 * Defines an {@link IDataSerializer} that reads/writes {@link LocalDateTime}s.
 ******************************************************************************/
public class LocalDateTimeSerializer implements IDataSerializer<LocalDateTime>
{
    /**  */
    public static final long NANOS_PER_DAY = 1000000000L * 60 * 60 * 24;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDateTime read( IDataStream stream ) throws IOException
    {
        NanoTime nt = new NanoTime();

        nt.year = stream.readShort();
        nt.nanoseconds = stream.readLong();

        return nt.toDateTime();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( LocalDateTime time, IDataStream stream )
        throws IOException
    {
        NanoTime nt = new NanoTime( time );

        stream.writeShort( nt.year );
        stream.writeLong( nt.nanoseconds );
    }
}
