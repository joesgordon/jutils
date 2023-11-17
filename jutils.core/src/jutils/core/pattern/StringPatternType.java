package jutils.core.pattern;

import jutils.core.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum StringPatternType implements INamedItem
{
    /**  */
    EXACT( "Exact" ),
    /**  */
    CONTAINS( "Contains" ),
    /**  */
    WILDCARD( "Wildcard" ),
    /**  */
    REGEX( "Regular Expression" );

    /**  */
    public final String name;

    /***************************************************************************
     * @param name
     **************************************************************************/
    private StringPatternType( String name )
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
