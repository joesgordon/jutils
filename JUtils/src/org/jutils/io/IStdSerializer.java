package org.jutils.io;

/*******************************************************************************
 * Defines methods of reading/writing objects of a particular type from/to a
 * resource of a particular type.
 * @param <DATA> the type of object to be read/written.
 * @param <RESOURCE> the type of resource to be accessed.
 ******************************************************************************/
public interface IStdSerializer<DATA, RESOURCE>
    extends ISerializer<DATA, RESOURCE, RESOURCE>
{
}
