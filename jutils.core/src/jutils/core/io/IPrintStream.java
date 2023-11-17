package jutils.core.io;

import java.io.Closeable;

/*******************************************************************************
 * Defines methods to write formatted strings to a resource.
 ******************************************************************************/
public interface IPrintStream extends Closeable
{
    /**************************************************************************
     * Writes the provided string.
     * @param str the string to be written.
     **************************************************************************/
    public void print( String str );

    /**************************************************************************
     * Writes the provided string and a system line separator.
     * @param str the string to be written.
     **************************************************************************/
    public void println( String str );

    /***************************************************************************
     * Writes a system line separator.
     **************************************************************************/
    public void println();

    /***************************************************************************
     * Writes the formatted string and a system line separator.
     * @param format the format of the string to be written conforming to
     * {@link String#format(String, Object...)}.
     * @param args the items to be formatted into the string.
     **************************************************************************/
    public void println( String format, Object... args );

    /***************************************************************************
     * Writes the formatted string.
     * @param format the format of the string to be written conforming to
     * {@link String#format(String, Object...)}.
     * @param args the items to be formatted into the string.
     **************************************************************************/
    public void print( String format, Object... args );

    /***************************************************************************
     * Writes the provided characters and a system line separator.
     * @param chars the characters to be written.
     **************************************************************************/
    public void println( char [] chars );
}
