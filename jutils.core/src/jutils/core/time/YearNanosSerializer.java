package jutils.core.time;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;

/*******************************************************************************
 *
 ******************************************************************************/
public class YearNanosSerializer implements IDataSerializer<YearNanos>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public YearNanos read( IDataStream stream )
        throws IOException, ValidationException
    {
        YearNanos time = new YearNanos();

        time.year = stream.readShort();
        time.nanos = stream.readLong();

        return time;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( YearNanos time, IDataStream stream ) throws IOException
    {
        stream.writeShort( time.year );
        stream.writeLong( time.nanos );
    }
}
