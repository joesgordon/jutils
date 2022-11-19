package org.jutils.core.io.parsers;

import java.util.List;

import org.jutils.core.NumberParsingUtils;
import org.jutils.core.Utils;
import org.jutils.core.ValidationException;
import org.jutils.core.io.IParser;
import org.jutils.core.net.IpAddress;
import org.jutils.core.net.IpAddress.IpVersion;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IpAddressParser implements IParser<IpAddress>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IpAddress parse( String str ) throws ValidationException
    {
        if( str.contains( "." ) )
        {
            return parseIpv4( str );
        }
        else if( str.contains( ":" ) )
        {
            return parseIpv6( str );
        }

        throw new ValidationException(
            "An IP address must contain a period or a colon" );
    }

    /***************************************************************************
     * @param str
     * @return
     * @throws ValidationException
     **************************************************************************/
    private static IpAddress parseIpv4( String str ) throws ValidationException
    {
        IpAddress address = new IpAddress( IpVersion.IPV4 );
        IntegerParser fieldParser = new IntegerParser( 0, 255 );
        List<String> fields = Utils.splitSkip( str, false, '.' );

        if( fields.size() > 4 )
        {
            throw new ValidationException(
                "Too many fields in an IP4 address: " + fields.size() );
        }
        else if( fields.size() < 4 )
        {
            throw new ValidationException(
                "Too few fields in an IP4 address: " + fields.size() );
        }

        for( int i = 0; i < fields.size(); i++ )
        {
            try
            {
                int num = fieldParser.parse( fields.get( i ) );

                address.address[i] = ( byte )num;
            }
            catch( ValidationException ex )
            {
                throw new ValidationException(
                    "Cannot parse octect " + ( i + 1 ) + ": " + ex.getMessage(),
                    ex );
            }
        }

        return address;
    }

    /***************************************************************************
     * @param str
     * @return
     * @throws ValidationException
     **************************************************************************/
    private static IpAddress parseIpv6( String str ) throws ValidationException
    {
        IpAddress address = new IpAddress( IpVersion.IPV6 );
        int [] values = new int[8];

        int fieldIndex = 0;
        int nibbleCnt = 0;

        int colonCount = 0;
        int doubleIndex = -1;
        int doubleCharIdx = -1;

        for( int i = 0; i < str.length(); i++ )
        {
            char c = str.charAt( i );
            int v = -1;

            switch( c )
            {
                case ':':
                    colonCount++;
                    if( colonCount == 2 )
                    {
                        if( doubleIndex > -1 )
                        {
                            String msg = String.format(
                                "Double colons found at indexes %d and %d; only 1 allowed",
                                doubleCharIdx - 1, i - 1 );
                            throw new ValidationException( msg );
                        }
                        doubleIndex = fieldIndex;
                        doubleCharIdx = i;
                    }
                    else
                    {
                        fieldIndex++;
                    }
                    nibbleCnt = 0;
                    break;

                default:
                    try
                    {
                        v = NumberParsingUtils.digitFromHex( c );
                        nibbleCnt++;
                        colonCount = 0;
                    }
                    catch( NumberFormatException ex )
                    {
                        String msg = String.format(
                            "Unable to parse IPv6 from string at index %d: %s; %s",
                            i, str, ex.getMessage() );
                        throw new ValidationException( msg );
                    }
            }

            if( fieldIndex >= values.length )
            {
                String msg = String.format(
                    "Invalid address after index %d: %s", i, str );
                throw new ValidationException( msg );
            }

            if( colonCount > 2 )
            {
                String msg = String.format( "Invalid colon at index %d: %s", i,
                    str );
                throw new ValidationException( msg );
            }

            if( nibbleCnt > 4 )
            {
                String msg = String.format( "Invalid character at index %d: %s",
                    i, str );
                throw new ValidationException( msg );
            }

            if( v > -1 )
            {
                values[fieldIndex] = ( values[fieldIndex] << 4 ) | ( 0xF & v );
            }
        }

        // shift values from double colon right and assign

        if( doubleIndex > -1 )
        {
            int count = fieldIndex - doubleIndex + 1;
            for( int i = 0; i < count; i++ )
            {
                int di = 7 - i;
                int si = doubleIndex + i;

                values[di] = values[si];
            }

            count = ( 7 - doubleIndex + 1 ) - count;
            for( int i = doubleIndex; i < count; i++ )
            {
                values[i] = 0;
            }
        }

        byte [] bytes = new byte[values.length * 2];
        for( int i = 0; i < values.length; i++ )
        {
            bytes[2 * i] = ( byte )( ( values[i] >> 8 ) & 0xFF );
            bytes[2 * i + 1] = ( byte )( values[i] & 0xFF );
        }
        address.set( bytes );

        return address;
    }
}
