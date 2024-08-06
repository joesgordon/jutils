package jutils.telemetry.data.ch10.streaming;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.data.BitFieldInfo;
import jutils.core.data.INamedBitField;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.utils.ByteOrdering;
import jutils.telemetry.data.ch10.Format1Type;

/*******************************************************************************
 * As defined in Chapter 10.3.9.1.2. Format 1 uses
 * {@link ByteOrdering#LITTLE_ENDIAN Little Endian} byte ordering.
 ******************************************************************************/
public class StreamingFormat1Header
{
    /**  */
    public int sequence;
    /**  */
    public Format1Type type;
    /**  */
    public StreamingFormat format;

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum Tf1Fields implements INamedBitField
    {
        /**  */
        FORMAT( 0, 3, "Message Format" ),
        /**  */
        TYPE( 4, 7, "Message Type" ),
        /**  */
        SEQUENCE( 8, 31, "Message Sequence" ),;

        /**  */
        private final BitFieldInfo info;

        /**
         * @param start
         * @param end
         * @param name
         */
        private Tf1Fields( int start, int end, String name )
        {
            this.info = new BitFieldInfo( start, end, name );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName()
        {
            return info.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getStartBit()
        {
            return info.field.startBit;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getEndBit()
        {
            return info.field.endBit;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long getMask()
        {
            return info.field.mask;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class TransferFormat1HeaderSerializer
        implements IDataSerializer<StreamingFormat1Header>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public StreamingFormat1Header read( IDataStream stream )
            throws IOException, ValidationException
        {
            StreamingFormat1Header header = new StreamingFormat1Header();

            read( header, stream );

            return header;
        }

        /**
         * @param header
         * @param stream
         * @throws IOException
         * @throws ValidationException
         */
        private void read( StreamingFormat1Header header, IDataStream stream )
            throws IOException, ValidationException
        {
            int word = stream.readInt();

            header.sequence = Tf1Fields.SEQUENCE.getField( word );
            header.type = Format1Type.fromValue(
                Tf1Fields.TYPE.getField( word ) );
            header.format = StreamingFormat.fromValue(
                Tf1Fields.FORMAT.getField( word ) );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( StreamingFormat1Header header, IDataStream stream )
            throws IOException
        {
            // TODO Auto-generated method stub
        }
    }
}
