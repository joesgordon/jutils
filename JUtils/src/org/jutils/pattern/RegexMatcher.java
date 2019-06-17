package org.jutils.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jutils.ValidationException;

/*******************************************************************************
 * Defines a matcher that uses regular expressions as its pattern.
 ******************************************************************************/
public class RegexMatcher implements IMatcher
{
    /** The name of this matcher. */
    private final String name;
    /** The regular expression pattern used for matching. */
    private final Pattern pattern;

    /***************************************************************************
     * @param pattern
     * @throws ValidationException
     **************************************************************************/
    public RegexMatcher( StringPattern pattern ) throws ValidationException
    {
        this( pattern.name, pattern.patternText, pattern.isCaseSensitive );
    }

    /***************************************************************************
     * @param name
     * @param regex
     * @param isCaseSensitive
     * @throws ValidationException
     **************************************************************************/
    public RegexMatcher( String name, String regex, boolean isCaseSensitive )
        throws ValidationException
    {
        this( name, regex, isCaseSensitive, true );
    }

    /***************************************************************************
     * @param name
     * @param regex
     * @param isCaseSensitive
     * @param exact
     * @throws ValidationException
     **************************************************************************/
    public RegexMatcher( String name, String regex, boolean isCaseSensitive,
        boolean exact ) throws ValidationException
    {
        int flags = isCaseSensitive ? 0 : Pattern.CASE_INSENSITIVE;

        this.name = name;

        if( !exact )
        {
            if( !regex.startsWith( ".*" ) )
            {
                regex = ".*" + regex;
            }

            if( !regex.endsWith( ".*" ) )
            {
                regex = regex + ".*";
            }
        }

        try
        {
            this.pattern = Pattern.compile( regex, flags );
        }
        catch( IllegalArgumentException ex )
        {
            throw new ValidationException(
                "Unable to build regular expression pattern \"" + regex + "\"",
                ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean matches( String str )
    {
        Matcher matcher = pattern.matcher( str );

        return matcher.matches();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Match find( String str )
    {
        Matcher matcher = pattern.matcher( str );

        boolean matches = matcher.matches();
        int start = 0;
        int end = 0;

        if( matches )
        {
            start = matcher.start();
            end = matcher.end();
        }

        return new Match( matches, start, end );
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
