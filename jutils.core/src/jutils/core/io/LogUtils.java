package jutils.core.io;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

import jutils.core.Utils;
import jutils.core.time.TimeUtils;

/*******************************************************************************
 * Defines a set of standard utility functions to printing messages to the
 * standard output.
 ******************************************************************************/
public class LogUtils
{
    /**  */
    private static final String DATE_FORMAT = "HH:mm:ss:SSS";
    /** A date formatter for displaying debug statements. */
    private static final DateTimeFormatter DATE_FORMATTER;

    static
    {
        DATE_FORMATTER = TimeUtils.buildFormatter( DATE_FORMAT );
    }

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private LogUtils()
    {
    }

    /***************************************************************************
     * Prints the message to the log.
     * @param message the message to be written.
     **************************************************************************/
    public static void print( String message )
    {
        System.out.println( message );
    }

    /***************************************************************************
     * Prints the formatted message to the log.
     * @param format a string with a {@link Formatter}.
     * @param args the referenced by the format specifiers in the format string.
     **************************************************************************/
    public static void print( String format, Object... args )
    {
        print( String.format( format, args ) );
    }

    /***************************************************************************
     * Prints the message to the log with the header "DEBUG [HH:mm:ss:SSS]:".
     * @param message the message to be written.
     **************************************************************************/
    public static void printDebug( String message )
    {
        printMessage( "DEBUG", message );
    }

    /***************************************************************************
     * Prints the formatted message to the log with the header "DEBUG
     * [HH:mm:ss:SSS]: ".
     * @param format a string with a {@link Formatter}.
     * @param args the referenced by the format specifiers in the format string.
     **************************************************************************/
    public static void printDebug( String format, Object... args )
    {
        printMessage( "DEBUG", format, args );
    }

    /***************************************************************************
     * Prints the message to the log with the header "WARNING [HH:mm:ss:SSS]: ".
     * @param message the message to be written.
     **************************************************************************/
    public static void printWarning( String message )
    {
        printMessage( "WARNING", message );
    }

    /***************************************************************************
     * Prints the formatted message to the log with the header "WARNING
     * [HH:mm:ss:SSS]: ".
     * @param format a string with a {@link Formatter}.
     * @param args the referenced by the format specifiers in the format string.
     **************************************************************************/
    public static void printWarning( String format, Object... args )
    {
        printMessage( "WARNING", format, args );
    }

    /***************************************************************************
     * Prints the message to the log with the header "ERROR [HH:mm:ss:SSS]: ".
     * @param message the message to be written.
     **************************************************************************/
    public static void printError( String message )
    {
        printMessage( "ERROR", message );
    }

    /***************************************************************************
     * Prints the formatted message to the log with the header "ERROR
     * [HH:mm:ss:SSS]: ".
     * @param format a string with a {@link Formatter}.
     * @param args the referenced by the format specifiers in the format string.
     **************************************************************************/
    public static void printError( String format, Object... args )
    {
        printMessage( "ERROR", format, args );
    }

    /***************************************************************************
     * Prints the formatted message to the log with the header "ERROR
     * [HH:mm:ss:SSS]: ".
     * @param message the message to be written.
     * @param ex the exception that generated error.
     **************************************************************************/
    public static void printError( String message, Exception ex )
    {
        printMessage( "ERROR", message );
        Utils.printStackTrace( ex );
    }

    /***************************************************************************
     * Prints the message to the log with the header "INFO [HH:mm:ss:SSS]: ".
     * @param message the message to be written.
     **************************************************************************/
    public static void printInfo( String message )
    {
        printMessage( "INFO", message );
    }

    /***************************************************************************
     * Prints the formatted message to the log with the header "INFO
     * [HH:mm:ss:SSS]: ".
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
        String timeStr = LocalTime.now().format( DATE_FORMATTER );

        print( "%s[%s]: %s", msgClass, timeStr, message );
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
