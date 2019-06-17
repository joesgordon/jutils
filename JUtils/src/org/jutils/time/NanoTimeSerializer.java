package org.jutils.time;

import java.io.IOException;

import org.jutils.ValidationException;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

/*******************************************************************************
 *
 ******************************************************************************/
public class NanoTimeSerializer implements IDataSerializer<NanoTime>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NanoTime read( IDataStream stream )
        throws IOException, ValidationException
    {
        NanoTime time = new NanoTime();

        time.year = stream.readShort();
        time.nanoseconds = stream.readLong();

        return time;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( NanoTime time, IDataStream stream ) throws IOException
    {
        stream.writeShort( time.year );
        stream.writeLong( time.nanoseconds );
    }
}
