package jutils.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jutils.core.ValidationException;
import jutils.core.time.TimeUtils;

/*******************************************************************************
 * Creates a stream where items are written to one file and the offsets (the
 * file offset in the first file) to each item are written to another file.
 * Meant to be a cached location for items of variable serialized length.
 * @param <T> the type of object to be referenced.
 ******************************************************************************/
public final class ReferenceStream<T> implements IReferenceStream<T>
{
    /**  */
    private static final String TIMEDATE_PREFIX = "uuuu-MM-dd_HHmmss_SSS_";
    /** The serializer to read/write each item. */
    private final IDataSerializer<T> serializer;
    /** The file to which items are written. */
    private File itemsFile;
    /** The stream that writes to the items file. */
    private IDataStream itemsStream;

    /**  */
    private LongStream positionsStream;

    /** The number of items that have been written. */
    private long count;

    /***************************************************************************
     * Creates a stream with the provided serializer that uses temporary files
     * for both items and references.
     * @param serializer the serializer to read/write each item.
     * @throws FileNotFoundException if there was a error creating/opening
     * temporary files.
     * @throws IOException if there was a error opening temporary files.
     **************************************************************************/
    public ReferenceStream( IDataSerializer<T> serializer )
        throws FileNotFoundException, IOException
    {
        this( serializer, createTempFile(), createTempFile(), true, true );
    }

    /***************************************************************************
     * @param serializer
     * @param itemsFile
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public ReferenceStream( IDataSerializer<T> serializer, File itemsFile )
        throws FileNotFoundException, IOException
    {
        this( serializer, itemsFile, createTempFile(), false, true );
    }

    /***************************************************************************
     * @param serializer
     * @param itemsFile
     * @param itemsStream
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public ReferenceStream( IDataSerializer<T> serializer, File itemsFile,
        IDataStream itemsStream ) throws FileNotFoundException, IOException
    {
        this( serializer, itemsFile, createTempFile(), false, true,
            itemsStream );
    }

    /***************************************************************************
     * @param serializer
     * @param itemsFile
     * @param referenceFile
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public ReferenceStream( IDataSerializer<T> serializer, File itemsFile,
        File referenceFile ) throws FileNotFoundException, IOException
    {
        this( serializer, itemsFile, referenceFile, false, false );
    }

    /***************************************************************************
     * @param serializer
     * @param itemsFile
     * @param referenceFile
     * @param deleteItems
     * @param deleteReference
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public ReferenceStream( IDataSerializer<T> serializer, File itemsFile,
        File referenceFile, boolean deleteItems, boolean deleteReference )
        throws FileNotFoundException, IOException
    {
        this( serializer, itemsFile, referenceFile, deleteItems,
            deleteReference, null );
    }

    /***************************************************************************
     * @param serializer
     * @param itemsFile
     * @param referenceFile
     * @param deleteItems
     * @param deleteReference
     * @param itemsStream
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public ReferenceStream( IDataSerializer<T> serializer, File itemsFile,
        File referenceFile, boolean deleteItems, boolean deleteReference,
        IDataStream itemsStream ) throws FileNotFoundException, IOException
    {
        if( itemsStream == null )
        {
            @SuppressWarnings( "resource")
            IDataStream is = createStream( itemsFile );
            itemsStream = is;
        }

        @SuppressWarnings( "resource")
        IDataStream rs = createStream( referenceFile );

        this.serializer = serializer;

        this.itemsFile = deleteItems ? itemsFile : null;
        this.itemsStream = null;

        this.positionsStream = new LongStream( referenceFile, rs,
            deleteReference );

        this.count = 0;

        setItemsStream( itemsStream );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        positionsStream.close();
        itemsStream.close();

        if( itemsFile != null )
        {
            itemsFile.delete();
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getCount()
    {
        return count;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( T item ) throws IOException
    {
        long rpos = count * 8;
        long ipos = this.itemsStream.getPosition();

        positionsStream.seek( rpos );
        itemsStream.seek( ipos );

        positionsStream.write( ipos );
        serializer.write( item, itemsStream );

        count++;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T read( long index ) throws IOException, ValidationException
    {
        long rpos = index * 8;
        long ipos;

        positionsStream.seek( rpos );

        ipos = positionsStream.read();

        itemsStream.seek( ipos );

        return serializer.read( itemsStream );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<T> read( long index, int count )
        throws IOException, ValidationException
    {
        List<T> items = new ArrayList<>( count );
        long rpos = index * 8;
        long ipos;

        positionsStream.seek( rpos );

        ipos = positionsStream.read();

        itemsStream.seek( ipos );

        for( int i = 0; i < count; i++ )
        {
            items.add( serializer.read( itemsStream ) );
        }

        return items;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeAll() throws IOException
    {
        count = 0;
        positionsStream.seek( 0L );
        itemsStream.seek( 0L );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IStream getItemsStream()
    {
        return itemsStream;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setItemsFile( File file ) throws IOException
    {
        if( itemsFile != null )
        {
            itemsFile.delete();
            itemsFile = null;
        }

        @SuppressWarnings( "resource")
        IDataStream stream = createStream( file );

        setItemsStream( stream );
    }

    /***************************************************************************
     * @param stream
     * @throws IOException
     **************************************************************************/
    public void setItemsStream( IDataStream stream ) throws IOException
    {
        if( itemsStream != null )
        {
            itemsStream.close();
        }

        this.itemsStream = stream;

        long itemStreamLength = itemsStream.getLength();

        this.count = 0;
        this.positionsStream.reset();

        if( itemStreamLength > 0 )
        {
            long pos = 0L;

            stream.seek( pos );

            while( pos < itemStreamLength )
            {
                // LogUtils.printDebug( "Reading item @ 0x%016X", pos );

                try
                {
                    positionsStream.write( pos );
                    serializer.read( itemsStream );
                    count++;
                }
                catch( ValidationException ex )
                {
                    count = 0;
                    throw new IOException( ex );
                }

                pos = itemsStream.getPosition();
            }

            // LogUtils.printDebug( "Read %d items", count );
        }
    }

