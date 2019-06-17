package org.jutils.ui.hex;

import java.util.ArrayList;
import java.util.List;

import org.jutils.NumberParsingUtils;

// TODO move to org.jutils.utils

/*******************************************************************************
 * Utility class to contain static methods for parsing strings as hex and
 * converting bytes to strings.
 ******************************************************************************/
public final class HexUtils
{
    /**
     * Lookup table to contain the string representations of every possible
     * byte.
     */
    private static final HexStringConverter HEX_STRINGS = new HexStringConverter();

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private HexUtils()
    {
    }

    /***************************************************************************
     * Converts a byte to an integer sans sign extension.
     * @param b the byte to be converted.
     * @return the integer representation of the byte.
     **************************************************************************/
    public static int toUnsigned( byte b )
    {
        return b & 0x0FF;
    }

    /***************************************************************************
     * Converts a nibble (integer values 0 - 15) to a hexadecimal character.
     * @param nibble the nibble to be converted.
     * @return the hexadecimal character representation of the nibble.
     **************************************************************************/
    public static char toHex( int nibble )
    {
        switch( nibble )
        {
            case 0:
                return '0';
            case 1:
                return '1';
            case 2:
                return '2';
            case 3:
                return '3';
            case 4:
                return '4';
            case 5:
                return '5';
            case 6:
                return '6';
            case 7:
                return '7';
            case 8:
                return '8';
            case 9:
                return '9';
            case 10:
                return 'A';
            case 11:
                return 'B';
            case 12:
                return 'C';
            case 13:
                return 'D';
            case 14:
                return 'E';
            case 15:
                return 'F';
        }

        throw new IllegalArgumentException(
            "Outside the range of a nibble: " + nibble );
    }

    /***************************************************************************
     * Converts a byte to a two digit hexadecimal string.
     * @param b the byte to be converted.
     * @return the hexadecimal string representation of the byte.
     **************************************************************************/
    public static String toHexString( byte b )
    {
        return toHexString( toUnsigned( b ) );
    }

    /***************************************************************************
     * Converts the provided integer representation of an unsigned byte (integer
     * values 0 - 255) to a hexadecimal string.
     * @param b the byte to be converted.
     * @return the hexadecimal string representation of the byte.
     * @throws ArrayIndexOutOfBoundsException if the input value is outside the
     * range 0 - 255 inclusive.
     **************************************************************************/
    public static String toHexString( int b )
        throws ArrayIndexOutOfBoundsException
    {
        return HEX_STRINGS.get( b );
    }

    /***************************************************************************
     * Converts the byte array to a single string. The resultant string is not
     * prefaced with a <i>0x</i>.
     * @param bytes the bytes to be converted.
     * @return the hexadecimal string representation of the bytes.
     **************************************************************************/
    public static String toHexString( byte [] bytes )
    {
        return toHexString( bytes, "" );
    }

    /***************************************************************************
     * @param bytes
     * @param delim
     * @return
     **************************************************************************/
    public static String toHexString( byte [] bytes, String delim )
    {
        return toHexString( bytes, 0, bytes.length, delim );
    }

    /***************************************************************************
     * @param bytes
     * @param index
     * @param len
     * @param delim
     * @return
     **************************************************************************/
    public static String toHexString( byte [] bytes, int index, int len,
        String delim )
    {
        List<Byte> byteList = new ArrayList<Byte>( bytes.length );

        int end = index + len;

        for( int i = index; i < end; i++ )
        {
            byteList.add( bytes[i] );
        }

        return toHexString( byteList, delim );
    }

    /***************************************************************************
     * Converts the list of bytes to a single string. The resultant string is
     * not prefaced with a <i>0x</i>.
     * @param bytes the bytes to be converted.
     * @return the hexadecimal string representation of the bytes.
     **************************************************************************/
    public static String toHexString( List<Byte> bytes )
    {
        return toHexString( bytes, "" );
    }

    /***************************************************************************
     * Converts the byte array to a single string inserting the specified
     * delimiter between each byte. The resultant string is not prefaced with a
     * <i>0x</i>.
     * @param bytes the bytes to be converted.
     * @param delim the delimiter to be used.
     * @return the hexadecimal string representation of the bytes.
     **************************************************************************/
    public static String toHexString( List<Byte> bytes, String delim )
    {
        StringBuilder str = new StringBuilder();

        for( int i = 0; i < bytes.size(); i++ )
        {
            if( i > 0 )
            {
                str.append( delim );
            }

            str.append( HEX_STRINGS.get( toUnsigned( bytes.get( i ) ) ) );
        }

        return str.toString();
    }

    /***************************************************************************
     * Removed all non-hexadecimal digits from the provided string.
     * @param messyString the string to be cleansed.
     * @return a sting containing only hexadecimal digits.
     **************************************************************************/
    public static String trimHexString( String messyString )
    {
        return messyString.replaceAll( "[^0-9a-fA-F]", "" );
    }

