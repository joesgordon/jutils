package org.jutils.io;

import java.io.*;
import java.util.List;

import org.jutils.ValidationException;

/*******************************************************************************
 * Represents a method of storing items to an underlying stream.
 * @param <T> the type of item to be stored.
 ******************************************************************************/
public interface IReferenceStream<T> extends Closeable
{
    /***************************************************************************
     * Returns the number of items currently stored.
     * @return the number of items currently stored.
     **************************************************************************/
    public long getCount();

    /***************************************************************************
     * Writes the provided item to the end of storage.
     * @param item the item to be stored.
     * @throws IOException any I/O error that occurs.
     **************************************************************************/
    public void write( T item ) throws IOException;

    /***************************************************************************
     * Reads the item at the provided index.
     * @param index the 0-relative index of the item to be read.
     * @return the item read at the provided index.
     * @throws IOException any I/O error that occurs.
     * @throws ValidationException if the data representing the item is invalid.
     **************************************************************************/
    public T read( long index ) throws IOException, ValidationException;

    /***************************************************************************
     * Reads {@code count} number of items starting at {@code index}.
     * @param index be beginning 0-relative index to start reading.
     * @param count the number of items to be read.
     * @return the list of items read.
     * @throws IOException any I/O error that occurs.
     * @throws ValidationException if the data representing the item is invalid.
     **************************************************************************/
    public List<T> read( long index, int count )
        throws IOException, ValidationException;

    /***************************************************************************
     * Returns the stream to which items are read/written.
     * @return the stream to which items are read/written.
     **************************************************************************/
    public IStream getItemsStream();

    /***************************************************************************
     * Sets the provided file as the storage backing this stream. If the
     * provided file is not empty, it will be read as if it has serialized
     * items.
     * @param file the file to back this stream.
     * @throws IOException any I/O error that occurs.
     **************************************************************************/
    public void setItemsFile( File file ) throws IOException;

    /***************************************************************************
     * Removes all items from both cache and the underlying storage.
     * @throws IOException any I/O error that occurs.
     **************************************************************************/
    public void removeAll() throws IOException;
}
