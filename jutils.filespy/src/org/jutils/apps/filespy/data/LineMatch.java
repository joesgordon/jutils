package org.jutils.apps.filespy.data;

/*******************************************************************************
 * Represents the information need to be related to the user about a search
 * match within a text file.
 ******************************************************************************/
public class LineMatch
{
    /** The line number on which the match was found. */
    public final int lineNumber;
    /** The text prior to the match. */
    public final String preMatch;
    /** The text that matches the pattern. */
    public final String match;
    /** The text after the match. */
    public final String postMatch;

    /***************************************************************************
     * Creates a new set of match information with the provided values.
     * @param lineNumber the line number on which the match was found.
     * @param preMatch the text prior to the match.
     * @param match the text that matches the pattern.
     * @param postMatch the text after the match.
     **************************************************************************/
    public LineMatch( int lineNumber, String preMatch, String match,
        String postMatch )
    {
        this.lineNumber = lineNumber;
        this.preMatch = preMatch;
        this.match = match;
        this.postMatch = postMatch;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return preMatch + match + postMatch;
    }
}
