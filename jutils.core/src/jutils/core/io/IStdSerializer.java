package jutils.core.io;

import java.io.IOException;

import jutils.core.ValidationException;

/*******************************************************************************
 * Defines methods of reading/writing objects of a particular type from/to a
 * resource of a particular type.
 * @param <DATA> the type of object to be read/written.
 * @param <RESOURCE> the type of resource to be accessed.
 ******************************************************************************/
public interface IStdSerializer<DATA, RESOURCE>
    extends ISerializer<DATA, RESOURCE, RESOURCE>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public DATA read( RESOURCE resource )
        throws IOException, ValidationException;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( DATA item, RESOURCE resource ) throws IOException;
}