    /***************************************************************************
     * @param file
     * @return
     * @throws FileNotFoundException
     **************************************************************************/
    @SuppressWarnings( "resource")
    private static IDataStream createStream( File file )
        throws FileNotFoundException
    {
        // TODO !!!! fix bug in BufferedStream.
        // To illustrate, start NetMessagesViewMain. Go back. Add.
        // return new DataStream( new BufferedStream( new FileStream( file ) )
        // );

        boolean readOnly = !file.exists() || !file.canWrite();
        return new DataStream( new FileStream( file, readOnly ) );
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    private static File createTempFile() throws IOException
    {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = TimeUtils.buildFormatter(
            TIMEDATE_PREFIX );
        String prefix = time.format( formatter );
        File file = File.createTempFile( prefix, "_refstream.bin" );

        file.deleteOnExit();

        return file;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return new ItemIterator<>( this );
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static final class ItemIterator<T> implements Iterator<T>
    {
        /**  */
        private ReferenceStream<T> stream;

        /**  */
        private long index = 0;

        /**
         * @param stream
         */
        public ItemIterator( ReferenceStream<T> stream )
        {
            this.stream = stream;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext()
        {
            return index < stream.getCount();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public T next()
        {
            try
            {
                return stream.read( index++ );
            }
            catch( IOException | ValidationException ex )
            {
                throw new IllegalStateException( ex );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class LongStream
    {
        /** The file to which references are written. */
        private final File file;
        /** The stream that writes references to the file. */
        private final IDataStream stream;
        /**  */
        private boolean deleteFile;

        /**
         * @param file
         * @param stream
         * @param deleteFile
         */
        public LongStream( File file, IDataStream stream, boolean deleteFile )
        {
            this.file = file;
            this.stream = stream;
            this.deleteFile = deleteFile;
        }

        public void reset() throws IOException
        {
            stream.seek( 0L );
        }

        /**
         * @return
         * @throws IOException
         */
        public long read() throws IOException
        {
            return stream.readLong();
        }

        /**
         * @param value
         * @throws IOException
         */
        public void write( long value ) throws IOException
        {
            stream.writeLong( value );
        }

        /**
         * @param position
         * @throws IOException
         */
        public void seek( long position ) throws IOException
        {
            stream.seek( position );
        }

        public void close() throws IOException
        {
            stream.close();

            if( deleteFile )
            {
                file.delete();
            }
        }
    }
}
