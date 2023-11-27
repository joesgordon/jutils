package jutils.core.data;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;

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
     * @param <T>
     **************************************************************************/
    public static class DataItemPairSerializer<T>
        implements IDataSerializer<DataItemPair<T>>
    {
        /**  */
        private final IDataSerializer<T> itemSerializer;

        /**
         * @param itemSerializer
         */
        public DataItemPairSerializer( IDataSerializer<T> itemSerializer )
        {
            this.itemSerializer = itemSerializer;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DataItemPair<T> read( IDataStream stream )
            throws IOException, ValidationException
        {
            long startPos = stream.getPosition();
            T item = itemSerializer.read( stream );
            long endPos = stream.getPosition();

            int length = ( int )( endPos - startPos );
            byte [] data = new byte[length];

            stream.seek( startPos );
            stream.readFully( data );

            return new DataItemPair<T>( data, item );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( DataItemPair<T> pair, IDataStream stream )
            throws IOException
        {
            stream.write( pair.data );
        }
    }
}
