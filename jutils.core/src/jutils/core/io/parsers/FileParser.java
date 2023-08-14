package jutils.core.io.parsers;

import java.io.File;

import jutils.core.ValidationException;
import jutils.core.io.IOUtils;
import jutils.core.io.IParser;

/*******************************************************************************
 * Parses a path string into a {@link File}.
 ******************************************************************************/
public class FileParser implements IParser<File>
{
    /** Required path strings must be non-empty. */
    public final FileType fileType;
    /** The existence to be checked. */
    public final ExistenceType existence;
    /** Whether a blank field returns null or is invalid. */
    public final boolean isBlankAllowed;

    /***************************************************************************
     * Creates a new parser that ensures path strings are
     * {@link FileType#FILE}s, exist, and may not be empty.
     **************************************************************************/
    public FileParser()
    {
        this( FileType.FILE );
    }

    /***************************************************************************
     * Creates a new parser that ensures path strings are of the provided file
     * type, exist, and may not be empty.
     * @param fileType the type of file to be check for.
     **************************************************************************/
    public FileParser( FileType fileType )
    {
        this( fileType, ExistenceType.EXISTS );
    }

    /***************************************************************************
     * Creates a new parser that checks path strings with the provided file
     * type, provided existence type, and may not be empty.
     * @param fileType
     * @param existence the existence type to be checked.
     **************************************************************************/
    public FileParser( FileType fileType, ExistenceType existence )
    {
        this( fileType, existence, false );
    }

    /***************************************************************************
     * Creates a new parser that checks path strings with the provided file
     * type, provided existence type, and may be empty according to the provided
     * boolean.
     * @param fileType the type of file to be check for.
     * @param existence the existence type to be checked.
     * @param isBlankAllowed whether a blank field returns null or is invalid.
     **************************************************************************/
    public FileParser( FileType fileType, ExistenceType existence,
        boolean isBlankAllowed )
    {
        this.fileType = fileType;
        this.existence = existence;
        this.isBlankAllowed = isBlankAllowed;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public File parse( String text ) throws ValidationException
    {
        if( text.isEmpty() )
        {
            if( !isBlankAllowed )
            {
                throw new ValidationException( "Empty path string" );
            }

            return null;
        }

        File f = new File( text );

        IOUtils.validateFile( f, fileType, existence );

        return f.getAbsoluteFile();
    }
}
