package jutils.core.data;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.ByteArrayStream;
import jutils.core.io.DataStream;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.utils.ByteOrdering;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class DataItemPair<T>
{
    /**  */
    public final byte [] data;
    /**  */
    public final T item;

    /***************************************************************************
     * @param data
     * @param item
     **************************************************************************/
    public DataItemPair( byte [] data, T item )
    {
        this.data = data;
        this.item = item;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class DataItemPairSerializer<T>
        implements IDataSerializer<DataItemPair<T>>
    {
        /**  */
        private final IDataSerializer<T> itemSerializer;
        /**  */
        private ByteOrdering itemOrder;

        /**
         * @param itemSerializer
         */
        public DataItemPairSerializer( IDataSerializer<T> itemSerializer )
        {
            this( itemSerializer, ByteOrdering.BIG_ENDIAN );
        }

        /**
         * @param itemSerializer
         * @param order
         */
        public DataItemPairSerializer( IDataSerializer<T> itemSerializer,
            ByteOrdering order )
        {
            this.itemSerializer = itemSerializer;
            this.itemOrder = order;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DataItemPair<T> read( IDataStream stream )
            throws IOException, ValidationException
        {
            int length;
            byte [] data;
            T item = null;

            length = stream.readInt();
            data = new byte[length];

            stream.readFully( data );

            try( ByteArrayStream bas = new ByteArrayStream( data, data.length,
                0, false );
                 IDataStream itemStream = new DataStream( bas,
                     this.itemOrder ) )
            {
                item = itemSerializer.read( itemStream );
            }
            catch( IOException | ValidationException ex )
            {
                throw new RuntimeException( ex );
            }

            return new DataItemPair<T>( data, item );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( DataItemPair<T> pair, IDataStream stream )
            throws IOException
        {
            stream.writeInt( pair.data.length );
            stream.write( pair.data );
        }
    }
}
