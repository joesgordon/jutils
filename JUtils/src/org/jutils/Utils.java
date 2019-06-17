package org.jutils;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;

import javax.swing.JTable;

import org.jutils.data.SystemProperty;

/*******************************************************************************
 * Utility class for general static functions.
 ******************************************************************************/
public final class Utils
{
    /** A list of all regular expression meta characters. */
    public static final char [] REGEX_METAC;
    /** The new line string for the current system. */
    public static final String NEW_LINE;
    /** The user's home directory. */
    public static final String USER_HOME;

    static
    {
        REGEX_METAC = new char[] { '\\', '^', '|', '[', ']', '(', ')', '$', '.',
            '+', '*', '?', '{', '}' };
        NEW_LINE = SystemProperty.LINE_SEPARATOR.getProperty();
        USER_HOME = SystemProperty.USER_DIR.getProperty();
    }

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private Utils()
    {
    }

    /***************************************************************************
     * Provides a method of comparing two doubles within a given precision.
     * @param a double to compare.
     * @param b double to compare.
     * @param epsilon precision of comparison.
     * @return {@code true} if the doubles are equal to within the provided
     * precision; {@code false} otherwise.
     **************************************************************************/
    public static boolean doubleEquals( double a, double b, double epsilon )
    {
        return a == b ? true : Math.abs( a - b ) < epsilon;
    }

    /***************************************************************************
     * Creates a multi-line string representing the stack trace from the point
     * of the caller.
     * @return a multi-line string of the stack.
     **************************************************************************/
    public static String getStackTrace()
    {
        Throwable tr = new Throwable();
        StackTraceElement [] trace = tr.getStackTrace();
        StackTraceElement [] newTrace = Arrays.copyOfRange( trace, 1,
            trace.length );
        tr.setStackTrace( newTrace );

        return printStackTrace( tr );
    }

    /***************************************************************************
     * Prints the stack trace from the caller.
     **************************************************************************/
    public static void printStackTrace()
    {
        Throwable tr = new Throwable();
        StackTraceElement [] trace = tr.getStackTrace();
        StackTraceElement [] newTrace = Arrays.copyOfRange( trace, 1,
            trace.length );
        tr.setStackTrace( newTrace );
        System.out.println( printStackTrace( tr ) );
    }

    /***************************************************************************
     * Prints the stack trace of the provided throwable to a string.
     * @param tr the throwable containing the stack trace to be printed.
     * @return String the string representation of the stack trace.
     **************************************************************************/
    public static String printStackTrace( Throwable tr )
    {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter( result );
        tr.printStackTrace( printWriter );
        return result.toString();
    }

    /***************************************************************************
     * Determines the maximum size from the provided sizes.
     * @param max the dimension to set as the maximum (output). Not used in the
     * maximum calculation.
     * @param sizes the sizes to iterate over to get a maximum size.
     **************************************************************************/
    public static void getMaxSize( Dimension max, Dimension... sizes )
    {
        max.width = sizes[0].width;
        max.height = sizes[0].height;

        for( int i = 1; i < sizes.length; i++ )
        {
            max.width = Math.max( max.width, sizes[i].width );
            max.height = Math.max( max.height, sizes[i].height );
        }
    }

    /***************************************************************************
     * Returns the maximum value of the provided list.
     * @param values list of values to find a maximum.
     * @return the maximum of the provided values.
     **************************************************************************/
    public static int max( int... values )
    {
        int max = values[0];

        for( int i = 1; i < values.length; i++ )
        {
            max = Math.max( max, values[i] );
        }

        return max;
    }

    /***************************************************************************
     * Builds a URL for a resource relative to the provided class by the
     * provided string.
     * @param c the class from which the relative path of the resource is known.
     * @param resource the path to the resource relative to the class.
     * @return the URL path of the resource.
     **************************************************************************/
    public static URL loadResourceURL( Class<?> c, String resource )
    {
        URL url = c.getResource( resource );
        return url;
    }

    /***************************************************************************
     * Splits the provided string into multiple fields delimited by spaces,
     * retaining empty fields as empty strings.
     * @param str the delimited string of items.
     * @return the list of items found.
     **************************************************************************/
    public static List<String> split( String str )
    {
        return splitSkip( str, false, ' ', '\t' );
    }

