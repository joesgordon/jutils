package org.jutils.io;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

import org.jutils.Utils;

/*******************************************************************************
 * Defines a set of standard utility functions to printing messages to the
 * standard output.
 ******************************************************************************/
public class LogUtils
{
    /** A date formatter for displaying debug statements. */
    private static final SimpleDateFormat dateFormatter;

    static
    {
        dateFormatter = new SimpleDateFormat( "HH:mm:ss:SSS" );
    }

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private LogUtils()
    {
    }

    /***************************************************************************
     * Prints the message to the log with the header "DEBUG: ".
     * @param message the message to be written.
     **************************************************************************/
    public static void printDebug( String message )
    {
        printMessage( "DEBUG", message );
    }

    /***************************************************************************
     * Prints the formatted message to the log with the header "DEBUG: ".
     * @param format a string with a {@link Formatter}.
     * @param args the referenced by the format specifiers in the format string.
     **************************************************************************/
    public static void printDebug( String format, Object... args )
    {
        printMessage( "DEBUG", format, args );
    }

    /***************************************************************************
     * Prints the message to the log with the header "WARNING: ".
     * @param message the message to be written.
     **************************************************************************/
    public static void printWarning( String message )
    {
        printMessage( "WARNING", message );
    }

    /***************************************************************************
     * Prints the formatted message to the log with the header "WARNING: ".
     * @param format a string with a {@link Formatter}.
     * @param args the referenced by the format specifiers in the format string.
     **************************************************************************/
    public static void printWarning( String format, Object... args )
    {
        printMessage( "WARNING", format, args );
    }

    /***************************************************************************
     * Prints the message to the log with the header "ERROR: ".
     * @param message the message to be written.
     **************************************************************************/
    public static void printError( String message )
    {
        printMessage( "ERROR", message );
    }

    /***************************************************************************
     * Prints the formatted message to the log with the header "ERROR: ".
     * @param format a string with a {@link Formatter}.
     * @param args the referenced by the format specifiers in the format string.
     **************************************************************************/
    public static void printError( String format, Object... args )
    {
        printMessage( "ERROR", format, args );
    }

    /***************************************************************************
     * Prints the formatted message to the log with the header "ERROR: ".
     * @param message the message to be written.
     * @param ex the exception that generated error.
     **************************************************************************/
    public static void printError( String message, Exception ex )
    {
        printMessage( "ERROR", message );
        Utils.printStackTrace( ex );
    }

    /***************************************************************************
     * Prints the message to the log with the header "INFO: ".
     * @param message the message to be written.
     **************************************************************************/
    public static void printInfo( String message )
    {
        printMessage( "INFO", message );
    }

    /***************************************************************************
     * Prints the formatted message to the log with the header "INFO: ".
     * @param format a string with a {@link Formatter}.
     * @param args the referenced by the format specifiers in the format string.
     **************************************************************************/
    public static void printInfo( String format, Object... args )
    {
        printMessage( "INFO", format, args );
    }

    /***************************************************************************
     * Prints the message to the log with the provided header.
     * @param msgClass the type of message to be written.
     * @param message the message to be written.
     **************************************************************************/
    private static void printMessage( String msgClass, String message )
    {
        System.out.print( msgClass );
        System.out.print( "[" );
        System.out.print( dateFormatter.format( new Date() ) );
        System.out.print( "]: " );
        System.out.println( message );
    }

    /***************************************************************************
     * Prints the formatted message to the log with provided header.
     * @param msgClass the type of message to be written.
     * @param format a string with a {@link Formatter}.
     * @param args the referenced by the format specifiers in the format string.
     * @see String#format(String, Object...)
     **************************************************************************/
    private static void printMessage( String msgClass, String format,
        Object... args )
    {
        printMessage( msgClass, String.format( format, args ) );
    }
}
