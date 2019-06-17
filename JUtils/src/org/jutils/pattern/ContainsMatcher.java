package org.jutils.pattern;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ContainsMatcher implements IMatcher
{
    /**  */
    private final boolean isCaseSensitive;
    /**  */
    private final String name;
    /**  */
    private final String pattern;

    /***************************************************************************
     * @param pattern
     **************************************************************************/
    public ContainsMatcher( StringPattern pattern )
    {
        this.isCaseSensitive = pattern.isCaseSensitive;
        this.name = pattern.name;
        this.pattern = pattern.isCaseSensitive ? pattern.patternText
            : pattern.patternText.toUpperCase();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean matches( String str )
    {
        return prepare( str ).contains( pattern );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Match find( String str )
    {
        int idx = prepare( str ).indexOf( pattern );

        if( idx < 0 )
        {
            return new Match();
        }

        return new Match( true, idx, idx + pattern.length() );
    }

    /***************************************************************************
     * @param str
     * @return
     **************************************************************************/
    private String prepare( String str )
    {
        return isCaseSensitive ? str : str.toUpperCase();
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
