package jutils.core.io.parsers;

import jutils.core.INamedItem;

/*******************************************************************************
 * The type of existence to be checked (e.g. file/dir/either/none).
 ******************************************************************************/
public enum ExistenceType implements INamedItem
{
    /** The path must exist. */
    EXISTS( "Exists" ),
    /** The parent directory must exists. */
    PARENT_EXISTS( "Parent Exists" );

    /**  */
    public final String name;

    /***************************************************************************
     * @param name
     **************************************************************************/
    private ExistenceType( String name )
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
