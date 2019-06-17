package org.jutils.ui;

import javax.swing.text.*;

/*******************************************************************************
 *
 ******************************************************************************/
public class ContentSequence implements CharSequence
{
    /**  */
    private int start = 0;
    /**  */
    private int end = -1;
    /**  */
    private AbstractDocument.Content content = null;

    /***************************************************************************
     * @param content Content
     **************************************************************************/
    public ContentSequence( AbstractDocument.Content content )
    {
        this( content, 0, content.length() );
    }

    /***************************************************************************
     * @param content Content
     * @param start int
     * @param end int
     **************************************************************************/
    public ContentSequence( AbstractDocument.Content content, int start,
        int end )
    {
        if( content == null )
        {
            throw new NullPointerException( "Content cannot be null!" );
        }
        if( start < 0 )
        {
            throw new ArrayIndexOutOfBoundsException(
                "Start cannot be negative!" );
        }
        if( start > content.length() - 1 )
        {
            throw new ArrayIndexOutOfBoundsException(
                "Start is after content length! " + start + " > " +
                    content.length() );
        }
        if( end > content.length() )
        {
            throw new ArrayIndexOutOfBoundsException(
                "End is after content length! " + start + " > " +
                    content.length() );
        }

        this.content = content;
        this.start = start;
        this.end = end;
    }

    /***************************************************************************
     * @param index int
     * @return char
     **************************************************************************/
    @Override
    public char charAt( int index )
    {
        if( ( start + index - 1 ) > end )
        {
            throw new ArrayIndexOutOfBoundsException(
                "End is after content length! " + start + " > " +
                    content.length() );
        }
        Segment seg = new Segment();
        try
        {
            content.getChars( start + index, 1, seg );
        }
        catch( BadLocationException ex )
        {
            throw new ArrayIndexOutOfBoundsException(
                "Location " + index + " does not exist in Content!" );
        }

        // LogUtils.printDebug( "charAt( " + index + " ) = " + seg.first() );

        return seg.first();
    }

    /***************************************************************************
     * @param newStart int
     * @param newEnd int
     * @return CharSequence
     **************************************************************************/
    @Override
    public CharSequence subSequence( int newStart, int newEnd )
    {
        // LogUtils.printDebug( "subSequence( " + newStart + ", " + newEnd +
        // " )"
        // );
        return new ContentSequence( content, start + newStart, start + newEnd );
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    @Override
    public String toString()
    {
        Segment seg = new Segment();
        int length = end - start;
        try
        {
            content.getChars( start, length, seg );
        }
        catch( BadLocationException ex )
        {
            throw new ArrayIndexOutOfBoundsException( "Location " + length +
                " does not exist in Content! Contact you local programmer, he screwed up!" );
        }
        // LogUtils.printDebug( "toString()=" + seg.toString() );

        return seg.toString();
    }

    /***************************************************************************
     * @return int
     **************************************************************************/
    @Override
    public int length()
    {
        // LogUtils.printDebug( "length() = " + ( end - start) );
        return end - start;
    }
}
