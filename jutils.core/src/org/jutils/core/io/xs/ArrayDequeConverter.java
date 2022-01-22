package org.jutils.core.io.xs;

import java.util.ArrayDeque;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * 
 */
public class ArrayDequeConverter extends AbstractCollectionConverter
{
    /**
     * @param mapper
     */
    public ArrayDequeConverter( Mapper mapper )
    {
        super( mapper );
    }

    /**
     * @{@inheritDoc}
     */
    @Override
    public boolean canConvert( @SuppressWarnings( "rawtypes") Class object )
    {
        return object.equals( ArrayDeque.class );
    }

    /**
     * @{@inheritDoc}
     */
    @Override
    public void marshal( Object value, HierarchicalStreamWriter writer,
        MarshallingContext context )
    {
        ArrayDeque<?> array = ( ArrayDeque<?> )value;

        for( Object item : array )
        {
            writeCompleteItem( item, context, writer );
        }
    }

    /**
     * @{@inheritDoc}
     */
    @SuppressWarnings( "unchecked")
    @Override
    public Object unmarshal( HierarchicalStreamReader reader,
        UnmarshallingContext context )
    {
        @SuppressWarnings( "rawtypes")
        ArrayDeque array = new ArrayDeque();

        while( reader.hasMoreChildren() )
        {
            final Object item = readCompleteItem( reader, context, null );
            array.add( item );
        }

        return array;
    }
}
