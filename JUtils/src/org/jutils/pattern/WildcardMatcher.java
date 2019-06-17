package org.jutils.pattern;

import java.util.regex.Pattern;

import org.jutils.ValidationException;
import org.jutils.io.StringPrintStream;

/***************************************************************************
 * 
 **************************************************************************/
public class WildcardMatcher implements IMatcher
{
    /**  */
    private final RegexMatcher matcher;

    /***************************************************************************
     * @param pattern
     * @throws ValidationException
     **************************************************************************/
    public WildcardMatcher( StringPattern pattern ) throws ValidationException
    {
        this.matcher = buildMatcher( pattern );
    }

    /***************************************************************************
     * @param strPattern
     * @return
     * @throws ValidationException
     **************************************************************************/
    private static RegexMatcher buildMatcher( StringPattern strPattern )
        throws ValidationException
    {
        String patternText = strPattern.patternText;

        try( StringPrintStream stream = new StringPrintStream() )
        {
            int idx = -1;
            int from = 0;

            while( ( idx = patternText.indexOf( '*', from ) ) > -1 )
            {
                if( idx > from )
                {
                    String part = patternText.substring( from, idx );
                    stream.print( Pattern.quote( part ) );
                }

                stream.print( ".*" );

                from = idx + 1;
            }

            if( from < patternText.length() - 1 )
            {
                String part = patternText.substring( from );
                stream.print( Pattern.quote( part ) );
            }

            String regex = stream.toString();

            // LogUtils.printDebug( "regex: " + regex );

            return new RegexMatcher( strPattern.name, regex,
                strPattern.isCaseSensitive, false );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean matches( String str )
    {
        return matcher.matches( str );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Match find( String str )
    {
        return matcher.find( str );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return matcher.getName();
    }
}
