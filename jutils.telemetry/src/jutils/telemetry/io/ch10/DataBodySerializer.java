package jutils.telemetry.io.ch10;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataStream;
import jutils.telemetry.data.ch10.DataBody;
import jutils.telemetry.data.ch10.PacketHeader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataBodySerializer implements IBodySerializer<DataBody>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public DataBody read( IDataStream stream, PacketHeader header )
        throws IOException, ValidationException
    {
        DataBody body = new DataBody( header.dataLength );

        stream.readFully( body.data );

        return body;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( DataBody body, IDataStream stream, PacketHeader header )
        throws IOException
    {
        stream.write( body.data );
    }
}
