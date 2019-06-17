package org.jutils.pattern;

/*******************************************************************************
 * Defines a matcher that always succeeds.
 ******************************************************************************/
public class YesMatcher implements IMatcher
{
    /** The name of the matcher */
    private final String name;

    /***************************************************************************
     * @param name the name of the matcher.
     **************************************************************************/
    public YesMatcher( String name )
    {
        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean matches( String str )
    {
        return true;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Match find( String str )
    {
        return new Match( true, 0, str.length() );
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
