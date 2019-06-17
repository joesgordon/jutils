package org.jutils;

/*******************************************************************************
 * Static helper class for parsing numbers. This class exists because the java
 * standard library is, IMHO, broken. If you have a long (int, short, and byte
 * also exhibit the same behavior) called {@code foo}, you should be able to
 * execute the following:<code> foo = Long.parseString( Long.toHexString( foo ),
 * 16 ) </code> And you can. As long as {@code foo} is not negative. If
 * {@code foo} is negative, the code will throw a NumberFormatException. You
 * don't believe me, do you? Go ahead. Try it. Oh, no. I'll
 * wait...<br>...<br>...<br>...<br>...<br>...<br>...<br>...<br>...<br>...<br>...
 * <br>...<br>...<br>...<br>...<br>...<br><br> See. Broke, didn't it? Well I
 * tested the code below and it appears to be faster than all the
 * INTEGRAL_CLASS.parse anyway.
 ******************************************************************************/
public final class NumberParsingUtils
{
    /***************************************************************************
     * Parses the provided string as a hexadecimal byte of size
     * {@link Integer#SIZE}. If the string contains &quot;0x&quot; at the
     * beginning it will be ignored.
     * @param s the string to be parsed.
     * @return the byte parsed.
     * @throws NumberFormatException any error in the format of the number.
     **************************************************************************/
    public static int parseHexByte( String s ) throws NumberFormatException
    {
        return ( int )parseHex( s, 2 );
    }

    /***************************************************************************
     * Parses the provided string as a hexadecimal short of size
     * {@link Integer#SIZE}. If the string contains &quot;0x&quot; at the
     * beginning it will be ignored.
     * @param s the string to be parsed.
     * @return the short parsed.
     * @throws NumberFormatException any error in the format of the number.
     **************************************************************************/
    public static int parseHexShort( String s ) throws NumberFormatException
    {
        return ( int )parseHex( s, 4 );
    }

    /***************************************************************************
     * Parses the provided string as a hexadecimal integer of size
     * {@link Integer#SIZE}. If the string contains &quot;0x&quot; at the
     * beginning it will be ignored.
     * @param s the string to be parsed.
     * @return the integer parsed.
     * @throws NumberFormatException any error in the format of the number.
     **************************************************************************/
    public static int parseHexInteger( String s ) throws NumberFormatException
    {
        return ( int )parseHex( s, 8 );
    }

    /***************************************************************************
     * Parses the provided string as a hexadecimal integer of size
     * {@link Long#SIZE}. If the string contains &quot;0x&quot; at the beginning
     * it will be ignored.
     * @param s the string to be parsed.
     * @return the long parsed.
     * @throws NumberFormatException any error in the format of the number.
     **************************************************************************/
    public static long parseHexLong( String s ) throws NumberFormatException
    {
        return parseHex( s, 16 );
    }

    /***************************************************************************
     * @param s the string to be parsed. If the string contains &quot;0x&quot;
     * at the beginning it will be ignored.
     * @param limit the maximum number (inclusive) of hexadecimal digits.
     * @return the long parsed.
     * @throws NumberFormatException any error in the format of the number: <ul>
     * <li>Any non-hexadecimal character other than space, tab, or a leading
     * "0x"</li><li>More than {@code limit} digits are encountered</li></ul>
     **************************************************************************/
    public static long parseHex( String s, int limit )
        throws NumberFormatException
    {
        if( s.startsWith( "0x" ) )
        {
            s = s.substring( 2 );
        }

        long result = 0;
        long digit = 0;
        char c;
        int len = 0;

        for( int i = 0; i < s.length(); i++ )
        {
            c = s.charAt( i );

            if( c == ' ' || c == '\t' )
            {
                continue;
            }

            digit = digitFromHex( c );

            if( ++len > limit )
            {
                continue;
            }

            result <<= 4;
            result |= digit;

            // LogUtils.printDebug(
            // "Len: %02d, digit: %02d, result: 0x%016X with char %c", len,
            // digit, result, c );
        }

        if( len == 0 )
        {
            throw new NumberFormatException( "Empty string" );
        }
        else if( len > limit )
        {
            throw new NumberFormatException(
                "The number may be no longer than " + limit +
                    " hexadecimal characters: [" + len + "] '" + s + "'" );
        }

        // Utils.printStackTrace();

        return result;
    }

    /***************************************************************************
     * Returns the integer representation of the hexadecimal character.
     * @param c the character to be parsed.
     * @return the integer parsed.
     * @throws NumberFormatException if the character is not [0-9a-fA-F].
     **************************************************************************/
    public static int digitFromHex( char c ) throws NumberFormatException
    {
        switch( c )
        {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'a':
                return 0xA;
            case 'b':
                return 0xB;
            case 'c':
                return 0xC;
            case 'd':
                return 0xD;
            case 'e':
                return 0xE;
            case 'f':
                return 0xF;
            case 'A':
                return 0xA;
            case 'B':
                return 0xB;
            case 'C':
                return 0xC;
            case 'D':
                return 0xD;
            case 'E':
                return 0xE;
            case 'F':
                return 0xF;
        }

        throw new NumberFormatException(
            "'" + c + "' is not a hexadecimal digit" );
    }

    /***************************************************************************
     * Converts the provided long into a string of its binary representation
     * without any leading 0's.
     * @param value the long to convert.
     * @return the binary string.
     **************************************************************************/
    public static String toBinaryString( long value )
    {
        StringBuilder builder = new StringBuilder( 64 );

        boolean foundLeading = false;

        for( int i = 0; i < 64; i++ )
        {
            int b = ( int )( ( value >>> ( 63 - i ) ) & 1 );

            if( b == 1 || foundLeading )
            {
                foundLeading = true;
                builder.append( b == 0 ? "0" : "1" );
            }
        }

        String str = builder.toString();

        return str.isEmpty() ? "0" : str;
    }

    /***************************************************************************
     * Parses binary from the provided text into a long.
     * @param text the binary string.
     * @return the value of the binary string.
     * @throws NumberFormatException any error in converting the binary sting.
     **************************************************************************/
    public static long parseBinary( String text ) throws NumberFormatException
    {
        long value = 0;

        if( text.isEmpty() )
        {
            throw new NumberFormatException(
                "Cannot parse binary from an empty string" );
        }

        if( text.length() > 64 )
        {
            throw new NumberFormatException(
                "Cannot parse a long from a " + text.length() + "-bit string" );
        }

        for( int i = 0; i < text.length(); i++ )
        {
            char c = text.charAt( i );

            value <<= 1;

            if( c == '1' )
            {
                value |= 1;
            }
            else if( c != '0' )
            {
                throw new NumberFormatException(
                    "Character is not binary: " + c );
            }
        }

        return value;
    }
}
