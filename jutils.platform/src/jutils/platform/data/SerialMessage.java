package jutils.platform.data;

import java.io.IOException;
import java.time.LocalDateTime;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.io.LocalDateTimeSerializer;
import jutils.core.time.TimeUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialMessage
{
    /**  */
    public final boolean isTransmitted;
    /**  */
    public final LocalDateTime time;
    /**  */
    public final byte [] data;
    /**   */
    public Object message;

    /***************************************************************************
     * @param isTransmitted
     * @param data
     **************************************************************************/
    public SerialMessage( boolean isTransmitted, byte [] data )
    {
        this( isTransmitted, data, TimeUtils.getUtcNow() );
    }

    /***************************************************************************
     * @param isTransmitted
     * @param data
     * @param time
     **************************************************************************/
    public SerialMessage( boolean isTransmitted, byte [] data,
        LocalDateTime time )
    {
        this.isTransmitted = isTransmitted;
        this.data = data;
        this.time = time;
        this.message = null;
    }

    /***************************************************************************
     * @param <T>
     * @return
     **************************************************************************/
    public <T> T getParsedMessage()
    {
        @SuppressWarnings( "unchecked")
        T msg = ( T )message;
        return msg;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class SerialMessageSerializer
        implements IDataSerializer<SerialMessage>
    {
        /**  */
        public static final String SERMSGS_EXT = "sermsgs";
        /**  */
        public static final String MSGS_EXT = "msgs";

        /**  */
        private final LocalDateTimeSerializer timeSerializer;

        /**
         * 
         */
        public SerialMessageSerializer()
        {
            this.timeSerializer = new LocalDateTimeSerializer();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SerialMessage read( IDataStream stream )
            throws IOException, ValidationException
        {
            boolean isTx = stream.readBoolean();
            int dataLength = stream.readInt();
            byte [] data = new byte[dataLength];

            stream.read( data );

            LocalDateTime time = timeSerializer.read( stream );

            return new SerialMessage( isTx, data, time );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( SerialMessage msg, IDataStream stream )
            throws IOException
        {
            stream.writeBoolean( msg.isTransmitted );
            stream.writeInt( msg.data.length );
            stream.write( msg.data );
            timeSerializer.write( msg.time, stream );
        }
    }
}
