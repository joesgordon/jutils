package jutils.core.io.xs;

import java.io.File;

import jutils.core.io.IStdSerializer;
import jutils.core.io.options.DefaultOptionsCreator;
import jutils.core.io.options.IOptionsCreator;
import jutils.core.io.options.OptionsSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class XsOptions
{
    /***************************************************************************
     * Creates an options serializer with the specified file and options creator
     * by ensuring the directory structure exists for the file before creation.
     * @param <T> the type of object to be serialized.
     * @param cls the class of the object to be serialized.
     * @param creator the default creator to be used.
     * @param file the file to be used for serialization.
     * @return the new options serializer.
     **************************************************************************/
    public static <T> OptionsSerializer<T> getOptions( Class<T> cls, File file,
        IOptionsCreator<T> creator )
    {
        IStdSerializer<T, File> serializer = new XsFileSerializer<>( cls );
        return new OptionsSerializer<T>( creator, file, serializer );
    }

    /***************************************************************************
     * Creates an options serializer with the specified file and class of the
     * item type.
     * @param cls the class of the object to be serialized.
     * @param file the file to be used for serialization.
     * @param <T> the type of object to be serialized.
     * @return the new options serializer.
     **************************************************************************/
    public static <T> OptionsSerializer<T> getOptions( Class<T> cls, File file )
    {
        return getOptions( cls, file, new DefaultOptionsCreator<T>( cls ) );
    }
}
