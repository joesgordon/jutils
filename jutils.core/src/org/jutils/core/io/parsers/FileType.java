package org.jutils.core.io.parsers;

import org.jutils.core.INamedItem;

/*******************************************************************************
 * The type of existence to be checked (e.g. file/dir/either/none).
 ******************************************************************************/
public enum FileType implements INamedItem
{
    /** The path must denote an existing file. */
    FILE( "File" ),
    /** The path must denote an existing directory. */
    DIRECTORY( "Directory" ),
    /**
     * The path must denote an existing file or directory (i.e. the path must
     * exist).
     */
    PATH( "Path" );

    /**  */
    public final String name;

    /***************************************************************************
     * @param name
     **************************************************************************/
    private FileType( String name )
    {
        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }
}
