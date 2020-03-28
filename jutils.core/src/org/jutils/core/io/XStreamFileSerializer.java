package org.jutils.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jutils.core.ValidationException;

/*******************************************************************************
 * Serializes items to XML using XStream.
 * @param <T> the type of item to be serialized.
 ******************************************************************************/
public class XStreamFileSerializer<T> implements IStdSerializer<T, File>
{
    /**  */
    private final XStreamStreamSerializer<T> serializer;

    /***************************************************************************
     * @param clss
     * @throws ValidationException
     **************************************************************************/
    public XStreamFileSerializer( Class<?>... clss )
    {
        this.serializer = new XStreamStreamSerializer<>( clss );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T read( File file ) throws IOException, ValidationException
    {
        try( FileInputStream fis = new FileInputStream( file ) )
        {
            return serializer.read( fis );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( T item, File file ) throws IOException
    {
        try( FileOutputStream stream = new FileOutputStream( file ) )
        {
            serializer.write( item, stream );
        }
    }
}
