package org.jutils.pattern;

import org.jutils.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IMatcher extends INamedItem
{
    /***************************************************************************
     * @param str the string to be searched.
     * @return {@code true} if the pattern matches the input, {@code false}
     * otherwise.
     **************************************************************************/
    public boolean matches( String str );

    /***************************************************************************
     * @param str the string to be searched.
     * @return results of search guaranteed to be non-null.
     **************************************************************************/
    public Match find( String str );

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName();
}