    /***************************************************************************
     * Converts a string of hexadecimal digits to a byte array.
     * @param text the string to be converted.
     * @return the byte array.
     * @throws NumberFormatException see {@link #fromHexString(String)}.
     **************************************************************************/
    public static byte [] fromHexStringToArray( String text )
        throws NumberFormatException
    {
        List<Byte> byteList = fromHexString( text );

        return asArray( byteList );
    }

    /***************************************************************************
     * @param text
     * @return
     * @throws NumberFormatException
     **************************************************************************/
    public static byte parseHex( String text ) throws NumberFormatException
    {
        if( text.length() != 2 )
        {
            throw new NumberFormatException(
                "Incorrect length to specify a byte in hexadecimal" );
        }

        int b = 0;
        char c = text.charAt( 0 );

        b = NumberParsingUtils.digitFromHex( c );
        b = b << 4;
        b |= NumberParsingUtils.digitFromHex( c );

        return ( byte )b;
    }

    /***************************************************************************
     * Converts a string of hexadecimal digits to a list of bytes.
     * @param text the string to be converted.
     * @return the list of bytes.
     * @throws NumberFormatException if any non-hexadecimal digit is found, if
     * the string is empty, or see {@link NumberParsingUtils#digitFromHex}.
     **************************************************************************/
    public static List<Byte> fromHexString( String text )
        throws NumberFormatException
    {
        if( text.length() == 0 )
        {
            throw new NumberFormatException( "The string is empty" );
        }

        List<Byte> bytes = new ArrayList<Byte>( text.length() / 2 );
        int b = 0;
        char c = '-';

        for( int i = 0; i < text.length(); i++ )
        {
            c = text.charAt( i );

            if( i % 2 == 0 )
            {
                b = NumberParsingUtils.digitFromHex( c );
            }
            else
            {
                b = b << 4;
                b |= NumberParsingUtils.digitFromHex( c );
                bytes.add( ( byte )b );
            }
        }

        // ---------------------------------------------------------------------
        // Check the text length only after checking each character because the
        // user should be notified of an incorrect character as they are typing
        // if the first hex digit of a byte is incorrect.
        // ---------------------------------------------------------------------
        if( text.length() % 2 != 0 )
        {
            throw new NumberFormatException(
                "The string must be an even number of hexadecimal digits" );
        }

        return bytes;
    }

    /***************************************************************************
     * Converts an array of bytes to a list of bytes.
     * @param array
     * @return
     **************************************************************************/
    public static List<Byte> asList( byte [] array )
    {
        List<Byte> bytes = new ArrayList<Byte>( array.length );

        for( int i = 0; i < array.length; i++ )
        {
            bytes.add( array[i] );
        }

        return bytes;
    }

    /***************************************************************************
     * Converts a list of bytes to an array of bytes.
     * @param bytes
     * @return
     **************************************************************************/
    public static byte [] asArray( List<Byte> bytes )
    {
        byte [] array = new byte[bytes.size()];

        for( int i = 0; i < bytes.size(); i++ )
        {
            array[i] = bytes.get( i );
        }

        return array;
    }

    /***************************************************************************
     * Converts the byte from binary coded decimal (BCD) to its two's complement
     * form. E.g. the byte 0x10 will be returned as 0x0A. This function performs
     * no error checking on the BCD byte.
     * @param b
     * @return
     **************************************************************************/
    public static byte fromBcd( byte b )
    {
        return ( byte )( ( ( ( b >>> 4 ) & 0x0F ) * 10 ) + ( b & 0x0F ) );
    }

    // TODO comment
    /***************************************************************************
     * @param d
     * @return
     **************************************************************************/
    public static byte toBcd( int d )
    {
        return ( byte )( ( ( d / 10 ) << 4 ) | ( d % 10 ) );
    }

    /***************************************************************************
     * @param buffer
     * @param start
     * @param count
     **************************************************************************/
    public static void cleanAscii( byte [] buffer, int start, int count )
    {
        for( int i = start; i < count; i++ )
        {
            byte ch = buffer[i];
            if( ch < 0x20 || ch > 0x7e )
            {
                ch = ' ';
                // ch = ( char )( -1 );
            }
            buffer[i] = ch;
        }
    }

    /***************************************************************************
     * Helper class for creating and accessing a lookup table for hexadecimal
     * string representations of bytes.
     **************************************************************************/
    private static class HexStringConverter
    {
        /**  */
        private final String [] BYTE_STRINGS;

        /**
         * 
         */
        public HexStringConverter()
        {
            this.BYTE_STRINGS = new String[256];

            for( int i = 0; i < BYTE_STRINGS.length; i++ )
            {
                BYTE_STRINGS[i] = buildHexString( i );
            }
        }

        /**
         * @param i
         * @return
         */
        public String get( int i )
        {
            return BYTE_STRINGS[i];
        }

        /**
         * @param b
         * @return
         */
        private static String buildHexString( int b )
        {
            String s = Integer.toHexString( b ).toUpperCase();

            if( b < 0x10 )
            {
                s = "0" + s;
            }

            return s;
        }
    }
}
