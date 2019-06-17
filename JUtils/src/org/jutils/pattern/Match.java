package org.jutils.pattern;

/*******************************************************************************
 * Relates if and where the pattern matched the input string.
 ******************************************************************************/
public class Match
{
    /** Whether the pattern matched the input string. */
    public final boolean matches;
    /** The inclusive start index. */
    public final int start;
    /** The exclusive end index. */
    public final int end;

    /***************************************************************************
     * 
     **************************************************************************/
    public Match()
    {
        this( false, 0, 0 );
    }

    /***************************************************************************
     * @param matches
     * @param start
     * @param end
     **************************************************************************/
    public Match( boolean matches, int start, int end )
    {
        this.matches = matches;
        this.start = start;
        this.end = end;
    }
}
