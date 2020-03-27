package org.jutils.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jutils.core.ValidationException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class XStreamStreamSerializer<T>
    implements ISerializer<T, InputStream, OutputStream>
{
    /**  */
    private final XStream xstream;

    /***************************************************************************
     * @param clss
     * @throws ValidationException
     **************************************************************************/
    public XStreamStreamSerializer( Class<?>... clss )
    {
        this.xstream = XStreamUtils.createXStream( clss );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T read( InputStream stream ) throws IOException, ValidationException
    {
        try
        {
            Object obj = xstream.fromXML( stream );

            @SuppressWarnings( "unchecked")
            T item = ( T )obj;

            return item;
        }
        catch( XStreamException ex )
        {
            throw new ValidationException( ex.getMessage(), ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( T item, OutputStream stream ) throws IOException
    {
        xstream.toXML( item, stream );
    }
}
