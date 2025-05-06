package jutils.telemetry.ch10.io;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataStream;
import jutils.core.io.IKeyedSerializer;
import jutils.telemetry.ch10.IPacketBody;
import jutils.telemetry.ch10.PacketHeader;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public interface IBodySerializer<T extends IPacketBody>
    extends IKeyedSerializer<PacketHeader, T>
{
    /***************************************************************************
     * @param stream
     * @param header
     * @return
     **************************************************************************/
    @Override
    public T read( IDataStream stream, PacketHeader header )
        throws IOException, ValidationException;

    /***************************************************************************
     * @param body
     * @param stream
     * @param header
     **************************************************************************/
    @Override
    public void write( T body, IDataStream stream, PacketHeader header )
        throws IOException;
}
