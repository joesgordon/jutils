package org.jutils.io;

import java.io.IOException;

import org.jutils.utils.ByteOrdering;

/*******************************************************************************
 * Defines methods of reading and writing intrinsic data types from/to a stream.
 ******************************************************************************/
public interface IDataStream extends IStream
{
    /***************************************************************************
     * Sets the method of ordering bytes in this stream.
     * @param ordering the byte order for fields.
     **************************************************************************/
    public void setOrder( ByteOrdering ordering );

    /***************************************************************************
     * Returns the method of ordering bytes in this stream.
     * @return the endiannes of the data in this stream.
     **************************************************************************/
    public ByteOrdering getOrder();

    /***************************************************************************
     * Reads a boolean value from the stream. A boolean value occupies one byte
     * where {@code 0} is {@code false} and all other values are {@code true}.
     * @return the boolean read.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public boolean readBoolean() throws IOException;

    /***************************************************************************
     * Reads two bytes from the current position as a signed 16-bit value.
     * @return the value read.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public short readShort() throws IOException;

    /***************************************************************************
     * Reads four bytes from the current position as a signed 32-bit value.
     * @return the value read.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public int readInt() throws IOException;

    /***************************************************************************
     * Reads eight bytes from the current position as a signed 64-bit value.
     * @return the value read.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public long readLong() throws IOException;

    /***************************************************************************
     * Reads four bytes from the current position as a IEEE 32-bit floating
     * point value.
     * @return the value read.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public float readFloat() throws IOException;

    /***************************************************************************
     * Reads eight bytes from the current position as a IEEE 64-bit floating
     * point value.
     * @return the value read.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public double readDouble() throws IOException;

    /***************************************************************************
     * Writes the provided boolean as a one byte value: {@code 0} if
     * {@code false} and {@code 1} if {@code true}.
     * @param v the value to be written.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public void writeBoolean( boolean v ) throws IOException;

    /***************************************************************************
     * Writes the provided signed 16-bit value to the following two bytes at the
     * current position.
     * @param v the value to be written.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public void writeShort( short v ) throws IOException;

    /***************************************************************************
     * Writes the provided signed 32-bit value to the following four bytes at
     * the current position.
     * @param v the value to be written.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public void writeInt( int v ) throws IOException;

    /***************************************************************************
     * Writes the provided signed 64-bit value to the following eight bytes at
     * the current position.
     * @param v the value to be written.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public void writeLong( long v ) throws IOException;

    /***************************************************************************
     * Writes the provided 32-bit IEEE value to the following four bytes at the
     * current position.
     * @param v the value to be written.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public void writeFloat( float v ) throws IOException;

    /***************************************************************************
     * Writes the provided 64-bit IEEE value to the following eight bytes at the
     * current position.
     * @param v the value to be written.
     * @throws IOException if any I/O error occurs.
     **************************************************************************/
    public void writeDouble( double v ) throws IOException;
}
