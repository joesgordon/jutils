package org.jutils.io;

/*******************************************************************************
 * Defines the methods of reading and writing objects of a particular type.
 * @param <T> the type of object to be read/written.
 ******************************************************************************/
public interface IDataSerializer<T> extends IDataReader<T>, IDataWriter<T>
{
}
