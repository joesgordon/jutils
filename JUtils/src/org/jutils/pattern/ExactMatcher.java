package org.jutils.pattern;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ExactMatcher implements IMatcher
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
    public ExactMatcher( StringPattern pattern )
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
        String text = str;

        if( !isCaseSensitive )
        {
            text = str.toUpperCase();
        }

        return text.equals( pattern );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Match find( String str )
    {
        if( matches( str ) )
        {
            return new Match( true, 0, str.length() );
        }

        return new Match();
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
