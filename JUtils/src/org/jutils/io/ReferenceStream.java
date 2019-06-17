package org.jutils.io;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jutils.ValidationException;

/*******************************************************************************
 * Creates a stream where items are written to one file and the offsets (the
 * file offset in the first file) to each item are written to another file.
 * Meant to be a cached location for items of variable serialized length.
 * @param <T> the type of object to be referenced.
 ******************************************************************************/
public final class ReferenceStream<T> implements IReferenceStream<T>
{
    /** The serializer to read/write each item. */
    private final IDataSerializer<T> serializer;
    /** The file to which items are written. */
    private File itemsFile;
    /** The stream that writes to the items file. */
    private IDataStream itemsStream;

    /** The file to which references are written. */
    private final File referenceFile;
    /** The stream that writes references to the file. */
    private final IDataStream refStream;

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
        this( serializer, createTempFile(), true );
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
        this( serializer, itemsFile, false );
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
        this( serializer, itemsFile, false, createStream( itemsFile ),
            referenceFile, false, createStream( referenceFile ) );
    }

    /***************************************************************************
     * @param serializer
     * @param refStream
     * @param itemsStream
     * @throws IOException
     **************************************************************************/
    public ReferenceStream( IDataSerializer<T> serializer,
        IDataStream refStream, IDataStream itemsStream ) throws IOException
    {
        this( serializer, null, false, refStream, null, false, itemsStream );
    }

    /***************************************************************************
     * @param serializer
     * @param itemsFile
     * @param deleteItems
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    private ReferenceStream( IDataSerializer<T> serializer, File itemsFile,
        boolean deleteItems ) throws FileNotFoundException, IOException
    {
        this( serializer, itemsFile, deleteItems, createTempFile(), true );
    }

    /***************************************************************************
     * @param serializer
     * @param itemsFile
     * @param deleteItems
     * @param referenceFile
     * @param deleteReference
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    private ReferenceStream( IDataSerializer<T> serializer, File itemsFile,
        boolean deleteItems, File referenceFile, boolean deleteReference )
        throws FileNotFoundException, IOException
    {
        this( serializer, itemsFile, deleteItems, createStream( itemsFile ),
            referenceFile, deleteReference, createStream( referenceFile ) );
    }

    /***************************************************************************
     * @param serializer the serializer to read/write each item.
     * @param itemsFile the file to which items are written.
     * @param deleteItems whether or not to delete the items file on close.
     * @param itemsStream
     * @param referenceFile
     * @param deleteReference
     * @param referenceStream
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    private ReferenceStream( IDataSerializer<T> serializer, File itemsFile,
        boolean deleteItems, IDataStream itemsStream, File referenceFile,
        boolean deleteReference, IDataStream referenceStream )
        throws FileNotFoundException, IOException
    {
        this.serializer = serializer;

        this.itemsFile = deleteItems ? itemsFile : null;
        this.itemsStream = itemsStream;

        this.referenceFile = deleteReference ? referenceFile : null;
        this.refStream = referenceStream;

        this.count = refStream.getLength() / 8;

        long itemStreamLength = itemsStream.getLength();

        if( itemStreamLength > 0 && count == 0 )
        {
            while( itemsStream.getAvailable() > 0 )
            {
                try
                {
                    serializer.read( itemsStream );
                    count++;
                }
                catch( ValidationException ex )
                {
                    count = 0;
                    throw new IOException( ex );
                }
            }
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        refStream.close();
        itemsStream.close();

        if( referenceFile != null )
        {
            referenceFile.delete();
        }

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

        refStream.seek( rpos );
        itemsStream.seek( ipos );

        refStream.writeLong( ipos );
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

        refStream.seek( rpos );

        ipos = refStream.readLong();

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

        refStream.seek( rpos );

        ipos = refStream.readLong();

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
        refStream.seek( 0L );
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
        this.itemsStream.close();

        if( itemsFile != null )
        {
            itemsFile.delete();
            itemsFile = null;
        }

        this.itemsStream = createStream( file );

        long itemStreamLength = itemsStream.getLength();

        this.count = 0;
        this.refStream.seek( 0L );

        if( itemStreamLength > 0 )
        {
            long pos;
            while( ( pos = itemsStream.getPosition() ) < itemStreamLength )
            {
                // LogUtils.printDebug( "Reading item @ 0x%016X", pos );
                try
                {
                    refStream.writeLong( pos );
                    serializer.read( itemsStream );
                    count++;
                }
                catch( ValidationException ex )
                {
                    count = 0;
                    throw new IOException( ex );
                }
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
        return new DataStream( new FileStream( file ) );
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    private static File createTempFile() throws IOException
    {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "uuuu-MM-dd_HHmmss_SSS_" );
        String prefix = time.format( formatter );
        File file = File.createTempFile( prefix, "_refstream.bin" );

        file.deleteOnExit();

        return file;
    }
}