    /***************************************************************************
     * Splits the provided string into multiple fields delimited by the provided
     * list of characters, retaining empty fields as empty strings.
     * @param str the delimited string of items.
     * @param delimeters the list of characters that could separate each item.
     * @return the list of items found.
     **************************************************************************/
    public static List<String> split( String str, char... delimeters )
    {
        return splitSkip( str, false, delimeters );
    }

    /***************************************************************************
     * Splits the provided string into multiple fields delimited by spaces,
     * skipping empty fields.
     * @param str the delimited string of items.
     * @return the list of items found.
     **************************************************************************/
    public static List<String> splitSkip( String str )
    {
        return splitSkip( str, true, ' ', '\t' );
    }

    /***************************************************************************
     * Splits the provided string into multiple fields delimited by the provided
     * list of characters, skipping empty fields.
     * @param str the delimited string of items.
     * @param delimeters the list of characters that could separate each item.
     * @return the list of items found.
     **************************************************************************/
    public static List<String> splitSkip( String str, char... delimeters )
    {
        return splitSkip( str, true, delimeters );
    }

    /***************************************************************************
     * Splits the provided string into multiple fields delimited by the provided
     * list of characters, skipping empty fields if indicated.
     * @param str the delimited string of items.
     * @param skip skips empty fields when {@code true}; returns an empty string
     * for each empty field otherwise.
     * @param delimeters the list of characters that could separate each item.
     * @return the list of items found.
     **************************************************************************/
    public static List<String> splitSkip( String str, boolean skip,
        char... delimeters )
    {
        List<String> fields = new ArrayList<>();
        int start = -1;
        char c;
        boolean isDelim = false;

        for( int i = 0; i < str.length(); i++ )
        {
            c = str.charAt( i );

            isDelim = false;

            for( char delim : delimeters )
            {
                if( c == delim )
                {
                    isDelim = true;
                    break;
                }
            }

            if( isDelim )
            {
                start++;
                if( !skip || i > start )
                {
                    fields.add( str.substring( start, i ) );
                }

                start = i;
            }
        }

        start++;
        if( !skip || start < str.length() )
        {
            fields.add( str.substring( start ) );
        }

        return fields;
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the array and places a
     * comma and a space between each.
     * @param items Object[] the array to convert to a string.
     * @return the comma separated list of items.
     **************************************************************************/
    public static String arrayToString( Object [] items )
    {
        return arrayToString( items, ", " );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the array and places
     * the provided delimiter between each.
     * @param items the objects to convert to a string.
     * @param delimiter the characters to place between the items.
     * @return the string of items.
     **************************************************************************/
    public static String arrayToString( Object [] items, String delimiter )
    {
        StringBuffer buf = new StringBuffer();
        for( int i = 0; i < items.length; i++ )
        {
            if( i > 0 )
            {
                buf.append( delimiter );
            }
            buf.append( items[i] );
        }

        return buf.toString();
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each item and places a comma between
     * each.
     * @param items the objects to convert to a string.
     * @return the comma separated string of items.
     **************************************************************************/
    public static String argsToString( Object... items )
    {
        return arrayToString( items );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the array and places
     * the provided delimiter between each.
     * @param items the items to be concatenated.
     * @return the comma separated string of items.
     **************************************************************************/
    public static String itemsToString( Object... items )
    {
        return arrayToString( items );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the array and places
     * the provided delimiter between each.
     * @param delimiter the characters to place between the items.
     * @param items the items to be concatenated.
     * @return the string of items.
     **************************************************************************/
    public static String itemsToString( String delimiter, Object... items )
    {
        return arrayToString( items, delimiter );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the collection and
     * places a comma between each.
     * @param items the collection to convert to a string.
     * @return the comma separated list of items.
     **************************************************************************/
    public static String collectionToString( Collection<?> items )
    {
        return arrayToString( items.toArray() );
    }

    /***************************************************************************
     * Calls {@link Object#toString()} for each object in the collection and
     * places the provided delimiter between each.
     * @param items the items to be concatenated.
     * @param delimiter the characters to place between the items.
     * @return the string of items.
     **************************************************************************/
    public static String collectionToString( Collection<?> items,
        String delimiter )
    {
        return arrayToString( items.toArray(), delimiter );
    }

    /***************************************************************************
     * Escapes all meta-characters in the regular expression string provided.
     * @param str the regular expression to be cleansed.
     * @return the cleansed string.
     **************************************************************************/
    public static String escapeRegexMetaChar( String str )
    {
        return escapeAllChars( str, REGEX_METAC );
    }

    /***************************************************************************
     * Escapes each character in the provided array found in the provided string
     * with a backslash '\'.
     * @param str the string to be cleansed.
     * @param chars char[] the characters to be escaped.
     * @return the cleansed string.
     **************************************************************************/
    public static String escapeAllChars( String str, char [] chars )
    {
        StringBuffer buffer = new StringBuffer();
        int offset = str.length();

        for( int idx = str.length() - 1; idx > -1; idx-- )
        {
            for( int mIdx = 0; mIdx < chars.length; mIdx++ )
            {
                if( str.charAt( idx ) == chars[mIdx] )
                {
                    if( idx > 0 )
                    {
                        if( str.charAt( idx - 1 ) == '\\' )
                        {
                            // Properly escaped, skip.
                            idx--;
                        }
                        else
                        {
                            // Not properly escaped, escape and add to buffer.
                            buffer.insert( 0,
                                "\\" + str.substring( idx, offset ) );
                            offset = idx;
                        }
                    }
                    else
                    {
                        buffer.insert( 0, "\\" + str.substring( idx, offset ) );
                        offset = idx;
                    }
                    break;
                }
            }
        }

        if( offset > 0 )
        {
            buffer.insert( 0, str.substring( 0, offset ) );
        }

        return buffer.toString();

    }

    /***************************************************************************
     * Scrolls to the row and column provided.
     * @param table the table to be scrolled.
     * @param row the row to be scrolled to.
     * @param col the column to be scrolled to.
     **************************************************************************/
    public static void scrollToVisible( JTable table, int row, int col )
    {
        Rectangle rect = table.getCellRect( row, col, true );

        rect = new Rectangle( rect );

        // LogUtils.printDebug( "Scrolling to: " + row + ", " + col + ":" +
        // rect.toString() );

        table.scrollRectToVisible( rect );
    }

    /***************************************************************************
     * Creates a transparent buffered image for the current device.
     * @param width the width of the image to be created.
     * @param height the height of the image to be created.
     * @return the newly created image.
     **************************************************************************/
    public static BufferedImage createTransparentImage( int width, int height )
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        return gc.createCompatibleImage( width, height,
            Transparency.TRANSLUCENT );
    }

    /***************************************************************************
     * Wraps {@code System#arraycopy(Object, int, Object, int, int)} because it
     * does not provide any information when an
     * {@link ArrayIndexOutOfBoundsException} occurs.
     * @param <T> the type of elements to be copied.
     * @param src the source array.
     * @param srcPos the starting position in the source array.
     * @param dest the destination array.
     * @param destPos the starting position in the destination array.
     * @param length the number of items to be copied.
     * @throws ArrayIndexOutOfBoundsException if writing the provided number of
     * elements to the destination array at the destination position from the
     * source array at the source position would result in an index out of
     * bounds of either the source read or the destination write.
     **************************************************************************/
    public static <T> void arrayCopy( T [] src, int srcPos, T [] dest,
        int destPos, int length ) throws ArrayIndexOutOfBoundsException
    {
        try
        {
            System.arraycopy( src, srcPos, dest, destPos, length );
        }
        catch( ArrayIndexOutOfBoundsException ex )
        {
            throw new ArrayIndexOutOfBoundsException(
                "Unable to copy " + length + " items from array of length " +
                    src.length + " @ " + srcPos + " to array of length " +
                    dest.length + " @ " + destPos );
        }
    }

    /***************************************************************************
     * Wraps {@link System#arraycopy(Object, int, Object, int, int)} to provide
     * better exceptions when arguments are not valid.
     * @param src the buffer from which bytes are copied.
     * @param srcPos the starting position in the source buffer.
     * @param dest the buffer to which bytes are copied.
     * @param destPos the starting position in the destination buffer.
     * @param length the number of bytes to be copied.
     * @throws ArrayIndexOutOfBoundsException if (a) the positions or length is
     * negative, or (b) a position + length is greater than the length of the
     * buffer.
     **************************************************************************/
    public static void byteArrayCopy( byte [] src, int srcPos, byte [] dest,
        int destPos, int length ) throws ArrayIndexOutOfBoundsException
    {
        try
        {
            System.arraycopy( src, srcPos, dest, destPos, length );
        }
        catch( ArrayIndexOutOfBoundsException ex )
        {
            throw new ArrayIndexOutOfBoundsException(
                "Unable to copy " + length + " bytes from array of length " +
                    src.length + " @ " + srcPos + " to array of length " +
                    dest.length + " @ " + destPos );
        }
    }

    /***************************************************************************
     * Creates a new array containing the provided item followed by the provided
     * array.
     * @param <T> the type of item to be added.
     * @param array the items to which the item shall be added.
     * @param item the item to push to beginning of the array.
     * @return the new array.
     **************************************************************************/
    public static <T> T [] addFirst( T [] array, T item )
    {
        ArrayList<T> list = new ArrayList<T>( Arrays.asList( array ) );

        list.add( 0, item );

        return list.toArray( array );
    }

    /***************************************************************************
     * Returns a plain text representation of the item currently on the system
     * clipboard if one is available. Returns a zero-length string if it is not
     * available or if any error occurs from
     * {@link Transferable#getTransferData(DataFlavor)}.
     * @return the plain text currently on the system clipboard.
     **************************************************************************/
    public static String getClipboardText()
    {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents( null );

        if( contents != null )
        {
            boolean hasTransferableText = contents.isDataFlavorSupported(
                DataFlavor.stringFlavor );

            if( hasTransferableText )
            {
                try
                {
                    result = ( String )contents.getTransferData(
                        DataFlavor.stringFlavor );
                }
                catch( UnsupportedFlavorException ex )
                {
                }
                catch( IOException ex )
                {
                }
            }
        }

        return result;
    }

    /***************************************************************************
     * Sets the text on the system clipboard to the provided text.
     * @param text the text to be placed on the system clipboard.
     **************************************************************************/
    public static void setClipboardText( String text )
    {
        StringSelection selection = new StringSelection( text );
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        ClipboardOwner co = ( cc, tc ) -> {
        };

        clipboard.setContents( selection, co );
    }

    /***************************************************************************
     * Searches the specified list for the specified object using the binary
     * search algorithm. The list must be sorted into ascending order according
     * to the natural ordering of its elements (as by the sort(List) method)
     * prior to making this call. If it is not sorted, the results are
     * undefined. If the list contains multiple elements equal to the specified
     * object, there is no guarantee which one will be found.</p>
     * @param <T> the type of items to be searched.
     * @param <K> the type of the key to be compared to each item in the list.
     * @param items the list to be searched.
     * @param key the item to be searched for.
     * @param c the method of comparing items of type {@code T} against items of
     * type {@code K}.
     * @return the index of the search key, if it is contained in the list;
     * otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion
     * point</i> is defined as the point at which the key would be inserted into
     * the list: the index of the first element greater than the key, or
     * <tt>list.size()</tt> if all elements in the list are less than the
     * specified key. Note that this guarantees that the return value will be
     * &gt;= 0 if and only if the key is found.
     **************************************************************************/
    public static <T, K> int binarySearch( List<T> items, K key,
        IComparator<T, K> c )
    {
        int low = 0;
        int high = items.size() - 1;

        while( low <= high )
        {
            int mid = ( low + high ) >>> 1;
            T midVal = items.get( mid );
            int cmp = c.compare( key, midVal );

            if( cmp > 0 )
            {
                low = mid + 1;
            }
            else if( cmp < 0 )
            {
                high = mid - 1;
            }
            else
            {
                return mid; // key found
            }
        }
        return -( low + 1 ); // key not found
    }

    /***************************************************************************
     * Returns the mask needed to remove the upper bits for values up to the
     * provided maximum value.
     * @param maxValue the value for which the mask will be generated.
     * @return the generated mask.
     **************************************************************************/
    public static int getMaskForValue( int maxValue )
    {
        int v = maxValue--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        // v++;

        return v;
    }

    /***************************************************************************
     * Returns a list containing the font family names of all available
     * monospace fonts that can display the ASCII numbers, lower case, and upper
     * case values.
     * @return the generated list.
     **************************************************************************/
    public static List<String> getMonospacedFonts()
    {
        List<String> fonts = new ArrayList<>();

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String [] fontFamilyNames = graphicsEnvironment.getAvailableFontFamilyNames();

        BufferedImage bufferedImage = new BufferedImage( 1, 1,
            BufferedImage.TYPE_INT_ARGB );
        Graphics graphics = bufferedImage.createGraphics();

        for( String fontFamilyName : fontFamilyNames )
        {
            boolean isMonospaced = true;

            int fontStyle = Font.PLAIN;
            int fontSize = 12;
            Font font = new Font( fontFamilyName, fontStyle, fontSize );
            FontMetrics fontMetrics = graphics.getFontMetrics( font );

            if( fontMetrics.getHeight() > 2 * fontSize )
            {
                continue;
            }

            int firstCharacterWidth = 0;
            boolean hasFirstCharacterWidth = false;
            int [] chars = new int[62];

            for( int i = 0; i < 10; i++ )
            {
                chars[i] = i + 0x30;
            }
            for( int i = 0; i < 26; i++ )
            {
                chars[i + 10] = i + 0x41;
            }
            for( int i = 0; i < 26; i++ )
            {
                chars[i + 36] = i + 0x61;
            }

            for( int i = 0; i < chars.length; i++ )
            {
                int codePoint = chars[i];

                if( !font.canDisplay( codePoint ) )
                {
                    isMonospaced = false;
                    // LogUtils.printDebug( "%s cannot display 0x%02X '%c' @
                    // %d",
                    // fontFamilyName, codePoint, ( char )codePoint, i );
                    break;
                }

                if( Character.isValidCodePoint( codePoint ) &&
                    ( Character.isLetter( codePoint ) ||
                        Character.isDigit( codePoint ) ) )
                {
                    char character = ( char )codePoint;
                    int characterWidth = fontMetrics.charWidth( character );
                    if( hasFirstCharacterWidth )
                    {
                        if( characterWidth != firstCharacterWidth )
                        {
                            isMonospaced = false;
                            break;
                        }
                    }
                    else
                    {
                        firstCharacterWidth = characterWidth;
                        hasFirstCharacterWidth = true;
                    }
                }
            }

            if( isMonospaced )
            {
                fonts.add( fontFamilyName );
            }
        }

        graphics.dispose();

        return fonts;
    }

    /***************************************************************************
     * Returns the provided text where the character after every space (
     * {@link Character#isSpace(char)} is upper case and all others are lower
     * case.
     * @param text the text to be converted.
     * @return the title case text.
     **************************************************************************/
    public static String toTitleCase( String text )
    {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for( char c : text.toCharArray() )
        {
            if( Character.isSpaceChar( c ) )
            {
                nextTitleCase = true;
            }
            else if( nextTitleCase )
            {
                c = Character.toTitleCase( c );
                nextTitleCase = false;
            }
            else
            {
                c = Character.toLowerCase( c );
            }

            titleCase.append( c );
        }

        return titleCase.toString();
    }

    /***************************************************************************
     * Tests the provided object and returns its cast to the type of the data
     * provided if it is assignable.
     * @param <T> The type of the data to be tested.
     * @param data the data to be cast to.
     * @param obj the object to be tested.
     * @return obj cast to {@code <T>} or {@code null} if it is not assignable.
     **************************************************************************/
    public static <T> T isAssignable( T data, Object obj )
    {
        if( obj != null && data.getClass().isAssignableFrom( obj.getClass() ) )
        {
            @SuppressWarnings( "unchecked")
            T item = ( T )obj;

            return item;
        }

        return null;
    }

    /***************************************************************************
     * Object that can compare an item object to a key object. Used for sorting
     * or searching a list of the items type by a field of the item.
     * @param <T> the type of key to be compared.
     * @param <K> the type of item to be compared.
     * @see Comparator#compare(Object, Object)
     **************************************************************************/
    public static interface IComparator<T, K>
    {
        /***********************************************************************
         * Compares its two arguments for order. Returns <ul> <li>a negative
         * integer if {@code thisKey < thatItem}</li> <li>zero if
         * {@code thisKey == thatItem}, or </li> <li>a positive integer if
         * {@code thisKey > thatItem},</li> </ul>
         * @param thisKey the key to be tested.
         * @param thatItem the item to be tested.
         * @return a negative integer, zero, or a positive integer as the key is
         * less than, equal to, or greater than the item.
         **********************************************************************/
        public int compare( K thisKey, T thatItem );
    }
}
