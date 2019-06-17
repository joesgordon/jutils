package org.jutils.io.parsers;

import java.io.File;

import org.jutils.ValidationException;
import org.jutils.io.IParser;

/***************************************************************************
 * 
 **************************************************************************/
public class FileParser implements IParser<File>
{
    /**  */
    private final ExistenceType type;
    /**  */
    private final boolean required;

    /***************************************************************************
     * 
     **************************************************************************/
    public FileParser()
    {
        this( ExistenceType.FILE_ONLY );
    }

    /***************************************************************************
     * @param type
     **************************************************************************/
    public FileParser( ExistenceType type )
    {
        this( type, true );
    }

    /***************************************************************************
     * @param type
     * @param required
     **************************************************************************/
    public FileParser( ExistenceType type, boolean required )
    {
        this.type = type;
        this.required = required;
    }

    /***************************************************************************
     * 
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

        if( type == ExistenceType.DO_NOT_CHECK )
        {
            File parent = f.getAbsoluteFile().getParentFile();

            if( parent != null && !parent.exists() )
            {
                throw new ValidationException( "Parent Path does not exist" );
            }

            return f;
        }

        // LogUtils.printDebug( "Testing path " + text );

        if( !f.exists() )
        {
            throw new ValidationException( "Path does not exist" );
        }

        boolean isFile = f.isFile();
        boolean isDir = f.isDirectory();

        if( type != ExistenceType.DO_NOT_CHECK )
        {
            if( type == ExistenceType.DIRECTORY_ONLY && !isDir )
            {
                throw new ValidationException( "Path is not a directory" );
            }
            else if( type == ExistenceType.FILE_ONLY && !isFile )
            {
                throw new ValidationException( "Path is not a file" );
            }
            else if( type == ExistenceType.FILE_OR_DIRECTORY && !isFile &&
                !isDir )
            {
                throw new ValidationException(
                    "Path is not a file or directory" );
            }
        }

        return f.getAbsoluteFile();
    }
}
