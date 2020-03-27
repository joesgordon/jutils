package org.jutils.core.io.parsers;

import java.io.File;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.IParser;

/*******************************************************************************
 * Parses a path string into a {@link File}.
 ******************************************************************************/
public class FileParser implements IParser<File>
{
    /** The existence to be checked. */
    public final ExistenceType type;
    /** Required path strings must be non-empty. */
    public final boolean required;

    /***************************************************************************
     * Creates a new parser that ensures path strings are
     * {@link ExistenceType#FILE_ONLY} (paths must be non-empty).
     **************************************************************************/
    public FileParser()
    {
        this( ExistenceType.FILE_ONLY );
    }

    /***************************************************************************
     * Creates a new parser that checks path strings with the provided existence
     * type (paths must be non-empty).
     * @param type the existence type to be checked.
     **************************************************************************/
    public FileParser( ExistenceType type )
    {
        this( type, true );
    }

    /***************************************************************************
     * Creates a new parser that checks path strings with the provided values.
     * @param type the existence type to be checked.
     * @param required an empty string is invalid if {@code true}.
     **************************************************************************/
    public FileParser( ExistenceType type, boolean required )
    {
        this.type = type;
        this.required = required;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public File parse( String text ) throws ValidationException
    {
        if( text.isEmpty() )
        {
            if( required )
            {
                throw new ValidationException( "Empty path string" );
            }

            return null;
        }

        File f = new File( text );

        IOUtils.validateFile( f, type );

        return f.getAbsoluteFile();
    }
}
