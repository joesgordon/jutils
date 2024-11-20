package jutils.core.io.parsers;

import jutils.core.NumberParsingUtils;
import jutils.core.ValidationException;
import jutils.core.io.IParser;
import jutils.core.net.IpAddress;
import jutils.core.net.IpAddress.IpVersion;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ip6AddressParser implements IParser<IpAddress>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IpAddress parse( String str ) throws ValidationException
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
