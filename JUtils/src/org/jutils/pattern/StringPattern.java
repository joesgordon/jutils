package org.jutils.pattern;

import org.jutils.ValidationException;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StringPattern
{
    /**  */
    public StringPatternType type;
    /**  */
    public String patternText;
    /**  */
    public boolean isCaseSensitive;
    /**  */
    public String name;

    /***************************************************************************
     * 
     **************************************************************************/
    public StringPattern()
    {
        this.type = StringPatternType.CONTAINS;
        this.patternText = "";
        this.isCaseSensitive = false;
        this.name = "";
    }

    /***************************************************************************
     * @param pattern
     **************************************************************************/
    public StringPattern( StringPattern pattern )
    {
        set( pattern );
    }

    /***************************************************************************
     * @param name
     **************************************************************************/
    public StringPattern( String name )
    {
        this();

        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return String.format( "%s: %s{%s}[%b]", name, type.name, patternText,
            isCaseSensitive );
    }

    /***************************************************************************
     * @return
     * @throws ValidationException
     **************************************************************************/
    public IMatcher createMatcher() throws ValidationException
    {
        if( patternText.isEmpty() )
        {
            return new YesMatcher( name );
        }

        switch( type )
        {
            case EXACT:
                return new ExactMatcher( this );

            case CONTAINS:
                return new ContainsMatcher( this );

            case WILDCARD:
                return new WildcardMatcher( this );

            case REGEX:
                return new RegexMatcher( this );

            default:
                break;
        }

        throw new IllegalStateException( "Unhandled type " + type.name );
    }

    /***************************************************************************
     * @param pattern
     **************************************************************************/
    public void set( StringPattern pattern )
    {
        this.type = pattern.type;
        this.isCaseSensitive = pattern.isCaseSensitive;
        this.patternText = pattern.patternText;
        this.name = pattern.name;
    }
}
